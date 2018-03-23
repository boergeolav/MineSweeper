package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class Square extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	public final static int SQUARE_WIDTH = 30;
	public final static int SQUARE_HEIGHT = 30;
	
	private GuiBoard gui;
	private int row, column;
	private final static Dimension size = new Dimension(SQUARE_WIDTH, SQUARE_HEIGHT);
	private char mark;
	
	public Square(GuiBoard gui, int row, int column) {
		this.gui = gui;
		this.row = row;
		this.column = column;
		this.mark = MinesweeperBoard.EMPTY_SQUARE;
		setBackground(Color.WHITE);
		addMouseListener(this);
	}
	
	public void setMark(char mark) {
		this.mark = mark;
		switch(mark) {
		case MinesweeperBoard.CHECKED_SQUARE:
			setBackground(Color.BLACK);
			break;
		case MinesweeperBoard.MINE_SQUARE:
			setBackground(Color.RED);
			break;
		case MinesweeperBoard.MINE_FLAG_SQUARE:
			setBackground(Color.YELLOW);
			break;
		default:
			setBackground(Color.WHITE);
			break;
		}
	}
	
	public char getMark() {
		return mark;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return size;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(0, 0, SQUARE_WIDTH, SQUARE_HEIGHT);
		g.drawString(""+mark, 11, 20);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(isEnabled())
			gui.squareClicked(row, column, e.getButton());
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

}
