package io.pathfinder.astar;

import java.awt.Color;

import io.pathfinder.CubeNode;

public class Node implements java.io.Serializable{
	
	private int x, y, z;
	private NodeType type;

	private transient Node next;
	private Node[] neighbors;
	
	
	public Node(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = NodeType.EMPTY;
		neighbors = new Node[NeighborDirection.values().length];
		this.next = null;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public void setNeighbor(Node[][][] nodes, NeighborDirection direction) {
		Node neighbor = NeighborDirection.getNeighbor(nodes, this, direction);
		neighbors[direction.ordinal()] = neighbor;
	}

	public Node[] getNeighbors(){
		return neighbors;
	}
	
	public String getLocationStr() {
		return x + ", " + y + ", " + z;
	}

	
	public Node getNext() {
		return next;
	}
	
	public void setNext(Node next) {
		this.next = next;
	}
}