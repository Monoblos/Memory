package de.htwg.memory.entities;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import de.htwg.memory.logic.BoardEventListener;
import de.htwg.memory.logic.SettingUtil;

public class Board implements MemoryCardEventListener{
	private MemoryCard[][] memoryCards;
	private int cardCount;
	private List<BoardEventListener> eventListeners;
	
	public Board(MemoryCard[] memoryCards, int width, int height) {
		MemoryCard[] cards = memoryCards;
		this.memoryCards = new MemoryCard[width][height];
		cardCount = 0;
		
		if(cards == null) {
			cards = new MemoryCard[width * height / SettingUtil.getNumberOfCardsToMatch()];
			for (int i = 0; i < width * height / SettingUtil.getNumberOfCardsToMatch(); i++) {
				cards[i] = new MemoryCard(i + 1);
			}
		}
		
		for (int i = 0; i < width * height / SettingUtil.getNumberOfCardsToMatch() && i < cards.length; i++) {
			addCardPair(cards[i]);
		}
		eventListeners = new LinkedList<>();
	}
	
	public void addListener(BoardEventListener listener) {
		eventListeners.add(listener);
	}
	public void removeListener(BoardEventListener listener) {
		eventListeners.remove(listener);
	}
	
	public int getCardCount() {
		return cardCount;
	}
	public int getWidth() {
		return memoryCards[0].length;
	}
	public int getHeight() {
		return memoryCards.length;
	}
	
	public final void addCardPair(MemoryCard card) {
		if (card == null) {
			throw new NullPointerException("Can't add null Memory Cards");
		}
		if (cardCount + SettingUtil.getNumberOfCardsToMatch() > getWidth() * getHeight()) {
			throw new IndexOutOfBoundsException("Board has reached maximum number of cards");
		}
		for (int i = 0; i < SettingUtil.getNumberOfCardsToMatch(); i++) {
			this.memoryCards[cardCount / getWidth()][cardCount % getWidth()] = card.clone();
			this.memoryCards[cardCount / getWidth()][cardCount % getWidth()].addListener(this);
			cardCount++;
		}
	}
	
	public boolean pickCard(int x, int y) {
		return pickCard(memoryCards[x][y]);
	}
	public boolean pickCard(MemoryCardInterface c) {
		if (!c.setVisible(true)) {
			return false;
		}
		int matchMade = updateMatchings();
		if (matchMade == 1) {
			for (BoardEventListener l : eventListeners) {
				l.matchMade();
			}
			if (isFinished()) {
				for (BoardEventListener l : eventListeners) {
					l.win();
				}
			}
		} else if (matchMade == -2) {
			c.setVisible(false);
		}
		return true;
	}
	public boolean hasVisibleCard() {
		boolean hasVisible = false;
		for (int i = 0; i < memoryCards.length; i++) {
			for (int j = 0; j < memoryCards[i].length; j++) {
				hasVisible |= memoryCards[i][j].isVisible() && !memoryCards[i][j].isSolved();
			}
		}
		return hasVisible;
	}
	public boolean isFinished() {
		boolean isFinished = true;
		for (int i = 0; i < memoryCards.length && isFinished; i++) {
			for (int j = 0; j < memoryCards[i].length && isFinished; j++) {
				isFinished &= memoryCards[i][j].isSolved();
			}
		}
		return isFinished;
	}
	/**
	 * 
	 * @return 0 if not enougth cards for a match, 1 if a match was made, -1 if match is invalid.
	 */
	private int updateMatchings() {
		MemoryCard foundCards[] = getChoosenCards();
		
		if (foundCards.length < SettingUtil.getNumberOfCardsToMatch()) {
			return 0;
		} else if (foundCards.length > SettingUtil.getNumberOfCardsToMatch()) {
			return -2;
		}
		
		boolean allMatch = true;
		for (int i = 1; i < foundCards.length; i++) {
			allMatch &= foundCards[0].compareTo(foundCards[i]) == 0;
		}
		if (allMatch) {
			for (int i = 0; i < foundCards.length; i++) {
				foundCards[i].setSolved(true);
			}
			return 1;
		}
		
		hideAll();
		return -1;
	}
	
	private MemoryCard[] getChoosenCards() {
		MemoryCard foundCards[] = new MemoryCard[getWidth() * getHeight()];
		int cardsFound = 0;
		
		for (int i = 0; i < memoryCards.length; i++) {
			for (int j = 0; j < memoryCards[i].length; j++) {
				if (memoryCards[i][j].isVisible() && !memoryCards[i][j].isSolved()) {
					foundCards[cardsFound++] = memoryCards[i][j];
				}
			}
		}
		
		MemoryCard result[] = new MemoryCard[cardsFound];
		for (int i = 0; i < cardsFound; i++)
			result[i] = foundCards[i];
		return result;
	}
	
	public void shuffle() {
		shuffle(200);
	}
	public void shuffle(int shuffleCount) {
		Random r = new Random();
		for(int i = 0; i < shuffleCount; i++) {
			int x1 = r.nextInt(memoryCards[0].length);
			int y1 = r.nextInt(memoryCards.length);
			int x2 = r.nextInt(memoryCards[0].length);
			int y2 = r.nextInt(memoryCards.length);

			MemoryCard temp = memoryCards[y1][x1];
			memoryCards[y1][x1] = memoryCards[y2][x2];
			memoryCards[y2][x2] = temp;
		}
	}
	
	public void hideAll() {
		for (BoardEventListener l : eventListeners) {
			l.beforeBoardReset();
		}
		for (int i = 0; i < memoryCards.length; i++) {
			for (int j = 0; j < memoryCards[i].length; j++) {
				memoryCards[i][j].setVisible(false);
			}
		}
		for (BoardEventListener l : eventListeners) {
			l.afterBoardReset();
		}
	}
	
	public void reset() {
		for (BoardEventListener l : eventListeners) {
			l.beforeBoardReset();
		}
		for (int i = 0; i < memoryCards.length; i++) {
			for (int j = 0; j < memoryCards[i].length; j++) {
				memoryCards[i][j].setVisible(false);
				memoryCards[i][j].setSolved(false);
			}
		}
		for (BoardEventListener l : eventListeners) {
			l.afterBoardReset();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				sb.append(memoryCards[i][j]).append(" ");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public Iterable<MemoryCard> getMemoryCardIterator() {
		ArrayList<MemoryCard> forIt = new ArrayList<>();
		for(int i = 0; i < memoryCards.length; i++)
			forIt.addAll(Arrays.asList(memoryCards[i]));
		return forIt;
	}

	public static Board fromImages(File[] img, ActionListener a){
		MemoryCard[] memCard = new MemoryCard[img.length];
		BufferedImage image = null;
		if(img.length <= 1){
			System.out.println("Es wurden zu wenig Karten ausgewählt");
		}
		for(int i = 1; i <= img.length; i++){
				try {
					image = ImageIO.read(img[i]);
					memCard[i] = new MemoryCard(i, image);
				} catch (IOException e) {
					System.out.println("Bilder konnten nicht eingelesen werden!");
				}
			}
		return new Board(memCard, (int)(Math.round(Math.sqrt(img.length))), (int)(Math.round(Math.sqrt(img.length))));
	}

	@Override
	public void picked(MemoryCardInterface mc) {
		pickCard(mc);
	}
}
