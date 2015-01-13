package de.htwg.memory.main;

import de.htwg.memory.ui.GUI;
import de.htwg.memory.ui.TUI;

public final class Main {
	private Main() { }

	public static void main(String[] args) {
		new GUI(5, 4);
//		new TUI().startGameThreat();
	}
}
