package io.pathfinder.astar;

import java.awt.Color;

public enum NodeType implements java.io.Serializable{
	START(Color.GREEN),
	EMPTY(Color.WHITE),
	OBSTACLE(Color.DARK_GRAY),
	END(Color.RED);
		
	private Color color = Color.WHITE;
	private NodeType(Color color) {
		this.color = color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return color;
	}
	public static NodeType getNextType(NodeType current) {
		int nextIndex = (current.ordinal() + 1); 
		nextIndex = nextIndex > NodeType.values().length - 1 ? 0 : nextIndex;
		
		
		return NodeType.values()[nextIndex];
	}
	
	public static NodeType getPreviousType(NodeType current) {
		int nextIndex = (current.ordinal() - 1); 
		nextIndex = nextIndex < 0 ? NodeType.values().length - 1 : nextIndex;
	
		return NodeType.values()[nextIndex];
	}
}
