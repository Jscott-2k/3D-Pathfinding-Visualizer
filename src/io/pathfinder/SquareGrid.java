package io.pathfinder;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import io.pathfinder.astar.Node;
import io.pathfinder.astar.NodeType;


/**
 * 
 * This class acts as a cross-section of the CubicGrid
 * Used for the built-in editor of the cubic grid,
 *  where the user can add and remove nodes of different types on a certain section of the grid
 *  
 * 
 * 
 * @Author Justin Scott
 *
 */
public class SquareGrid {

	private int y;
	private int size;
	
	private int hoveredX = 0, hoveredZ = 0; //Index of the square being hovered by cursor
	
	// Each square represents a CubeNode of the grid. Cross section so it stores squares by only x and z value, not y.
	private Square[][] squares;

	public SquareGrid(int y, int size, int canvasWidth, int canvasHeight, CubicGrid cubicGrid) {

		this.y = y;
		this.size = size;
		this.squares = new Square[size][size];
		//this.cubicGrid = cubicGrid;

		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				squares[x][z] = new Square(x, z, size, canvasWidth, canvasHeight, cubicGrid.getCubeNode(x, y, z));
			}
		}
	}

	public void onLeftRelease(Point mousePosition) {

		if (mousePosition == null) {
			return;
		}

		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				Square square = squares[x][z];
				if (square.contains(mousePosition)) {
					NodeType next = NodeType.getNextType(square.getNodeType());
					square.setNodeType(next); 
				}
			}
		}
	}

	public void onMouseMove(Point mousePosition) {

		if (mousePosition == null) {
			return;
		}

		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				Square square = squares[x][z];
				if (square.contains(mousePosition)) {
					square.setHover(true);
					hoveredX = x;
					hoveredZ = z;
				} else {
					square.setHover(false);
				}
			}
		}
	}
	public void onRightRelease(Point mousePosition) {

		if (mousePosition == null) {
			return;
		}
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				Square square = squares[x][z];
				if (square.contains(mousePosition)) {
					NodeType next = NodeType.getPreviousType(square.getNodeType());
					square.setNodeType(next);
				}
			}
		}

	}

	public void render(Graphics2D g) {
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {

				Square square = squares[x][z];
				square.render(g);
			}
		}
		// System.out.println("HOVERED: " + hoveredX + " " + hoveredY);
		squares[hoveredX][hoveredZ].render(g);

	}

	public void setSquareNode(int x, int z, Node node) {
		squares[x][z].setNode(node);
		squares[x][z].setNodeType(node.getType());
	}
}
