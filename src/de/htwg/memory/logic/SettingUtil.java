package de.htwg.memory.logic;

import java.awt.Image;

public final class SettingUtil {
	private SettingUtil() { }
	
	private static String hiddenValue = " X ";
	
	public static String getHiddenValue() {
		return hiddenValue;
	}
	public static Image getHiddenImage() {
		return Util.createImageFromString(getHiddenValue());
	}
}
