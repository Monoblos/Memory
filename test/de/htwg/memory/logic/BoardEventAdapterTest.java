package de.htwg.memory.logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BoardEventAdapterTest {
	BoardEventAdapter bea;

	@Before
	public void setUp() throws Exception {
		bea = new BoardEventAdapter();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		bea.afterBoardReset();
		bea.beforeBoardReset();
		bea.matchMade();
		bea.win();
	}

}
