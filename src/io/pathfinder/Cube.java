package io.pathfinder;

import java.awt.Color;

import io.pathfinder.math.Vector3d;

/**
 * Mesh of 6 Polygons 8 unique Points
 * 
 * @author Justin Scott
 *
 */
public class Cube extends Mesh {

	public Cube(Color color, int size, Vector3d position, Vector3d rotation) {
		super(position, rotation);	
		Color[] colors = new Color[6];
		
		for(int i=0;i<6;i++) {
			//colors[i] = new Color( (int)(color.getRed() * ( (i+1) / 7.0)),(int)(color.getGreen() * (i / 7.0)), (int)(color.getBlue() * (i / 7.0)));
			colors[i] = color;
		}
		
		double halfSize = size/2;
	
		Vector3d v0 = new Vector3d(-halfSize, -halfSize, -halfSize);
		Vector3d v1 =  new Vector3d(halfSize, -halfSize, -halfSize);
		Vector3d v2 =  new Vector3d(halfSize, halfSize, -halfSize);
		Vector3d v3 = new Vector3d(-halfSize, halfSize, -halfSize);
		
		Vector3d v4 = new Vector3d(-halfSize, halfSize, halfSize);
		Vector3d v5 = new Vector3d(halfSize, halfSize, halfSize);
		Vector3d v6 = new Vector3d(halfSize, -halfSize, halfSize);
		Vector3d v7 = new Vector3d(-halfSize,-halfSize, halfSize);

		// NOTE!! Order of triangles is extremely important for surface normals to be calculated correctly! 
		// Uses a left to right triangle wrapping for meshes. Reference image for how this works would be helpful
		// Wrapping should be consistent throughout all meshes
		super.addTriangles(
				//SOUTH FACE
				new Triangle(colors[0], v0, v2, v1),
				new Triangle(colors[0], v1, v3, v2),
				
				//TOP FACE
				new Triangle(colors[1], v2, v3, v4),
				new Triangle(colors[1], v2, v4, v5),
			
				
				//NORTH FACE
				new Triangle(colors[3], v1, v2, v5),
				new Triangle(colors[3], v1, v5, v6),
				
				//EAST FACE
				new Triangle(colors[4], v0, v7, v4),
				new Triangle(colors[4], v0, v4, v3),
				
				//WEST FACE
				new Triangle(colors[5], v5, v4, v7),
				new Triangle(colors[5], v5, v7, v6),
				
				//BOTTOM FACE
				new Triangle(colors[2], v0, v6, v7),
				new Triangle(colors[2], v0, v1, v6));
		

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
//		
//		super.addTriangles(
//				//NORTH FACE
//				new Triangle(Color.YELLOW, v5, v6, v7),
//				new Triangle(Color.YELLOW, v5, v7, v8)
//				);
	}

	public Cube(Color color, int size) {
		this(color, size, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0));
	}

}