package io.pathfinder.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import io.pathfinder.CubicGridSaveManager;
import io.pathfinder.Driver;
import io.pathfinder.astar.NodeType;

public class EditMenu extends JMenu {

	private JCheckBoxMenuItem edit, wireframe;
	private NodeType appliedNodeType;;
	private JMenuItem cubeColor;

	public EditMenu() {
		super("Edit");
		edit = new JCheckBoxMenuItem("edit mode");
		wireframe = new JCheckBoxMenuItem("wireframe");
		
		JMenuItem size = new JMenuItem("size");
		JMenuItem backgroundColor = new JMenuItem("background color");
		cubeColor = new JMenuItem("cube color");
		edit.setSelected(false);

		initActions();

		add(edit);
		add(wireframe);
		add(size);
		addSeparator();
		add(backgroundColor);
		add(cubeColor);
	}

	private void initActions() {
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("Toggled Edit Mode...");
			}
		});
		edit.setMnemonic(KeyEvent.VK_E);
		edit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));

		wireframe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				CubicGridSaveManager.getCubicGridSaver().getCubicGrid().setIsWireframe(wireframe.isSelected());
			}
		});
		
		wireframe.setMnemonic(KeyEvent.VK_W);
		wireframe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		
		cubeColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Opening Color Dialog");
				JDialog colorDialog = new JDialog(Driver.getDriver().getScreen().getFrame(), "Set Cube Color");
				JPanel p = new JPanel();
				JButton applyButton = new JButton("Apply");
				JButton closeButton = new JButton("Close");
				JColorChooser tcc = new JColorChooser();
				
				appliedNodeType = NodeType.START;
				JComboBox<String> cubeTypeBox = new JComboBox<String>(
						new String[] { "Start", "Empty", "Obstacle", "End" });

				closeButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						colorDialog.dispose();
					}
				});

				applyButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						appliedNodeType.setColor(tcc.getColor());
						colorDialog.dispose();
					}
				});
				cubeTypeBox.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						appliedNodeType = NodeType.values()[cubeTypeBox.getSelectedIndex()];
						System.out.println("Selected Type: " + appliedNodeType.toString());
					}
				});

				p.add(cubeTypeBox);
				p.add(tcc);
				p.add(closeButton);
				p.add(applyButton);

				colorDialog.add(p);
				colorDialog.setSize(750, 450);
				colorDialog.setLocationRelativeTo(Driver.getDriver().getScreen().getFrame());
				colorDialog.setResizable(false);

				colorDialog.setVisible(true);

			}

		});
	}

	public JCheckBoxMenuItem getEdit() {
		return edit;
	}
}
