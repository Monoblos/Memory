package de.htwg.memory.logic;

public interface BoardEventListener {
	/**
	 * Listener handling BoardEvents
	 * @author Simon Müller und Sandra Berger
	 * @since 13.01.2015
	 */
	
	/**
	 * Is called when game is won
	 */
	void win();
	
	/**
	 * Is called when player made a card match
	 */
	void matchMade();
	
	/**
	 * Is called before resetting the Board 
	 */
	void beforeBoardReset();
	
	/**
	 * Is called after resetting the Board
	 */
	void afterBoardReset();
}
