package io.pathfinder.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.pathfinder.CubicGridSaveManager;

public class FileMenu extends JMenu {

	private JMenuItem newLayout;
	private JMenuItem openLayout;
	private JMenuItem saveLayout;

	private String workingDir;

	public FileMenu() {
		super("File");

		this.workingDir = System.getProperty("user.dir") + "/saves";
		newLayout = new JMenuItem("new");
		openLayout = new JMenuItem("open");
		saveLayout = new JMenuItem("save");

		initActions();

		add(newLayout);
		add(openLayout);
		add(saveLayout);

	}

	private void initActions() {
		saveLayout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("SAVING...");
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(new FileNameExtensionFilter(".p3dv", "p3dv"));

					fileChooser.setCurrentDirectory(new File(workingDir));
					int selection = fileChooser.showSaveDialog(null);
					if (selection == JFileChooser.APPROVE_OPTION) {
						CubicGridSaveManager.getCubicGridSaver().save(fileChooser.getSelectedFile() + ".p3dv");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		saveLayout.setMnemonic(KeyEvent.VK_S);
		saveLayout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

		openLayout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("OPENING...");
				JFileChooser fileChooser = new JFileChooser();
				try {
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.setFileFilter(new FileNameExtensionFilter(".p3dv", "p3dv"));
					fileChooser.setCurrentDirectory(new File(workingDir));
					fileChooser.showOpenDialog(null);
					File file = fileChooser.getSelectedFile();

					if (file == null) {
						throw new NullPointerException();
					}

					if (CubicGridSaveManager.getCubicGridSaver().load(file)) {
						System.out.println("\tLOADED SAVE!");
					}

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("\tFAILED TO LOAD SAVE!");
				}

			}
		});
		openLayout.setMnemonic(KeyEvent.VK_O);
		openLayout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

		newLayout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				CubicGridSaveManager.getCubicGridSaver().getCubicGrid().reset();
			}
		});
		
		newLayout.setMnemonic(KeyEvent.VK_N);
		newLayout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	}
}
