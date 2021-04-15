package io.pathfinder.astar;

public enum NeighborDirection {
	TOP(0,1,0),BOTTOM(0,-1,0),NORTH(0,0,-1),SOUTH(0,0,1),EAST(1,0,0),WEST(-1,0,0);
	
	private int xOff = 0, yOff = 0, zOff = 0;
	
	private NeighborDirection(int xOff, int yOff, int zOff) {
		this.xOff = xOff;
		this.yOff = yOff;
		this.zOff = zOff;
	}
	
	public int getXOff() {
		return this.xOff;
	}
	
	public int getYOff() {
		return this.yOff;
	}
	
	public int getZOff() {
		return this.zOff;
	}
	
	public static Node getNeighbor(Node[][][] nodes, Node currentNode, NeighborDirection direction) {
		
		int cx = currentNode.getX();
		int cy = currentNode.getY();
		int cz = currentNode.getZ();
		
		int x = direction.getXOff() + cx;
		int y = direction.getYOff() + cy;
		int z = direction.getZOff() + cz;
		
		if(x < 0 || y < 0 || z < 0 || x >= nodes.length || y >= nodes[0].length || z >= nodes[0][0].length){
			return null;
		}
		
		return nodes[x][y][z];
	}
}