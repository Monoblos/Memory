package de.htwg.memory.entities;

import java.awt.Image;

import de.htwg.memory.logic.SettingUtil;

public class MemoryCard implements Comparable<MemoryCard>, Cloneable{
	private int cardValue;
	private Image picture;
	private boolean visible;
	private boolean solved;
	
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
		return this.picture;
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
		return "(" + String.valueOf(cardValue) + ")";
		//return SettingUtil.getHiddenValue();
	}
}
