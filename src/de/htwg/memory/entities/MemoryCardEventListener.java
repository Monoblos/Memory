package de.htwg.memory.entities;


public interface MemoryCardEventListener {
	
	/**
	 * Is called when the MemoryCard should be picked
	 * @param mc MemoryCardInterface
	 */
	void picked(MemoryCardInterface mc);
}
