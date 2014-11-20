package de.htwg.memory.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.htwg.memory.entities.Board;
import de.htwg.memory.entities.MemoryCard;
import de.htwg.memory.logic.BoardEventListener;

public class TUI implements Runnable, BoardEventListener, KeyListener {
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
		this.selectedRow = -1;
		this.cardToPick = 1;
		this.countRounds = 0;
	}
	
	public void startGameThreat() {
		game = new Thread(this);
		continueRunning = true;
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
		
		board.shuffle();
		
		virtualConsole.println(board);
		virtualConsole.println("Select card " + cardToPick + ":");
		
		while (continueRunning) {
			char lastKey = ' ';
			synchronized (holder) {
				while (holder.isEmpty()) {
					try {
						holder.wait();
					} catch (InterruptedException e) {
						Logger.getLogger("").log(Level.ALL, "Interupt exception happend");
					}
				}
				
				lastKey = holder.remove(0);
			}
			int key;
			try {
				key = Integer.valueOf(String.valueOf(lastKey)) - 1;
			} catch (NumberFormatException e) {
				virtualConsole.println("Invalid input: '" + lastKey + "'");
				continue;
			}
			
			if (handleKey(key)) {
				virtualConsole.clear();
				virtualConsole.println(board);
				virtualConsole.println("Select card " + cardToPick + ":");
			}
		}
		
		virtualConsole.kill();
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
	
	@Override
	public void win() {
		virtualConsole.println("Congratulation, you won!");
		virtualConsole.println("Rounds played: " + this.countRounds);
		virtualConsole.waitForKey();
		endGameThreat();
	}
	@Override
	public void matchMade() {
		cardToPick = 0;
		countRounds++;
		virtualConsole.clear();
		virtualConsole.println(board);
		virtualConsole.println();
		virtualConsole.println("Correct match!");
		virtualConsole.waitForKey();
	}
	@Override
	public void beforeBoardReset() {
		cardToPick = 0;
		countRounds++;
		virtualConsole.clear();
		virtualConsole.println(board);
		virtualConsole.print("No match! Press any key to hide and continue... ");
		virtualConsole.waitForKey();
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
}
