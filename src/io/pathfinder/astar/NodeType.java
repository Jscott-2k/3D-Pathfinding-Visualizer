package io.pathfinder.astar;

import java.awt.Color;


/**
 * Stores static instances of the types of Nodes. 
 * There are 4 types: START, EMPTY, OBSTACLE, and END.
 * Each have distinct colors
 * 
 * @author Justin Scott
 *
 */
public enum NodeType implements java.io.Serializable{
	START(Color.GREEN),
	EMPTY(new Color(255,255,255, 100)),
	OBSTACLE(new Color(75,75,75, 100)),
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
	
	/**
	 * Get the next NodeType in the enum values() array of NodeTypes given the type passed in
	 * @param current
	 * @return
	 */
	public static NodeType getNextType(NodeType current) {
		int nextIndex = (current.ordinal() + 1); 
		nextIndex = nextIndex > NodeType.values().length - 1 ? 0 : nextIndex;
		
		
		return NodeType.values()[nextIndex];
	}
	
	/**
	 * Get the previous NodeType in the enum values() array of NodeTypes given the type passed in
	 * @param current
	 * @return
	 */
	public static NodeType getPreviousType(NodeType current) {
		int nextIndex = (current.ordinal() - 1); 
		nextIndex = nextIndex < 0 ? NodeType.values().length - 1 : nextIndex;
	
		return NodeType.values()[nextIndex];
	}
}
