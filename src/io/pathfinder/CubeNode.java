package io.pathfinder;

import java.awt.Color;
import java.awt.Graphics;

import io.pathfinder.astar.Node;
import io.pathfinder.astar.NodeType;

public class CubeNode {

	private Mesh testCube1, testCube2, testCube3;
	private int traced = 0;

	private Node node;

	public CubeNode(double x, double y, double z, int xUnit, int yUnit, int zUnit) {
//		testCube1 = new Cube(Color.DARK_GRAY, 6);
//		testCube1.translate(x, y, z);

		testCube2 = new Cube(Color.WHITE, 4);
		testCube2.translate(x, y, z);
		testCube2.setWireframe(true);
//		
		testCube3 = new Cube(Color.GREEN, 2);
		testCube3.translate(x, y, z);

		this.node = new Node(xUnit, yUnit, zUnit);
	}

	public void update() {
		// testCube1.rotate(0,0,0);
		// testCube2.rotate(.5,0,.5);

		if (NodeType.EMPTY != node.getType() && NodeType.OBSTACLE != node.getType()) {

			//testCube3.rotate(1, 1, 1);
		}
		
		testCube3.setColor(node.getType().getColor());
		
		if (NodeType.EMPTY == node.getType() && traced == 2) {
			testCube3.setColor(Color.ORANGE);

		}else if(NodeType.EMPTY == node.getType() && traced == 3) {
			testCube3.setColor(Color.CYAN);
		}
		else if (NodeType.EMPTY == node.getType() && traced == 1) {
			testCube3.setColor(Color.BLUE);
			testCube3.rotate(1, 1, 1);
		}
	}

	public void render(Graphics graphics, Camera camera) {
		testCube3.render(graphics, camera);
//		testCube2.render(graphics, camera);
//		testCube1.render(graphics, camera);
	}

	public int getAverageProjectedZ() {
		return (int) testCube3.calcAverageProjectedZ();
	}

	public boolean wasProjected() {
		for (Triangle tri : testCube2.getTriangles()) {
			if (!tri.wasProjected()) {
				return false;
			}
		}
		return true;
	}

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void setTraced(int traced) {
		this.traced = traced;
	}

	public void setNeighborTraced() {
		System.out.println("Traced Neighbor");
		this.traced = 2;
	}
}
