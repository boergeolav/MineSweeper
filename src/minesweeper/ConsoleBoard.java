package minesweeper;

public class ConsoleBoard extends MinesweeperBoard {
	
	private char[][] representationBoard;
	
	public ConsoleBoard(int width, int height, int numberOfMines) {
		super(width, height, numberOfMines);
		resetRepresentationBoard();
	}
	
	private void resetRepresentationBoard() {
		this.representationBoard = new char[height][width];
		for(int x = 0; x < height; x++) {
			for(int y = 0; y < width; y++) {
				this.representationBoard[x][y] = EMPTY_SQUARE;
			}
		}
	}
	
	public void printMineBoard() {
		if(mineBoard == null) return;
		String output = "";
		for(int x = 0; x < height; x++) {
			for(int y = 0; y < width; y++) {
				output += mineBoard[x][y] ? "[" + MINE_SQUARE + "]" : "[" + EMPTY_SQUARE + "]";
			}
			output += "\n";
		}
		System.out.println(output);
	}
	
	public void printRepresentationBoard() {
		int x, y;
		String output = "    ";
		for(y = 0; y < width; y++) {
			output += y + "  ";
		}
		output += "\n";
		for(x = 0; x < height; x++) {
			output += " " + x + " ";
			for(y = 0; y < width; y++) {
				output += "[" + representationBoard[x][y] + "]";
			}
			output += "\n";
		}
		System.out.println(output);
	}

	@Override
	protected void setCharAt(int xCoord, int yCoord, char newChar) {
		representationBoard[xCoord][yCoord] = newChar;
	}

	@Override
	protected char getCharAt(int xCoord, int yCoord) {
		return representationBoard[xCoord][yCoord];
	}
}
