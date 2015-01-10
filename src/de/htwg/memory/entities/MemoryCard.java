package de.htwg.memory.entities;

import java.awt.Color;
import java.awt.Image;

import de.htwg.memory.logic.SettingUtil;
import de.htwg.memory.logic.Util;

public class MemoryCard implements Comparable<MemoryCard>, Cloneable{
	private int cardValue;
	private Image picture;
	private boolean visible;
	private boolean solved;
	private Board board;
	
	public MemoryCard() {
		this(0);
	}
	public MemoryCard(int cardValue) {
		this(cardValue, null);
	}
	public MemoryCard(int cardValue, Image picture) {
		this.cardValue = cardValue;
		this.picture = picture;
		this.visible = false;
		this.solved = false;
	}
	
	public void setCardValue(int value){
		this.cardValue = value;
	}
	public int getCardValue(){
		return this.cardValue;
	}
	public void setPicture(Image value) {
		this.picture = value;
	}
	public Image getPicture() {
		if (isVisible())
			return Util.createImageFromString(this.toString(), Color.WHITE, Color.BLUE);
		else
			return SettingUtil.getHiddenImage();
	}
	
	public boolean isVisible() {
		return visible;
	}
	public boolean setVisible(boolean visible) {
		if (visible && !this.visible && !this.solved) {
			this.visible = visible;
			return true;
		} else if (visible) {
			return false;
		}
		this.visible = visible;
		return true;
	}
	public boolean isSolved() {
		return solved;
	}
	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
	public void registerInBoard(Board b) {
		board = b;
	}
	public void pick() {
		if (board != null)
			board.pickCard(this);
		System.out.println("Board looks like this:\n" + board);
	}
	
	@Override
	public int compareTo(MemoryCard o) {
		return this.getCardValue() - o.getCardValue();
	}
	@Override
	public MemoryCard clone() {
		return new MemoryCard(cardValue, picture);
	}
	@Override
	public String toString() {
		if (visible || solved) {
			return " " + String.valueOf(cardValue) + " ";
		}
		return SettingUtil.getHiddenValue();
	}
}