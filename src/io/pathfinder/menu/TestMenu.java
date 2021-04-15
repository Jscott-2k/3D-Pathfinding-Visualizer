package io.pathfinder.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import io.pathfinder.CubicGridSaveManager;
import io.pathfinder.Driver;

public class TestMenu extends JMenu{
	
	JMenuItem runDefault;
	
	public TestMenu() {
		super("Test");
		runDefault = new JMenuItem("run (skip steps)");
		JMenuItem runStep = new JMenuItem("run (step)");
		JMenuItem runStepOnKey = new JMenuItem("run (step on key)");
		
		initActions();
		
		add(runDefault);
		add(runStep);
		add(runStepOnKey);
		
		
	}
	
	
	private void initActions() {
		runDefault.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CubicGridSaveManager.getCubicGridSaver().getCubicGrid().findPath();

			}
			
		});
		
	}
}
