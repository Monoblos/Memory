package de.htwg.memory.logic;

public interface BoardEventListener {
	public void win();
	public void matchMade();
	public void beforeBoardReset();
	public void afterBoardReset();
}
