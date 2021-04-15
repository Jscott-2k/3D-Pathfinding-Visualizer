package io.pathfinder.menu;

import javax.swing.JMenuBar;

public class CustomMenuBar extends JMenuBar{
	
	private EditMenu editMenu;
	
	public CustomMenuBar() {
		super();
		editMenu = new EditMenu();
		add(new FileMenu());
		add(editMenu);
		add(new TestMenu());
		add(new HelpMenu());
	}
	
	public EditMenu getEditMenu() {
		return editMenu;
	}
}
