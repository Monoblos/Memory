package de.htwg.memory.logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public final class Util {
	private Util() { }

	public static Image createImageFromString(String s) {
		return createImageFromString(s, Color.BLACK, Color.WHITE);
	}

	public static Image createImageFromString(String s, Color bg, Color fc) {
		BufferedImage i = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics g = i.createGraphics();
		g.setColor(bg);
		g.fillRect(0, 0, 100, 100);
		g.setColor(fc);
		g.setFont(g.getFont().deriveFont(50f));
		g.drawChars(s.toCharArray(), 0, s.length(), 25, 75);
		return i;
	}
}
