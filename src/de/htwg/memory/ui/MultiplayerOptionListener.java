package de.htwg.memory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import de.htwg.memory.logic.Controller;

public class MultiplayerOptionListener implements ActionListener {
	final Controller c;
	
	public MultiplayerOptionListener(Controller c) {
		this.c = c;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem src = (JMenuItem)e.getSource();
		if(src.isSelected()) {
			c.setPlayerCount(1);
			src.setSelected(false);
		} else {
			String s = JOptionPane.showInputDialog(null, "Enter number of Players", "2");
			if (s == null) // Canceled
				return;
			c.setPlayerCount(Integer.parseInt(s));
			src.setSelected(true);
		}
	}
}