package io.pathfinder;

import java.io.*;

public class CubicGridSaveManager {

	private CubicGrid cubicGrid;

	private static CubicGridSaveManager INSTANCE;

	public static CubicGridSaveManager getCubicGridSaver() {
		if (INSTANCE == null) {
			INSTANCE = new CubicGridSaveManager();
		}
		return INSTANCE;
	}

	private CubicGridSaveManager() {
	}

	public CubicGridData getSaveData() {
		return cubicGrid.getData();
	}

	public void setCubicGrid(CubicGrid cubicGrid) {
		this.cubicGrid = cubicGrid;
	}

	public CubicGrid getCubicGrid() {
		return this.cubicGrid;
	}

	public void save() throws IOException {
		FileOutputStream fOut = new FileOutputStream("save.p3dv");
		ObjectOutputStream out = new ObjectOutputStream(fOut);
		System.out.println("\t WRITING node array to \"save.p3dv\"...");
		out.writeObject(cubicGrid.getData().getNodeArray());
		out.close();
		fOut.close();
		System.out.println("\t DONE!");

	}

	public boolean load(File file) throws Exception {
		
		Driver.getDriver().getScreen().setUpdate(false);
		
	
		System.out.println("\tOPENING FILE: " + file.getName());

		FileInputStream fileInputStream = new FileInputStream(file);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		this.cubicGrid = this.cubicGrid.loadFromSave(objectInputStream);
		
		Driver.getDriver().getScreen().setUpdate(true);
		
		return this.cubicGrid != null;
		
	}
}