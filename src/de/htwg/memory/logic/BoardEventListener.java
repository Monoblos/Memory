package de.htwg.memory.logic;

public interface BoardEventListener {
	void win();
	void matchMade();
	void beforeBoardReset();
	void afterBoardReset();
}
