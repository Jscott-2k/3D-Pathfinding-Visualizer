package io.pathfinder.astar;

import java.util.ArrayList;
import java.util.HashMap;

import io.pathfinder.CubeNode;
import io.pathfinder.CubicGrid;
import io.pathfinder.Driver;

public class Pathfinder {

	private ArrayList<Node> open;
	private ArrayList<Node> closed;
	private HashMap<Node, Double> fScores;
	private HashMap<Node, Double> gScores;

	private CubicGrid grid;
	private boolean running = false;
	private Node start, end, current;
	private boolean hasPath = false;

	private int runMode = 0;
	
	public boolean hasPath() {
		return hasPath;
	}

	private double h(Node n) {
		return dist(n, end);
	}

	private double f(Node n) {

		double h = h(n);
		double g = g(current);
		double f = g + h;

		return f;
	}

	private double g(Node n) {

		double g = gScores.get(n) + 1;
		return g;
	}

	private double dist(Node a, Node b) {
//		return Math.sqrt(((b.getX() - a.getX()) * (b.getX() - a.getX()))
//				+ ((b.getY() - a.getY()) * (b.getY() - a.getY())) + ((b.getZ() - a.getZ()) * (b.getZ() - a.getZ())));

		return Math.abs(b.getX() - a.getX()) + Math.abs(b.getY() - a.getY()) + Math.abs(b.getZ() - a.getZ());
	}

	private boolean step() {

		Node next = getLowestF();
		this.current = next;

		// Visual
		CubeNode currentCN = grid.getCubeNode(current.getX(), current.getY(), current.getZ());
		currentCN.setTraced(3);

		if (this.current == end) {
			System.out.println("Found End!");
			return true;
		}
		open.remove(current);
		closed.add(current);

		Node[] neighbors = current.getNeighbors();
		System.out.println("Stepping Path! " + current.getLocationStr());
		System.out.println("Neighbors:");
		for (Node neighbor : neighbors) {

			if (neighbor == null) {
				continue;
			}
			System.out.println("\tNeighbor:" + neighbor.getLocationStr());

			if (neighbor.getType() == NodeType.OBSTACLE || closed.contains(neighbor)) {
				continue;
			}

			// Visual
			CubeNode cubeNode = grid.getCubeNode(neighbor.getX(), neighbor.getY(), neighbor.getZ());
			cubeNode.setNeighborTraced();

			neighbor.setNext(current);
			gScores.put(neighbor, g(current));
			fScores.put(neighbor, f(neighbor));

			if (!open.contains(neighbor)) {
				open.add(neighbor);
			}

		}

		return false;
	}

	private Node getLowestF() {

		Node resultNode = open.get(0);
		double minF = fScores.get(resultNode);

		System.out.println("Finding Lowest F: ");

		for (Node n : open) {

			System.out.print(fScores.get(n) + ", ");
			double testF = fScores.get(n);
			if (testF < minF) {
				resultNode = n;
				minF = testF;
			}
		}
		System.out.println("\n\t Found: " + resultNode.getLocationStr() + " : " + minF);

		return resultNode;
	}

	public void run(ArrayList<Node> data, Node start, Node end, CubicGrid grid, int runMode) {

		if (start == null || end == null) {
			System.out.println("Null start or end node. Cannot find path!");
			return;
		}

		this.grid = grid;
		this.runMode = runMode;
		this.open = new ArrayList<Node>();
		this.closed = new ArrayList<Node>();

		this.start = start;
		this.end = end;
		this.current = start;
		open.add(start);

		fScores = new HashMap<Node, Double>();
		gScores = new HashMap<Node, Double>();

		gScores.put(start, 0.0);
		fScores.put(start, h(this.start));
		System.out.println(h(start));
		System.out.println("Start Location: " + start.getLocationStr());
		System.out.println("End Location: " + end.getLocationStr());

		hasPath = false;
		System.out.println("Running Pathfinder: " + "\n\tStart: " + start + "\n\tEnd:" + end);

		if (runMode == 0) {

			while (!this.open.isEmpty()) {
				hasPath = step();
				if (hasPath) {
					break;
				}
			}
		} else {
			running = true;
		}
	}

	public void update() {

		if (this.open == null) {
			return;
		}

		long currentFrameTick = Driver.getDriver().getScreen().getFrameTick();
		if (running && !this.open.isEmpty() && !hasPath && currentFrameTick % 5 == 0) {
			hasPath = step();
		}
		if (hasPath || this.open.isEmpty()) {
			running = false;
		}
	}

	public boolean isRunning() {
		return running;
	}

	public ArrayList<Node> buildPath() {

		ArrayList<Node> path = new ArrayList<Node>();
		Node walker = end;
		while (walker != null) {
			System.out.println("Walking Path Node: " + walker.getLocationStr() + " - " + walker.getType());
			path.add(0, walker);
			walker = walker.getNext();
		}

		System.out.println("Path Built!");
		hasPath = false;
		return path;
	}

	public int getRunMode() {
		return this.runMode;
	}
}