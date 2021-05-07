package io.pathfinder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import io.pathfinder.astar.Node;
import io.pathfinder.astar.NodeType;

public class Square extends Rectangle2D.Double{
	
	private NodeType nodeType;
	private int xUnit, yUnit, size;
	private double renderWidth, renderHeight;
	private Rectangle2D.Double outline;
	private final static Stroke DEFAULT_STROKE = new BasicStroke(1);
	private final static Stroke OUTLINE_STROKE = new BasicStroke(3);
	private boolean isHover;
	private Node node;
	
	public Square(int x, int y, int size, int canvasWidth, int canvasHeight, CubeNode cubeNode) {
		super();
		this.xUnit = x;
		this.yUnit = y;
		this.size = size;
		this.isHover = false;
		this.node = cubeNode.getNode();
		
		renderWidth = (canvasWidth / (double)size) / 2;
		renderHeight = ((canvasHeight  / (double)size));
		
		super.setRect(xUnit * renderWidth, yUnit * renderHeight, renderWidth, renderHeight);
		outline = new Rectangle2D.Double(xUnit * renderWidth, yUnit * renderHeight, renderWidth, renderHeight);
		nodeType = NodeType.EMPTY;

	}

	public Color getColor() {
		return nodeType.getColor();
	}
	
	public void setHover(boolean hover) {
		this.isHover = hover;
	}
	
	
	public void render(Graphics2D g){
		g.setStroke(OUTLINE_STROKE);

		g.setColor(isHover ? getColor().darker() : getColor());
		g.fill(this);
		
		g.setColor(isHover ? Color.GRAY : Color.BLACK);
		g.draw(outline);
		
		g.setStroke(DEFAULT_STROKE);
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
		node.setType(nodeType);
	}
	public NodeType getNodeType() {
		return this.nodeType;
	}

	public void setNode(Node node) {
		this.node = node;
	}
}
