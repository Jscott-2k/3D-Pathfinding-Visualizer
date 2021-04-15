package io.pathfinder.astar;

import java.util.ArrayList;
import java.util.HashMap;

public class Pathfinder {

	private ArrayList<Node> open;
	private ArrayList<Node> closed;
	private HashMap<Node, Double> fScores;
	private HashMap<Node, Double> gScores;

	private Node start, end, current;
	private boolean hasPath = false;

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

	public void run(ArrayList<Node> data, Node start, Node end) {

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
		
		while (!this.open.isEmpty()) {
			hasPath = step();
			if (hasPath) {
				break;
			}
		}
	}

	public ArrayList<Node> buildPath() {
		
		ArrayList<Node> path = new ArrayList<Node>();
		
		Node walker = end;
		while (walker != null) {
			System.out.println("Walking Path Node: " + walker.getLocationStr() + " - " + walker.getType());			
			path.add(walker);
			walker = walker.getNext();
		}

		System.out.println("Path Built!");
		return path;
	}
}