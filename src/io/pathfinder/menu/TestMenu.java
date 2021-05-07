package io.pathfinder.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import io.pathfinder.CubicGridSaveManager;
import io.pathfinder.Driver;

public class TestMenu extends JMenu {

	JMenuItem runSkip;
	JMenuItem runStep;
	JMenuItem runStepOnKey;

	public TestMenu() {
		super("Test");
		runSkip = new JMenuItem("run (skip steps)");
		runStep = new JMenuItem("run (step)");
		runStepOnKey = new JMenuItem("run (step on key)");

		initActions();

		add(runSkip);
		add(runStep);
		add(runStepOnKey);

	}

	private void initActions() {
		runSkip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CubicGridSaveManager.getCubicGridSaver().getCubicGrid().findPath(0);
			}
		});
		runStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CubicGridSaveManager.getCubicGridSaver().getCubicGrid().findPath(1);
			}
		});

	}
}
