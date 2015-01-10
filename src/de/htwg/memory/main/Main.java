package de.htwg.memory.main;

import de.htwg.memory.ui.GUI;

public final class Main {
	private Main() { }

	public static void main(String[] args) {
		new GUI(10, 5);
//		new TUI().startGameThreat();
	}
}
