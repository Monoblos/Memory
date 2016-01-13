package de.htwg.memory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import de.htwg.memory.logic.Controller;

public class MultiplayerOptionListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem src = (JMenuItem)e.getSource();
		if(src.isSelected()) {
			Controller.getController().setPlayerCount(1);
			src.setSelected(false);
		} else {
			String s = JOptionPane.showInputDialog(null, "Enter number of Players", "2");
			if (s == null) // Canceled
				return;
			Controller.getController().setPlayerCount(Integer.parseInt(s));
			src.setSelected(true);
		}
	}
}