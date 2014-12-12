package de.htwg.memory.main;

import de.htwg.memory.ui.TUI;

public final class Main {
	private Main() { }

	public static void main(String[] args) {
		new TUI().startGameThreat();
	}
}
