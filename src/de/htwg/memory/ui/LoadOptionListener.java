package de.htwg.memory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import de.htwg.memory.logic.Controller;

public class LoadOptionListener implements ActionListener {
	final Controller c;
	
	public LoadOptionListener(Controller c) {
		this.c = c;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser("SavedGames");
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Game-Files (game.dat)";
			}
			
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith("game.dat");
			}
		});
		if (fileChooser.showOpenDialog((JComponent)e.getSource()) == JFileChooser.APPROVE_OPTION) {
			c.loadBoard(fileChooser.getSelectedFile());
		}
	}

}
