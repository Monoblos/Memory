package de.htwg.memory.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.htwg.memory.entities.Board;
import de.htwg.memory.entities.MemoryCard;
import de.htwg.memory.logic.BoardEventListener;

public class GUI extends JFrame implements BoardEventListener {
	private static final long serialVersionUID = -9143205935002378362L;

	private Board board;
	private int countRounds;
	
	
	public GUI() {
		this(null);
	}
	public GUI(MemoryCard[] cards) {
		this(cards, 4, 4);
	}
	public GUI(int width, int height) {
		this(null, width, height);
	}
	public GUI(MemoryCard[] memoryCards, int width, int height) {
		if (memoryCards != null)
			this.board = new Board(memoryCards.clone(), width, height);
		else
			this.board = new Board(null, width, height);
		this.board.addListener(this);
		this.countRounds = 0;
		
		board.shuffle();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidthMultiplikator = (screenSize.getWidth() - 25) / width;
		double screenHeightMultiplikator = (screenSize.getHeight() - 85) / height;
		double multiplikatorToUse = screenWidthMultiplikator < screenHeightMultiplikator ? screenWidthMultiplikator : screenHeightMultiplikator;

		this.setSize((int)Math.round(width * multiplikatorToUse)
				    ,(int)Math.round(height * multiplikatorToUse));

		JPanel panel = new JPanel(new GridLayout(height, width, 5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setContentPane(panel);
		for(MemoryCard mc : board.getMemoryCardIterator()) {
			if(mc != null)
				mc.registerInBoard(board);
			this.add(new MemCardButton(mc));
		}
		
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}


	@Override
	public void win() {
		
	}
	@Override
	public void matchMade() {
		this.invalidate();
	}
	@Override
	public void beforeBoardReset() {
		this.invalidate();
		this.repaint();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void afterBoardReset() {
		this.invalidate();
		this.repaint();
	}
}
