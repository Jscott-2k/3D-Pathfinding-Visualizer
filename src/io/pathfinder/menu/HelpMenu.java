package io.pathfinder.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class HelpMenu extends JMenu{
	public HelpMenu() {
		super("Help");
		JMenuItem about = new JMenuItem("about");
		JMenuItem userGuide = new JMenuItem("user guide");
		add(about);
		add(userGuide);
	}
}
