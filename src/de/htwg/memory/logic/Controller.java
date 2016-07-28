package de.htwg.memory.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import de.htwg.memory.entities.Board;
import de.htwg.memory.entities.IMemoryCard;
import de.htwg.memory.entities.Board.PickResult;
import de.htwg.memory.entities.MemoryCard;
import de.htwg.memory.entities.MemoryCardEventListener;

public class Controller implements MemoryCardEventListener {
	private static Controller singelton;
	private static boolean underConstruction = false;
	
	private Board board;
	private int countRounds;
	private int players;
	private int matchPerPlayer[];
	private boolean waitingForHide;

	private LinkedList<UiEventListener> eventListeners;
	
	private Controller() {
		eventListeners = new LinkedList<>();
		board = null;
		countRounds = 0;
		players = 1;
		matchPerPlayer = new int[]{0};
		waitingForHide = false;
	}
	
	public static Controller getController() {
		if (underConstruction)
			throw new IllegalStateException("Controller reqested while in construction. Probably a loop in Controller init.");

		if (singelton == null) {
			underConstruction = true;
			singelton = new Controller();
			underConstruction = false;
		}
		return singelton;
	}
	
	public static Controller getNewController() {
		return new Controller();
	}
	
	public void loadBoard(File gameFile) {
		SaveFile sv = new SaveFile(gameFile);
		
		MemoryCard[] memCard = new MemoryCard[sv.getCards().size()];
		BufferedImage image = null;
		int i = 0;
		for(Entry<Integer, String> card : sv.getCards().entrySet()){
			try {
				image = ImageIO.read(new File(card.getValue()));
				memCard[i] = new MemoryCard(card.getKey(), image);
				memCard[i++].addListener(this);
			} catch (IOException e) {
				System.err.println("Bilder konnten nicht eingelesen werden! \"" + card.getValue() + "\"");
			}
		}
		
		replaceBoard(new Board(memCard, sv.getBoardSize().width, sv.getBoardSize().height, sv.getCardsToMatch(), this));
	}
	
	public void loadStaticBoard(int width, int height) {
		replaceBoard(new Board(null, width, height, 2, this));
	}
	
	private void replaceBoard(Board newBoard) {
		board = newBoard;

		fireBoardChanged();
		resetGame();
	}
	
	public void resetGame() {
		countRounds = 0;
		matchPerPlayer = new int[this.players];
		board.reset();
		board.shuffle();

		fireGameReset();
		fireBoardNeedsReload();
	}
	
	public void hideWrongMatch() {
		if (waitingForHide) {
			board.hideAll();
			fireBoardNeedsReload();
			waitingForHide = false;
		}
	}

	@Override
	public synchronized void picked(IMemoryCard mc) {
		if (!waitingForHide)
			processResult(board.pickCard(mc));
	}

	/**
	 * Tries to pick a card and returns if the pick was valid.
	 * @param x	X-Coordinate of the card
	 * @param y	Y-Coordinate of the card
	 * @return	False if the choices is invalid, true otherwise.
	 */
	public synchronized boolean pickCard(int x, int y) {
		if (waitingForHide || x >= board.getHeight() || y >= board.getWidth())
			return false;
		return processResult(board.pickCard(x, y));
	}
	
	private boolean processResult(PickResult result) {
		if (result == PickResult.INVALID_CARD) {
			return false;
		}

		if (result == PickResult.NO_MATCH_MADE
				|| result == PickResult.TOO_MANY_CARDS) {
			countRounds++;
			waitingForHide = true;
			fireNoMatchMade();
		} else if (result == PickResult.TOO_LESS_CARDS) {
			fireBoardNeedsReload();
		} else if (result == PickResult.MATCH_MADE) {
			matchPerPlayer[getCurrentPlayer() - 1]++;
			if (board.isFinished()) {
				fireWin();
			} else {
				fireMatchMade();
			}
		}
		
		return true;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public int getPlayerCount() {
		return players;
	}
	
	public int getCurrentPlayer() {
		if (getPlayerCount() > 0)
			return (getRoundNumber() % getPlayerCount()) + 1;
		return -1;
	}
	
	public void setPlayerCount(int players) {
		this.players = players;
		this.matchPerPlayer = new int[players];
		firePlayerCountChanged(this.players);
		resetGame();
	}
	
	/**
	 * Returns 0-based round number
	 * @return
	 */
	public int getRoundNumber() {
		return countRounds;
	}
	
	public int getPlayerMatches(int playerId) {
		if (playerId < players)
			return matchPerPlayer[playerId];
		return -1;
	}
	
	private void fireWin() {
		for (UiEventListener l : eventListeners) {
			l.win();
		}
	}
	
	private void fireMatchMade() {
		for (UiEventListener l : eventListeners) {
			l.matchMade();
		}
	}
	
	private void fireNoMatchMade() {
		for (UiEventListener l : eventListeners) {
			l.noMatchMade();
		}
	}
	
	private void fireBoardNeedsReload() {
		for (UiEventListener l : eventListeners) {
			l.boardNeedsRealod();
		}
	}
	
	private void fireBoardChanged() {
		for (UiEventListener l : eventListeners) {
			l.boardChanged();
		}
	}
	
	private void firePlayerCountChanged(int players) {
		for (UiEventListener l : eventListeners) {
			l.playerCountChanged(players);
		}
	}
	
	private void fireGameReset() {
		for (UiEventListener l : eventListeners) {
			l.gameReset();
		}
	}
	
	public void addListener(UiEventListener listener) {
		eventListeners.add(listener);
	}
	public void removeListener(UiEventListener listener) {
		eventListeners.remove(listener);
	}
}
