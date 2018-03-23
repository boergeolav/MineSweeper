package minesweeper;

public abstract class MinesweeperBoard {
	
	static enum GameStatus {
		GAME_WON, GAME_LOST, GAME_CONTINUES
	}
	
	public final static char EMPTY_SQUARE = ' ';
	public final static char CHECKED_SQUARE = '.';
	public final static char MINE_SQUARE = 'M';
	public final static char MINE_FLAG_SQUARE = 'F';
	public final static char MYSTERY_FLAG_SQUARE = '?';
	
	protected boolean[][] mineBoard;
	protected int width, height;
	protected int numberOfMines;
	
	public MinesweeperBoard(int width, int height, int numberOfMines) {
		this.width = width;
		this.height = height;
		this.numberOfMines = numberOfMines;
	}
	
	protected abstract void setCharAt(int xCoord, int yCoord, char newChar);
	
	protected abstract char getCharAt(int xCoord, int yCoord);
	
	public GameStatus setXY(int xCoord, int yCoord, boolean setFlag) {
		if(mineBoard == null) {
			generateMineBoard(xCoord, yCoord);
		}
		if(setFlag) {
			switch(getCharAt(xCoord, yCoord)) {
			case EMPTY_SQUARE:
				setCharAt(xCoord, yCoord, MINE_FLAG_SQUARE);
				break;
			case MINE_FLAG_SQUARE:
				setCharAt(xCoord, yCoord, MYSTERY_FLAG_SQUARE);
				break;
			case MYSTERY_FLAG_SQUARE:
				setCharAt(xCoord, yCoord, EMPTY_SQUARE);
				break;
			default:
				break;
			}
		}
		else {
			if(getCharAt(xCoord, yCoord) == EMPTY_SQUARE || getCharAt(xCoord, yCoord) == MYSTERY_FLAG_SQUARE) {
				// If the checked box contains a mine, reveal all mines and return 'L' because the game is lost.
				if(mineBoard[xCoord][yCoord]) {
					for(int x = 0; x < height; x++) {
						for(int y = 0; y < width; y++) {
							if(mineBoard[x][y]) {
								setCharAt(x, y, MINE_SQUARE);
							}
						}
					}
					return GameStatus.GAME_LOST;
				}
				// The box did not contain a mine. Check 
				else {
					checkThisAndNeighboringSquares(xCoord, yCoord);
					if(hasWon()) {
						return GameStatus.GAME_WON;
					}
				}
			}
		}
		return GameStatus.GAME_CONTINUES;
	}
	
	protected boolean hasWon() {
		int numberOfCheckedBoxes = 0;
		for(int x = 0; x < height; x++) {
			for(int y = 0; y < width; y++) {
				char c = getCharAt(x, y);
				if(c != EMPTY_SQUARE && c != MINE_FLAG_SQUARE && c != MYSTERY_FLAG_SQUARE && c != MINE_SQUARE) numberOfCheckedBoxes++;
			}
		}
		return height * width - numberOfCheckedBoxes == numberOfMines;
	}
	
	
	protected void checkThisAndNeighboringSquares(int xCoord, int yCoord) {
		// Return if indices are out of bounds, or if this box has already been checked.
		if(xCoord < 0 || xCoord > height - 1 || yCoord < 0 || yCoord > width - 1 || getCharAt(xCoord, yCoord) == CHECKED_SQUARE)
			return;
		int x, y, numberOfNeighboringMines = 0;
		// Check neighbors for mines and count the number of occurrences.
		for(x = -1; x <= 1; x++) {
			// Skip if x is out of bounds.
			if(xCoord + x < 0 || xCoord + x > height - 1) continue;
			for(y = -1; y <= 1; y++) {
				// Skip if not a neighbor, or if y is out of bounds.
				if((x == 0 && y == 0) || yCoord + y < 0 || yCoord + y > width - 1)
					continue;
				if(mineBoard[xCoord + x][yCoord + y]) numberOfNeighboringMines++;
			}
		}
		// If at least one neighbor has a mine, set representationBoard[xCoord][yCoord] to 
		// the number of neighboring mines and then stop.
		if(numberOfNeighboringMines > 0) {
			setCharAt(xCoord, yCoord, (char)(numberOfNeighboringMines + '0'));
		}
		// If no neighbors have mines, set representationBoard[xCoord][yCoord] to CHECKED_SQUARE and
		// do the same check on all neighbors.
		else {
			setCharAt(xCoord, yCoord, CHECKED_SQUARE);
			checkThisAndNeighboringSquares(xCoord - 1, yCoord - 1); checkThisAndNeighboringSquares(xCoord - 1, yCoord); checkThisAndNeighboringSquares(xCoord - 1, yCoord + 1);
			checkThisAndNeighboringSquares(xCoord,	   yCoord - 1); checkThisAndNeighboringSquares(xCoord,     yCoord); checkThisAndNeighboringSquares(xCoord,     yCoord + 1);
			checkThisAndNeighboringSquares(xCoord + 1, yCoord - 1); checkThisAndNeighboringSquares(xCoord + 1, yCoord); checkThisAndNeighboringSquares(xCoord + 1, yCoord + 1);
		}
	}
	
	/**
	 * Generates a new mine board of size {@code height} * {@code width} and places
	 * mines randomly across the board except at coordinates ({@code forbiddenX, forbiddenY})
	 * @param forbiddenX
	 * @param forbiddenY
	 */
	protected void generateMineBoard(int forbiddenX, int forbiddenY) {
		this.mineBoard = new boolean[height][width];
		int x, y, NoM = this.numberOfMines;
		while(NoM > 0) {
			x = (int)(Math.random()*height);
			y = (int)(Math.random()*width);
			if(mineBoard[x][y] || (x == forbiddenX && y == forbiddenY)) continue;
			else {
				mineBoard[x][y] = true;
				NoM--;
			}
		}
	}

}
