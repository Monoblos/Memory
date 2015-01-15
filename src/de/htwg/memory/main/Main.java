package de.htwg.memory.main;

import de.htwg.memory.ui.GUI;
import de.htwg.memory.ui.TUI;

public final class Main {
	private Main() { }

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase("-t")) {
			new TUI().startGameThreat();
		} else {
			new GUI(5, 4);
		}
	}
}
