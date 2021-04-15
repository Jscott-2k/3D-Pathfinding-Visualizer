package io.pathfinder;

import java.awt.Canvas;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.pathfinder.astar.Node;
import io.pathfinder.astar.NodeArray;
import io.pathfinder.astar.NodeType;
import io.pathfinder.astar.Pathfinder;

public class CubicGrid {

	/**
	 * Storing Cube Nodes in two different structures but both point to same nodes.
	 * 
	 * Array List will allow us to easily sort and does not care about order Array
	 * will allow us quick lookup which is needed for A* and editor
	 * 
	 * Down-side is additional maintenance where both are correctly pointing to same
	 * cube node and both are updated accordingly.
	 */

	private ArrayList<CubeNode> cubeGroups; // Useful for sorting
	private CubicGridData cubeGridData; // Useful for searching

	private SquareGrid[] squareGrids;
	private int size = 0;
	private Pathfinder pathfinder;

	public CubicGrid(int size, int space, Canvas canvas) {
		this.size = size;
		double center = ((size - 1) * -space) / 2;

		squareGrids = new SquareGrid[size];
		cubeGroups = new ArrayList<CubeNode>();
		cubeGridData = new CubicGridData();
		cubeGridData.load(size, space, center, cubeGroups, null);

		for (int y = 0; y < size; y++) {
			squareGrids[y] = new SquareGrid(y, size, canvas.getWidth(), canvas.getHeight(), this);
		}

		this.pathfinder = new Pathfinder();
	}

	public void loadCubeNodesArrayList() {
		cubeGroups.clear();
		cubeGroups.ensureCapacity(size * size * size);

		System.out.println("Loading Cubes Nodes Array List: ");
		System.out.print("\t");
		for (int i = 0; i <= size - 1; i++) {
			for (int j = 0; j <= size - 1; j++) {
				System.out.print("*");
				for (int k = 0; k <= size - 1; k++) {

					cubeGroups.add(cubeGridData.getData()[i][j][k]);
				}
			}
			System.out.print("\n\t");
		}
		System.out.println("DONE!");
	}

	public CubeNode getCubeNode(int x, int y, int z) {
		return cubeGridData.getData()[x][y][z];
	}

	public void update() {
		for (CubeNode cubeGroup : cubeGroups) {
			cubeGroup.update();
		}
		sortCubes();
	}

	public void render(Graphics graphics, Camera camera) {
		for (CubeNode cubeGroup : cubeGroups) {
			cubeGroup.render(graphics, camera);
		}
	}

	public SquareGrid getSquareGrid(int editY) {
		if (editY > squareGrids.length - 1) {
			return squareGrids[squareGrids.length - 1];
		} else if (editY < 0) {
			return squareGrids[0];
		} else {
			return squareGrids[editY];
		}
	}

	public ArrayList<CubeNode> sortCubes() {
		Collections.sort(cubeGroups, new Comparator<CubeNode>() {
			@Override
			public int compare(CubeNode cg1, CubeNode cg2) {

				int diff = (int) (cg1.getAverageProjectedZ() - cg2.getAverageProjectedZ());

				if (diff == 0) {
					return 0;
				}

				return diff < 0 ? 1 : -1;
			}
		});
		return cubeGroups;
	}

	public CubicGridData getData() {
		return this.cubeGridData;
	}

	public CubicGrid loadFromSave(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
		cubeGridData = new CubicGridData();

		NodeArray loadedNodeArray = (NodeArray) objectInputStream.readObject();

		int space = 12;
		this.size = loadedNodeArray.getSize();
		double center = ((size - 1) * -space) / 2;

		this.cubeGridData = cubeGridData.load(size, space, center, cubeGroups, loadedNodeArray);

		Driver.getDriver().getScreen().setGridSize(size);

		loadCubeNodesArrayList();

		return this;
	}

	public Node getStart() {
		return cubeGridData.findFirstNode(NodeType.START);
	}

	public Node getEnd() {
		return cubeGridData.findFirstNode(NodeType.END);
	}

	public void findPath() {

		pathfinder.run(this.cubeGridData.getNodeArray().getAsArrayList(), getStart(), getEnd());
		if (pathfinder.hasPath()) {
			System.out.println("Found Path! Reconstructing...");
			pathfinder.buildPath().forEach(n -> {
				getCubeNode(n.getX(), n.getY(), n.getZ()).setTraced(true);
			});

		} else {
			System.out.println("No Path!");
		}
	}

}