package io.pathfinder.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class EditMenu extends JMenu{
	
	private JCheckBoxMenuItem edit;
	
	public EditMenu() {
		super("Edit");
		edit = new JCheckBoxMenuItem("edit mode");
		JMenuItem size = new JMenuItem("size");
		JMenuItem backgroundColor = new JMenuItem("background color");
		JMenuItem cubeColor = new JMenuItem("cube color");
		edit.setSelected(false);
		
		initActions();
		
		add(edit);
		add(size);
		addSeparator();
		add(backgroundColor);
		add(cubeColor);
	}
	
	private void initActions() {
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("Switch Edit...");				
			}
		});
		edit.setMnemonic(KeyEvent.VK_E);
		edit.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_E, ActionEvent.CTRL_MASK));
	}
	
	public JCheckBoxMenuItem getEdit() {
		return edit;
	}
}
