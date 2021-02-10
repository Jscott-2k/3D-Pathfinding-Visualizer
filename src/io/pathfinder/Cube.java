package io.pathfinder;

import java.awt.Color;

import io.pathfinder.math.Vector3d;



/**
 * 6 Polygons
 * 8 Points
 * @author Justin Scott
 *
 */
public class Cube extends Mesh{
	
	public Cube(Color color, int size, Vector3d position, Vector3d rotation) {
		super(position, rotation);	
		
		double halfSize = size/2;
	
		Vector3d v1 = new Vector3d(-halfSize, -halfSize, -halfSize);
		Vector3d v2 =  new Vector3d(-halfSize, halfSize, -halfSize);
		Vector3d v3 =  new Vector3d(halfSize, halfSize, -halfSize);
		Vector3d v4 = new Vector3d(halfSize, -halfSize, -halfSize);
		
		Vector3d v5 = new Vector3d(-halfSize, -halfSize, halfSize);
		Vector3d v6 = new Vector3d(-halfSize, halfSize, halfSize);
		Vector3d v7 = new Vector3d(halfSize, halfSize, halfSize);
		Vector3d v8 = new Vector3d(halfSize, -halfSize, halfSize);

		super.addTriangles(
				//SOUTH FACE
				new Triangle(color, v1, v2, v3),
				new Triangle(color, v1, v3, v4),
				
				//TOP FACE
				new Triangle(color, v2, v3, v6),
				new Triangle(color, v3, v6, v7),
				
				//BOTTOM FACE
				new Triangle(color, v1, v5, v4),
				new Triangle(color, v4, v5, v8),
				
				//NORTH FACE
				new Triangle(color, v5, v6, v7),
				new Triangle(color, v5, v7, v8),
				
				//EAST FACE
				new Triangle(color, v1, v2, v5),
				new Triangle(color, v2, v5, v6),
				
				//WEST FACE
				new Triangle(color, v3, v4, v7),
				new Triangle(color, v4, v7, v8));
		

//		super.addTriangles(
//				//SOUTH FACE
//				new Triangle(Color.RED, v1, v2, v3),
//				new Triangle(Color.RED, v1, v3, v4),
//				
//				//TOP FACE
//				new Triangle(Color.BLUE, v2, v3, v6),
//				new Triangle(Color.BLUE, v3, v6, v7),
//				
//				//BOTTOM FACE
//				new Triangle(Color.GREEN, v1, v5, v4),
//				new Triangle(Color.GREEN, v4, v5, v8),
//				
//				//NORTH FACE
//				new Triangle(Color.YELLOW, v5, v6, v7),
//				new Triangle(Color.YELLOW, v5, v7, v8)
//				);
//		
//		super.addTriangles(
//		
//		
//		//EAST FACE
//		new Triangle(Color.WHITE, v1, v2, v5),
//		new Triangle(Color.WHITE, v2, v5, v6),
//		
//		//WEST FACE
//		new Triangle(Color.CYAN, v3, v4, v7),
//		new Triangle(Color.CYAN, v4, v7, v8));
	}

	public Cube(Color color, int size) {
		this(color,size, new Vector3d(0,0,0), new Vector3d(0,0,0));
	}
	
}