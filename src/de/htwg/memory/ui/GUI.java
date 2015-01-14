package de.htwg.memory.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import de.htwg.memory.entities.Board;
import de.htwg.memory.entities.MemoryCard;
import de.htwg.memory.logic.BoardEventListener;

public class GUI extends JFrame implements BoardEventListener, ActionListener {
	private static final long serialVersionUID = -9143205935002378362L;
	
	private static final long WAIT_TIME_AFTER_WRONG_MATCH = 500;

	private Board board;
	private int countRounds;
	private int players;
	private int matchPerPlayer[];
	
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
		this.players = 1;
		
		resetGame();

		addMenuBar();
		
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private final void realoadButtons() {
		JPanel panel = new JPanel(new GridLayout(board.getHeight(), board.getWidth(), 5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setContentPane(panel);
		for(MemoryCard mc : board.getMemoryCardIterator()) {
			this.add(new MemCardButton(mc));
		}
		this.getContentPane().invalidate();
	}
	
	private final void setWindowSize() {
		this.setSize(400, 400);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidthMultiplikator = (screenSize.getWidth() - 25) / board.getWidth();
		double screenHeightMultiplikator = (screenSize.getHeight() - 85) / board.getHeight();
		double multiplikatorToUse = screenWidthMultiplikator < screenHeightMultiplikator ? screenWidthMultiplikator : screenHeightMultiplikator;

		this.setSize((int)Math.round(board.getWidth() * multiplikatorToUse)
				    ,(int)Math.round(board.getHeight() * multiplikatorToUse));
	}
	
	private final void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menü");
		menuBar.add(menu);
		JMenuItem load = new JMenuItem("Load");
		menu.add(load);
		load.addActionListener(this);
		JMenuItem multiplayer = new JMenuItem("Use multiplayer");
		menu.add(multiplayer);
		multiplayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(((JMenuItem)e.getSource()).isSelected()) {
					players = 1;
					((JMenuItem)e.getSource()).setSelected(false);
				} else {
					String s = JOptionPane.showInputDialog(null, "Enter number of Players", "2");
					players = Integer.parseInt(s);
					((JMenuItem)e.getSource()).setSelected(true);
				}
				resetGame();
			}
		});
		JMenuItem restart = new JMenuItem("Restart");
		menu.add(restart);
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetGame();
			}
		});
		JMenuItem exit = new JMenuItem("Exit");
		menu.add(exit);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.setJMenuBar(menuBar);
	}


	@Override
	public void win() {
		StringBuilder text = new StringBuilder();
		text.append("Vitory!");
		if (players == 1) {
			text.append("\nNumber of turns: ").append(countRounds);
		} else {
			for (int i = 0; i < players; i++) {
				text.append("\nMatches Player ").append(i + 1).append(": ").append(matchPerPlayer[i]);
			}
		}
		
		text.append("\nDo you want to restart?");
		
		if (JOptionPane.showConfirmDialog(this,
				text.toString(),
				"Victory!",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			resetGame();
		}
	}
	@Override
	public void matchMade() {
		matchPerPlayer[countRounds % players]++;
		this.invalidate();
		this.repaint();
	}
	@Override
	public void beforeBoardReset() {
		this.invalidate();
		this.repaint();
		countRounds++;
		try {
			Thread.sleep(WAIT_TIME_AFTER_WRONG_MATCH);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (players > 1)
			JOptionPane.showConfirmDialog(this, "Player " + (countRounds % players + 1), "Next Player", JOptionPane.OK_CANCEL_OPTION);
	}
	@Override
	public void afterBoardReset() {
		this.invalidate();
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
	
	private final void resetGame() {
		this.countRounds = 0;
		this.matchPerPlayer = new int[this.players];
		this.board.removeListener(this);
		this.board.reset();
		this.board.shuffle();
		realoadButtons();
		this.board.addListener(this);
		this.invalidate();
		this.repaint();
		setWindowSize();
	}
	
	private void load(File f) {
		this.board.removeListener(this);
		this.board = Board.load(f);
		resetGame();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser("SavedGames");
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Game-Files (game.dat)";
			}
			
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith("game.dat");
			}
		});
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			load(fileChooser.getSelectedFile());
		}
	}
}
