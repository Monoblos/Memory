package de.htwg.memory.logic;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.htwg.memory.entities.Board;
import de.htwg.memory.entities.MemoryCard;

public final class Util {
	MemoryCard[] memCard;
	int width, height = 0;
	Board board;
	
	private Util() { }

	public static boolean exits() {
		return true;
	}
	
	public Board readCards(File[] img, ActionListener a){
		memCard = null;
		board = null;
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
		return board = new Board(memCard, (int)(Math.round(Math.sqrt(img.length))), (int)(Math.round(Math.sqrt(img.length))));
		
	}
}
