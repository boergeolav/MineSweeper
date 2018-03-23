package minesweeper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class GuiBoard extends MinesweeperBoard implements ActionListener {
	
	private static final int BEGINNER_NUMBER_OF_MINES = 10;
	private static final int BEGINNER_WIDTH = 9;
	private static final int BEGINNER_HEIGHT = 9;
	
	private static final int INTERMEDIATE_NUMBER_OF_MINES = 40;
	private static final int INTERMEDIATE_WIDTH = 16;
	private static final int INTERMEDIATE_HEIGHT = 16;
	
	private static final int ADVANCED_NUMBER_OF_MINES = 99;
	private static final int ADVANCED_WIDTH = 16;
	private static final int ADVANCED_HEIGHT = 30;
	
	private int numberOfMineFlags;
	
	private JFrame frame;
	
	private JMenuBar dropDownMenu;
	private JMenu menu;
	private JMenuItem newGameItem, optionsItem, quitItem;
	
	private JLabel minesLeftLabel;
	private JTextField minesLeftTextField;
	private JPanel minesLeftPanel;
	
	private JPanel boardPanel;
	private Square[][] representationBoard;
	
	private JFrame optionsFrame;
	private JPanel optionsGroupPanel;
	private ButtonGroup optionsGroup;
	private JRadioButton beginnerRadioButton;
	private JRadioButton intermediateRadioButton;
	private JRadioButton advancedRadioButton;
	private JRadioButton customRadioButton;
	private JTextField beginnerTextField;
	private JTextField intermediateTextField;
	private JTextField advancedTextField;
	private JPanel optionsOkCancelPanel;
	private JButton okButton;
	private JButton cancelButton;

	public GuiBoard(int width, int height, int numberOfMines) {
		super(width, height, numberOfMines);
		
		dropDownMenu = new JMenuBar();
		menu = new JMenu("Game");
		dropDownMenu.add(menu);
		newGameItem = new JMenuItem("New game");
		newGameItem.addActionListener(this);
		menu.add(newGameItem);
		optionsItem = new JMenuItem("Options");
		optionsItem.addActionListener(this);
		menu.add(optionsItem);
		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(this);
		menu.add(quitItem);
		
		minesLeftLabel = new JLabel("Mines left: ");
		minesLeftTextField = new JTextField("" + (numberOfMines - numberOfMineFlags));
		minesLeftTextField.setEnabled(false);
		minesLeftPanel = new JPanel();
		minesLeftPanel.add(minesLeftLabel);
		minesLeftPanel.add(minesLeftTextField);
		
		resetRepresentationBoard();
	}
	
	private void resetRepresentationBoard() {
		if(frame != null)
			frame.setVisible(false);
		this.numberOfMineFlags = 0;
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(width, height, 0, 0));
		representationBoard = new Square[width][height];
		for(int row = 0; row < representationBoard.length; row++) {
			for(int col = 0; col < representationBoard[row].length; col++) {
				representationBoard[row][col] = new Square(this, row, col);
				gridPanel.add(representationBoard[row][col]);
			}
		}
		boardPanel = new JPanel();
		boardPanel.add(gridPanel);
		
		// Place and show the window:
		frame = new JFrame("MineSweeper");
		frame.setSize((width + 5) * Square.SQUARE_WIDTH, (height + 5) * Square.SQUARE_HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		frame.setLayout(new BorderLayout());
		frame.add(BorderLayout.NORTH, dropDownMenu);
		frame.add(BorderLayout.CENTER, boardPanel);
		frame.add(BorderLayout.SOUTH, minesLeftPanel);
		
		frame.setVisible(true);
	}

	public void squareClicked(int row, int column, int button) {
		GameStatus gs = GameStatus.GAME_CONTINUES;
		if(button == MouseEvent.BUTTON1) {
			gs = setXY(row, column, false);
		}
		else if (button == MouseEvent.BUTTON3) {
			setXY(row, column, true);
		}
		int opt = -1;
		String optionDialogText = "";
		String optionDialogTitle = "";
		Object[] options = {"New game?", "Replay this game?", "Quit?"};
		if(gs == GameStatus.GAME_WON || gs == GameStatus.GAME_LOST) {
			for(int x = 0; x < height; x++) {
				for(int y = 0; y < height; y++) {
					representationBoard[x][y].setEnabled(false);
				}
			}
			if(gs == GameStatus.GAME_WON) {
				optionDialogText = "You won! Well done!";
				optionDialogTitle = "You won";
			}
			else {
				optionDialogText = "You lost... Better luck next time!";
				optionDialogTitle = "You lost";
			}
			opt = JOptionPane.showOptionDialog(null,
					optionDialogText,
					optionDialogTitle,
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					null);
			switch(opt) {
			case JOptionPane.YES_OPTION:    // New game
				startNewGame(true);
				break;
			case JOptionPane.NO_OPTION:		// Replay game
				startNewGame(false);
				break;
			case JOptionPane.CANCEL_OPTION: // Quit
				System.exit(0);
				break;
			case JOptionPane.CLOSED_OPTION:
				System.exit(0);
				break;
			default: 
				break;
			}
		}
		frame.repaint();
	}

	@Override
	protected void setCharAt(int xCoord, int yCoord, char newChar) {
		representationBoard[xCoord][yCoord].setMark(newChar);
		if(newChar == MINE_FLAG_SQUARE) numberOfMineFlags++;
		if(newChar == MYSTERY_FLAG_SQUARE) numberOfMineFlags--;
		minesLeftTextField.setText("" + (numberOfMines - numberOfMineFlags));
	}

	@Override
	protected char getCharAt(int xCoord, int yCoord) {
		return representationBoard[xCoord][yCoord].getMark();
	}

	private void options() {
		if(optionsFrame == null) {
			optionsFrame = new JFrame("Options");
			optionsFrame.setLayout(new GridLayout(0, 1));
			optionsGroup = new ButtonGroup();
			beginnerRadioButton = new JRadioButton("Beginner");
			beginnerRadioButton.addActionListener(this);
			beginnerTextField = new JTextField("10 mines. Board with 9 x 9 squares.");
			beginnerTextField.setEnabled(false);
			intermediateRadioButton = new JRadioButton("Intermediate");
			intermediateRadioButton.addActionListener(this);
			intermediateTextField = new JTextField("40 mines. Board with 16 x 16 squares.");
			intermediateTextField.setEnabled(false);
			advancedRadioButton = new JRadioButton("Advanced");
			advancedRadioButton.addActionListener(this);
			advancedTextField = new JTextField("99 mines. Board with 16 x 30 squares.");
			advancedTextField.setEnabled(false);
			customRadioButton = new JRadioButton("Custom");
			customRadioButton.addActionListener(this);
			customRadioButton.setEnabled(false);
			optionsGroup.add(beginnerRadioButton);
			optionsGroup.add(intermediateRadioButton);
			optionsGroup.add(advancedRadioButton);
			optionsGroup.add(customRadioButton);
			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);
			optionsGroupPanel = new JPanel(new GridLayout(0, 2));
			optionsGroupPanel.add(beginnerRadioButton);
			optionsGroupPanel.add(beginnerTextField);
			optionsGroupPanel.add(intermediateRadioButton);
			optionsGroupPanel.add(intermediateTextField);
			optionsGroupPanel.add(advancedRadioButton);
			optionsGroupPanel.add(advancedTextField);
			optionsGroupPanel.add(customRadioButton);
			optionsOkCancelPanel = new JPanel();
			optionsOkCancelPanel.add(okButton);
			optionsOkCancelPanel.add(cancelButton);
			optionsFrame.add(optionsGroupPanel);
			optionsFrame.add(optionsOkCancelPanel);
			optionsFrame.setSize(500, 500);
			optionsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		}
		optionsFrame.setVisible(true);
	}
	
	private void startNewGame(boolean newGame) {
		resetRepresentationBoard();
		if(newGame)
			mineBoard = null;
	}
	
	private void startNewGame(int width, int height, int numberOfMines, boolean newGame) {
		this.width = width;
		this.height = height;
		this.numberOfMines = numberOfMines;
		startNewGame(newGame);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(newGameItem)) {
			startNewGame(true);
		}
		else if(e.getSource().equals(optionsItem)) {
			options();
		}
		else if(e.getSource().equals(quitItem)) {
			System.exit(0);
		}
		else if(e.getSource().equals(okButton)) {
			if(beginnerRadioButton.isSelected()) {
				startNewGame(BEGINNER_WIDTH, BEGINNER_HEIGHT, BEGINNER_NUMBER_OF_MINES, true);
			}
			else if(intermediateRadioButton.isSelected()) {
				startNewGame(INTERMEDIATE_WIDTH, INTERMEDIATE_HEIGHT, INTERMEDIATE_NUMBER_OF_MINES, true);
			}
			else if(advancedRadioButton.isSelected()) {
				startNewGame(ADVANCED_WIDTH, ADVANCED_HEIGHT, ADVANCED_NUMBER_OF_MINES, true);
			}
			optionsFrame.setVisible(false);
		}
		else if(e.getSource().equals(cancelButton)) {
			optionsFrame.setVisible(false);
		}
	}
	
	

}
