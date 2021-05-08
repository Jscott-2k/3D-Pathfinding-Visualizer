package io.pathfinder.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import io.pathfinder.Driver;

public class HelpMenu extends JMenu{
	
	private JMenuItem about;
	
	public HelpMenu() {
		super("Help");
		about = new JMenuItem("about");
		JMenuItem userGuide = new JMenuItem("user guide");
		
		initActions();
		
		add(about);
		add(userGuide);
		

	}
	
	private void initActions() {
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null,
					    "CS 476 Final Project\nCreated by Justin Scott\n=====================\nArtwork from Westbeam on opengameart.org", "About", JOptionPane.INFORMATION_MESSAGE);
			}
			
		});
		
	}
}
