package io.pathfinder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import io.pathfinder.math.Matrix;
import io.pathfinder.math.Vector2d;
import io.pathfinder.math.Vector3d;
import io.pathfinder.math.Vector4d;

public class Triangle {

	private ArrayList<Vector3d> verticies;
	private Color color;
	
	public Triangle(Color color, Vector3d v1, Vector3d v2, Vector3d v3) {
		this.verticies = new ArrayList<Vector3d>();
		this.color = color;
		this.verticies.add(v1.copy());
		this.verticies.add(v2.copy());
		this.verticies.add(v3.copy());
	}
	
	public double getAverageZ(){
		double sum = 0;
		for(Vector3d v3d : verticies) {
			sum+=v3d.getZ();
		}

		return sum / verticies.size();
	}
	
	public void render(Graphics g, Camera camera, Matrix meshViewMatrix) {
		Polygon polygon = new Polygon();
		g.setColor(color);

		for(Vector3d v : verticies) {
			
			Vector4d v4d = new Vector4d(v, 1);	
			Vector4d vProjected = camera.getProjection(meshViewMatrix, v4d);
			polygon.addPoint((int) vProjected.getX(), (int) vProjected.getY());
			
		}
		g.drawPolygon(polygon);
		//g.drawPolygon(polygon);
		
	}
	
}
