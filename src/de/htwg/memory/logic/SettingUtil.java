package de.htwg.memory.logic;

public final class SettingUtil {
	private SettingUtil() { }
	
	private static String hiddenValue = "?";
	private static int numberOfCardsToMatch = 2;
	
	public static String getHiddenValue() {
		return hiddenValue;
	}
	public static int getNumberOfCardsToMatch() {
		return numberOfCardsToMatch;
	}
}
