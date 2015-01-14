package de.htwg.memory.entities;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.htwg.memory.logic.BoardEventAdapter;
import de.htwg.memory.logic.BoardEventListener;
import de.htwg.memory.logic.SettingUtil;

public class BoardTest {
	Board filledBoard;
	Board emptyBoard;
	
	@Before
	public void setUp() {
		filledBoard = new Board(null, 4, 4);
		emptyBoard = new Board(new MemoryCard[0], 4, 4);
	}
	
	@Test
	public void testShuffle() {
		filledBoard.shuffle();
	}
	
	@Test
	public void testHeigth() {
		assertEquals(4, filledBoard.getHeight());
	}
	
	@Test
	public void testWidth() {
		assertEquals(4, filledBoard.getWidth());
	}
	
	@Test
	public void testGetCardCount() {
		assertEquals(filledBoard.getHeight() * filledBoard.getWidth(), filledBoard.getCardCount());
	}
	
	@Test
	public void testInteracting() {
		Random r = new Random();
		BoardEventListener l = new BoardEventAdapter();
		filledBoard.addListener(l);
		for (int i = 0; !filledBoard.isFinished(); i++) {
			if (i % SettingUtil.getNumberOfCardsToMatch() != 0) {
				assertTrue(filledBoard.hasVisibleCard());
			}
			while(!filledBoard.pickCard(r.nextInt(4), r.nextInt(4)));
		}
		filledBoard.removeListener(l);
	}
	
	@Test
	public void testToString() {
		String s = filledBoard.toString();
		filledBoard.pickCard(1, 1);
		String c = filledBoard.toString();
		assertFalse(s.equals(c));
	}
	
	@Test
	public void addCardPair() {
		MemoryCard mc = new MemoryCard(17);
		assertEquals(0, emptyBoard.getCardCount());
		emptyBoard.addCardPair(mc);
		assertEquals(SettingUtil.getNumberOfCardsToMatch(), emptyBoard.getCardCount());
		try {
			emptyBoard.addCardPair(null);
			fail("Adding null card worked");
		} catch (NullPointerException e) { }
		try {
			filledBoard.addCardPair(mc);
			fail("Adding card to full board worked");
		} catch (IndexOutOfBoundsException e) { }
	}
	
	@Test
	public void testReset() {
		
	}
	
	@After
	public void breakDown() throws Exception {
		
	}
}
