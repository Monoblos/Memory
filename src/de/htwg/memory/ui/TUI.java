package de.htwg.memory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

import de.htwg.memory.entities.Board;
import de.htwg.memory.entities.MemoryCard;
import de.htwg.memory.logic.BoardEventListener;

public class TUI implements Runnable, BoardEventListener, KeyListener, ActionListener {
	private VirtualConsole virtualConsole;
	private Board board;
	private Thread game;
	private Boolean continueRunning;
	private int selectedRow;
	private int cardToPick;
	private final List<Character> holder = new LinkedList<>();
	private int countRounds;
	
	public TUI() {
		this(null);
	}
	public TUI(MemoryCard[] memoryCards) {
		this(memoryCards, 4, 4);
	}
	public TUI(int width, int height) {
		this(null, width, height);
	}
	public TUI(MemoryCard[] memoryCards, int width, int height) {
		this.virtualConsole = null;
		if (memoryCards != null)
			this.board = new Board(memoryCards.clone(), width, height);
		else
			this.board = new Board(null, width, height);
		this.board.addListener(this);
		this.game = null;
		this.continueRunning = false;
	}
	
	public void startGameThreat() {
		game = new Thread(this);
		continueRunning = true;
		this.selectedRow = -1;
		this.countRounds = 0;
		this.cardToPick = 1;
		holder.clear();
		game.start();
	}
	
	public boolean isRunning() {
		return continueRunning;
	}
	
	public void endGameThreat() {
		continueRunning = false;
	}
	
	@Override
	public void run() {
		virtualConsole = new VirtualConsole();
		virtualConsole.addKeyListener(this);
		virtualConsole.addMenueItem(this, "Reset");
		virtualConsole.addMenueItem(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		}, "Exit");
		
		board.shuffle();
		
		virtualConsole.println(board);
		virtualConsole.println("Select card " + cardToPick + ":");
		
		gameloop:
		while (continueRunning) {
			char lastKey = ' ';
			synchronized (holder) {
				while (holder.isEmpty()) {
					try {
						holder.wait();
					} catch (InterruptedException e) {
						break gameloop;
					}
				}
				
				lastKey = holder.remove(0);
			}
			int key;
			try {
				key = Integer.valueOf(String.valueOf(lastKey)) - 1;
			} catch (NumberFormatException e) {
				if (lastKey == KeyEvent.VK_BACK_SPACE) {
					if(roleBack()) {
						continue;
					}
				}
				virtualConsole.println("Invalid input: '" + lastKey + "'");
				continue;
			}
			
			if (handleKey(key) && continueRunning) {
				virtualConsole.clear();
				virtualConsole.println(board);
				virtualConsole.println("Select card " + cardToPick + ":");
			}
		}
		
		virtualConsole.kill();
		return;
	}
	
	private boolean handleKey(int key) {
		if (key >= 0 && key < board.getWidth()) {
			virtualConsole.print(key + 1);
			if (selectedRow == -1) {
				selectedRow = key;
				return false;
			}
			
			if (!board.pickCard(selectedRow, key)) {
				virtualConsole.println("Card already picked: '" + String.valueOf(selectedRow + 1) + (key + 1) + "'");
				selectedRow = -1;
				return false;
			} else {
				cardToPick++;
			}
			selectedRow = -1;
		} else {
			virtualConsole.println("Index out of range: '" + (key + 1) + "'");
			return false;
		}
		return true;
	}
	
	private boolean roleBack() {
		if (selectedRow == -1) {
			return false;
		}
		selectedRow = -1;
		return true;
	}
	
	@Override
	public void win() {
		virtualConsole.println("Congratulation, you won!");
		virtualConsole.println("Rounds played: " + this.countRounds);
		try {
			virtualConsole.waitForKey();
		} catch (InterruptedException e) {
			return;
		}
		endGameThreat();
	}
	@Override
	public void matchMade() {
		cardToPick = 0;
		virtualConsole.clear();
		virtualConsole.println(board);
		virtualConsole.println();
		virtualConsole.println("Correct match!");
		try {
			virtualConsole.waitForKey();
		} catch (InterruptedException e) {
			return;
		}
	}
	@Override
	public void beforeBoardReset() {
		cardToPick = 0;
		countRounds++;
		virtualConsole.clear();
		virtualConsole.println(board);
		virtualConsole.print("No match! Press any key to hide and continue... ");
		try {
			virtualConsole.waitForKey();
		} catch (InterruptedException e) {
			return;
		}
	}
	@Override
	public void afterBoardReset() {

	}
	
	@Override
	public void keyPressed(KeyEvent e) {

	}
	@Override
	public void keyReleased(KeyEvent e) {

	}
	@Override
	public void keyTyped(KeyEvent e) {
		synchronized (holder) {
			holder.add(e.getKeyChar());
			holder.notify();
		}
		e.consume();
	}
	public void actionPerformed(ActionEvent a){
		this.continueRunning = false;
		game.interrupt();
		try {
			game.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.board.removeListener(this);
		this.board.reset();
		this.board.addListener(this);
		
		startGameThreat();
	}
}
