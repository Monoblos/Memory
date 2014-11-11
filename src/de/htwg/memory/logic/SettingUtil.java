package de.htwg.memory.logic;

public class SettingUtil {
	private SettingUtil() { }
	
	private static String HIDDEN_VALUE = "?";
	private static int NUMBER_OF_CARDS_TO_MATCH = 2;
	
	public static String getHiddenValue() {
		return HIDDEN_VALUE;
	}
	public static int getNumberOfCardsToMatch() {
		return NUMBER_OF_CARDS_TO_MATCH;
	}
}
