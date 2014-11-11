package de.htwg.memory.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TUITest {
	TUI tui;
	
	@Before
	public void setUp() {
		tui = new TUI();
	}

	@Test
	public void testStartStop() {
		assertFalse(tui.isRunning());
		tui.startGameThreat();
		assertTrue(tui.isRunning());
		tui.endGameThreat();
		assertFalse(tui.isRunning());
	}

}
