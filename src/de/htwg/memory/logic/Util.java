package de.htwg.memory.logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.htwg.memory.entities.Board;
import de.htwg.memory.entities.MemoryCard;

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
	
	public static Board readCards(File[] img, ActionListener a){
		MemoryCard[] memCard = new MemoryCard[img.length];
		BufferedImage image = null;
		if(img.length <= 1){
			System.out.println("Es wurden zu wenig Karten ausgewählt");
		}
		for(int i = 1; i <= img.length; i++){
				try {
					image = ImageIO.read(img[i]);
					memCard[i] = new MemoryCard(i, image);
				} catch (IOException e) {
					System.out.println("Bilder konnten nicht eingelesen werden!");
				}
			}
		return new Board(memCard, (int)(Math.round(Math.sqrt(img.length))), (int)(Math.round(Math.sqrt(img.length))));
		
	}
}
