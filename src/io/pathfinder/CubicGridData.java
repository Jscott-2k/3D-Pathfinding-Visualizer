package io.pathfinder;

import java.util.ArrayList;

import io.pathfinder.astar.Node;
import io.pathfinder.astar.NodeArray;
import io.pathfinder.astar.NodeType;

public class CubicGridData {

	private CubeNode[][][] data;
	private NodeArray nodeArray;

	public CubicGridData load(int size, int space, double center, ArrayList<CubeNode> cubeGroups, NodeArray nodeArray) {
		data = new CubeNode[size][size][size];

		System.out.println("Loading CubicGridData...");

		this.nodeArray = nodeArray;
		boolean needBind = false;
		if (this.nodeArray == null) {
			this.nodeArray = new NodeArray(size);
			needBind = true;
		}

		cubeGroups.clear();
		cubeGroups.ensureCapacity(size * size * size);
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					data[x][y][z] = new CubeNode((x * space) + center, center + (y * space), (z * space) + 50, x, y, z);
					Node node = this.nodeArray.get(x, y, z);

					if (node != null) {
						NodeType type = this.nodeArray.get(x, y, z).getType();
						if (type == null) {
							node.setType(NodeType.EMPTY);
						} else {
							node.setType(type);
						}
						data[x][y][z].setNode(node);
						this.nodeArray.setNeighbors(node);
					}
					cubeGroups.add(data[x][y][z]);
				}
			}
		}
		if (needBind) {
			this.nodeArray.bind(data);
		}

//		if (nodeArray == null) {
//			nodeArray = bindNodeArray();
//		}
		System.out.println("DONE!");
		return this;
	}

	public CubeNode[][][] getData() {
		return data;
	}

//	public NodeArray bindNodeArray() {
//		System.out.println("Binding Node Array...");
//		this.nodeArray = new NodeArray();
//		return nodeArray;
//	}

	private ArrayList<Node> getNodeArrayList() {
		return nodeArray.getAsArrayList();
	}

	public NodeArray getNodeArray() {
		return nodeArray;
	}

	public void setData(CubeNode[][][] data) {
		this.data = data;
	}

	public Node findFirstNode(NodeType type) {
		for (Node n : getNodeArrayList()) {

			if (n == null) {
				break;
			}

			if (n.getType() == type) {
				return n;
			}
		}
		return null;
	}
}