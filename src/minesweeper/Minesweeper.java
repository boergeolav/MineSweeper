package minesweeper;

import java.util.Scanner;

import javax.swing.*;

import minesweeper.MinesweeperBoard.GameStatus;

public class Minesweeper {
	
	public static void main(String[] args) {
		MinesweeperBoard board;
		Object[] options = {"Console version", "GUI version"};
		int n = JOptionPane.showOptionDialog(null,
				"Do you want to play the console input version or the grapical user interface version?",
				"Version?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				null);
		if(n == JOptionPane.YES_OPTION) {
			Scanner in = new Scanner(System.in);
			boolean stillPlaying = true;
			while(stillPlaying) {
				System.out.println("Enter width, height and number of mines for your board!");
				System.out.print("Enter width: ");
				int width = in.nextInt();
				System.out.print("Enter height: ");
				int height = in.nextInt();
				int numberOfMines = width * height;
				while(numberOfMines >= width * height) {
					System.out.print("Enter number of mines, less than or equal to " + (width * height - 1) + ": ");
					numberOfMines = in.nextInt();
				}
				System.out.println("");
				board = new ConsoleBoard(width, height, numberOfMines);
				((ConsoleBoard)board).printRepresentationBoard();
				int x = 0, y = 0;
				boolean flag = false;
				boolean gameLostOrWon = false;
				while(!gameLostOrWon) {
					System.out.println("Enter row:");
					x = in.nextInt();
					System.out.println("Enter column:");
					y = in.nextInt();
					char c = ' ';
					while(c != 'y' && c != 'n') {
						System.out.println("Set flag? (y/n)");
						c = in.next().charAt(0);
					}
					flag = c == 'y';
					GameStatus status = board.setXY(x, y, flag);
					((ConsoleBoard)board).printRepresentationBoard();
					switch(status) {
					case GAME_WON: 
						System.out.println("You won!");
						gameLostOrWon = true;
						break;
					case GAME_LOST:
						System.out.println("You lost...");
						gameLostOrWon = true;
						break;
					case GAME_CONTINUES:
						System.out.println("The game goes on.");
						continue;
					default:
						break;
					}
				}
				char c = ' ';
				while(c != 'y' && c != 'n') {
					System.out.println("Play again? (y/n)");
					c = in.next().charAt(0);
				}
				stillPlaying = c == 'y';
			}
			in.close();
		}
		else if (n == JOptionPane.NO_OPTION) {
			board = new GuiBoard(10, 10, 10);
		}
	}
}
