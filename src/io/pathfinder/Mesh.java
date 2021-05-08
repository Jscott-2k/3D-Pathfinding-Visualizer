package io.pathfinder;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.pathfinder.math.Matrix;
import io.pathfinder.math.Vector3d;
import io.pathfinder.math.Vector4d;


/**
 * A Mesh consists of a group of triangles which form a 3D object.
 * The mesh is responsible for managing the triangles
 * 
 * Can render triangles and perform various transformation with them.
 * 
 * Currently applies single Color for all the triangles.
 * 
 * @author Justin Scott
 *
 */
public class Mesh {
	
	private ArrayList<Triangle> triangles;
	private Transform transform;
	private boolean isWireframe;
	private double averageProjectedZ = 0;
	
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
		Matrix meshMatrix = transform.getTransformationMatrix();
		Matrix meshViewMatrix = Matrix.MultiplyMatrix(meshMatrix, camera.getView());

		if(isWireframe) {
			for(Triangle tri : triangles) {
				tri.renderWire(graphics, camera, meshViewMatrix, meshMatrix);
			}
		}else {
			for(Triangle tri : triangles) {
				tri.renderFill(graphics, camera, meshViewMatrix, meshMatrix);
			}
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

	public boolean isWireframe() {
		return isWireframe;
	}

	public void setWireframe(boolean isWireframe) {
		this.isWireframe = isWireframe;
	}

	public double calcAverageProjectedZ() {
		int numTris = triangles.size();
		double sum = 0;
		for(Triangle tri : triangles) {
			sum+=tri.getAverageZ();
		}
		averageProjectedZ = sum / (double)numTris;
		return averageProjectedZ;
	}

	public ArrayList<Triangle> getTriangles() {
		return triangles;
	}

	public void setColor(Color color) {
		for(Triangle tri : triangles) {
			tri.setColor(color);
		}
	}

	public void setDefaultRotation() {
		transform.setRotation(0,0,0);
	}

}