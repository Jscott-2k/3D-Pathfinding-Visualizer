package io.pathfinder;

import java.awt.Canvas;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;

import io.pathfinder.astar.Node;
import io.pathfinder.astar.NodeArray;
import io.pathfinder.astar.NodeType;
import io.pathfinder.astar.Pathfinder;

/**
 * Class for encapsulating the entire cubic grid of CubeNodes
 * @author Justin Scott
 *
 */
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

	private ArrayList<CubeNode> cubeNodes; // Useful for sorting
	private CubicGridData cubeGridData; // Useful for searching

	private ArrayList<Node> path;
	private int pathIndex = 0;

	private SquareGrid[] squareGrids;
	private int size = 0;
	private Pathfinder pathfinder;
	private boolean buildingPath = false;
	private boolean canUpdate = true;
	private boolean searchingPath = false;

	private boolean isWireframe = false;
	
	public CubicGrid(int size, int space, Canvas canvas) {
		this.size = size;
		double center = ((size - 1) * -space) / 2;

		cubeNodes = new ArrayList<CubeNode>();
		cubeGridData = new CubicGridData();
		cubeGridData.load(size, space, center, cubeNodes, null);
		loadCubeNodesArrayList();
		initSquareGrid(canvas, cubeGridData);

		this.pathfinder = new Pathfinder();
	}

	private void initSquareGrid(Canvas canvas) {
		squareGrids = new SquareGrid[size];
		for (int y = 0; y < size; y++) {
			squareGrids[y] = new SquareGrid(y, size, canvas.getWidth(), canvas.getHeight(), this);
		}
	}

	public void loadCubeNodesArrayList() {
		cubeNodes.clear();
		cubeNodes.ensureCapacity(size * size * size);

		System.out.println("Loading Cubes Nodes Array List: ");
		System.out.print("\t");
		for (int i = 0; i <= size - 1; i++) {
			for (int j = 0; j <= size - 1; j++) {
				System.out.print("*");
				for (int k = 0; k <= size - 1; k++) {

					cubeNodes.add(cubeGridData.getData()[i][j][k]);
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

		if (pathfinder.getRunMode() == 1) {
			pathfinder.update();
			if (!pathfinder.isRunning()) {
				onPathfindEnd();
			}
		}
		for (CubeNode cubeGroup : cubeNodes) {
			cubeGroup.update();
		}
		if (buildingPath) {
			
			updatePathBuildAnimation();
		}
		sortCubes();
	}

	public void render(Graphics graphics, Camera camera) {
		try {
			for (CubeNode cubeGroup : cubeNodes) {
				cubeGroup.render(graphics, camera);
			}
		} catch (ConcurrentModificationException e) { //Occasional crash without try/catch :( Need better solution but works for time being
			e.printStackTrace();
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
		Collections.sort(cubeNodes, new Comparator<CubeNode>() {
			@Override
			public int compare(CubeNode cg1, CubeNode cg2) {

				int diff = (int) (cg1.getAverageProjectedZ() - cg2.getAverageProjectedZ());

				if (diff == 0) {
					return 0;
				}

				return diff < 0 ? 1 : -1;
			}
		});
		return cubeNodes;
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

		this.cubeGridData = cubeGridData.load(size, space, center, cubeNodes, loadedNodeArray);

		Driver.getDriver().getScreen().setGridSize(size);

		loadCubeNodesArrayList();

		initSquareGrid(Driver.getDriver().getScreen(), cubeGridData);

		return this;
	}

	private void initSquareGrid(Canvas canvas, CubicGridData cubeGridData) {
		initSquareGrid(canvas);

		for (int y = 0; y <= size - 1; y++) {
			SquareGrid grid = squareGrids[y];
			for (int x = 0; x <= size - 1; x++) {
				for (int z = 0; z <= size - 1; z++) {
					grid.setSquareNode(x, z, cubeGridData.getData()[x][y][z].getNode());
				}
			}
		}

	}

	public Node getStart() {
		return cubeGridData.findFirstNode(NodeType.START);
	}

	public Node getEnd() {
		return cubeGridData.findFirstNode(NodeType.END);
	}

	
	/**
	 * Every 10 frames the next node in the path is traced. Uses pathIndex and buildingPath field.
	 * 
	 * pathIndex tells which node is next to be traced
	 */
	public void updatePathBuildAnimation() {

		if (buildingPath) {
			long currentFrameTick = Driver.getDriver().getScreen().getFrameTick();
			if (currentFrameTick % 10 == 0) {

				pathIndex++;
				Node pathNode = path.get(pathIndex);
				double progress = (pathIndex/ (double) (path.size()-1)  ) * 100.0;
				System.out.println("Building Path... " +progress + "% node - " + pathNode.getLocationStr());
				getCubeNode(pathNode.getX(), pathNode.getY(), pathNode.getZ()).setTraced(1);
			}
			
			if (pathIndex >= path.size() - 1) {
				pathIndex = 0;
				buildingPath = false;
			}
		}
	}

	private void buildPath() {
		if (pathfinder.hasPath()) {
			System.out.println("Found Path! Reconstructing...");
			path = pathfinder.buildPath();

			pathIndex = 0;
			buildingPath = true;
			// path.forEach(n ->{
			// getCubeNode(n.getX(), n.getY(), n.getZ()).setTraced(true);
			// });

		} else {
			System.out.println("No Path!");
		}
	}

	private void onPathfindEnd() {
		if (searchingPath) {
			buildPath(); 
		}
		searchingPath = false;
	}

	
	/**
	 * Executes pathfinder run method and searches for path 
	 * 3 possible run modes. Skip = 0, Step = 1, Step on key = 2.
	 * 
	 * Skip mode attempts to execute all steps of the pathfinder in 1 single frame, whereas step does one step per n frames. 
	 * 
	 * Step on key is not implemented yet
	 * @param RUN_MODE
	 */
	public void findPath(int RUN_MODE) {
		cubeNodes.forEach(n -> {
			n.setTraced(0);
		});
		searchingPath = true;
		pathfinder.run(this.cubeGridData.getNodeArray().getAsArrayList(), getStart(), getEnd(), this, RUN_MODE);

		if (pathfinder.getRunMode() == 0) { // If skip mode, search would be over at this point so  we call onPathfindEnd
			onPathfindEnd();
		}
	}

	public void reset() {

		Driver.getDriver().getScreen().setUpdate(false);
		cubeNodes = new ArrayList<CubeNode>();
		cubeGridData = new CubicGridData();

		int space = 12;
		double center = ((size - 1) * -space) / 2;

		cubeGridData.load(size, space, center, cubeNodes, null);
		loadCubeNodesArrayList();
		initSquareGrid(Driver.getDriver().getScreen(), cubeGridData);
		Driver.getDriver().getScreen().setUpdate(true);
	}

	public boolean isWireframe() {
		return this.isWireframe;
	}
	
	public void setIsWireframe(boolean isWireframe) {
		this.isWireframe = isWireframe;
		
		for(CubeNode cubeNode : cubeNodes) {
			cubeNode.setWireframe(this.isWireframe);
		}
		
	}
	
}