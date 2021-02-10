package io.pathfinder;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.pathfinder.math.Matrix;
import io.pathfinder.math.Vector3d;

public class Mesh {
	private ArrayList<Triangle> triangles;
	private Transform transform;
	
	public Mesh(Vector3d position, Vector3d rotation) {
		triangles = new ArrayList<Triangle>();
		this.transform = new Transform(position, rotation);
	}
	
	protected void addTriangles(Triangle... pTriangles) {
		for(Triangle tri : pTriangles) {
			triangles.add(tri);
		}
	}

	public void render(Graphics graphics, Camera camera) {
		
		Matrix meshViewMatrix = Matrix.MultiplyMatrix(transform.getTransformationMatrix(), camera.getView());

		for(Triangle tri : triangles) {
			tri.render(graphics, camera, meshViewMatrix);
		}
		
	}
	
	public void rotate(double xDegrees,double yDegrees,double zDegrees){
		Vector3d rotation = transform.getRotation();
		
		double xRadians = Math.toRadians(xDegrees);
		double yRadians = Math.toRadians(yDegrees);
		double zRadians = Math.toRadians(zDegrees);
		
		rotation.add(new Vector3d(xRadians, yRadians, zRadians));
		transform.setRotation(rotation);
		

	}
	
	public Transform getTransform(){
		return transform;
	}
	
	public void translate(double xOffset, double yOffset, double zOffset) {
		transform.translate(xOffset, yOffset, zOffset);
	}
	
	public ArrayList<Triangle> sortTriangles() {
		Collections.sort(triangles, new Comparator<Triangle>(){
			@Override
			public int compare(Triangle tri1, Triangle tri2) {
				System.out.println(tri1.getAverageZ() );
				int diff = (int) (tri1.getAverageZ() - tri2.getAverageZ());
				
				if(diff == 0) {
					return 0;
				}
				
				return  diff < 0 ? 1 : -1;
			}
		});
		return triangles;
	}

}