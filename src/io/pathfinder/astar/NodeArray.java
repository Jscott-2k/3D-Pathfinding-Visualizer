package io.pathfinder.astar;

import java.util.ArrayList;

import io.pathfinder.CubeNode;

/**
 * Serializable class containing save for cubic grid node data. To be stored in .p3dv file. 
 * 
 * @author Justin Scott
 *
 */
public class NodeArray implements java.io.Serializable {

	private static final long serialVersionUID = 4479787027341967227L;
	private Node[][][] nodes;
	private int size;


	public NodeArray(int size) {
		this.size = size;
		this.nodes = new Node[size][size][size];
	}
	
	public void bind(CubeNode[][][] data) {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					nodes[x][y][z] = data[x][y][z].getNode();

				}
			}
		}
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					setNeighbors(data[x][y][z].getNode());
				}
			}
		}
	}

	public void setNeighbors(Node currentNode) {
		NeighborDirection[] directions = NeighborDirection.values();
		System.out.println("Setting Neighbors for Node: " + currentNode.getLocationStr());
		for (int i = 0; i < directions.length; i++) {
			currentNode.setNeighbor(nodes, directions[i]);

			System.out.println("\tAssigning Neighbor: " + directions[i].toString());
		}
	}

	public Node get(int x, int y, int z) {

		return nodes[x][y][z];
	}

	public int getSize() {
		return size;
	}

	public ArrayList<Node> getAsArrayList() {

		ArrayList<Node> aList = new ArrayList<Node>();
		aList.ensureCapacity(size * size * size);
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					aList.add(nodes[x][y][z]);
				}
			}
		}

		return aList;
	}
}