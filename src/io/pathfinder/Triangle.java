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

	private Vector3d[] verticies;
	private Vector3d[] projectedVerticies;
	private final static Polygon EMPTY_POLY = new Polygon();
	private Color color;
	private boolean wasProjected = false;

	public Triangle(Color color, Vector3d v1, Vector3d v2, Vector3d v3) {
		this.verticies = new Vector3d[3];
		projectedVerticies = new Vector3d[3];
		this.color = color;
		this.verticies[0] = v1.copy();
		this.verticies[1] = v2.copy();
		this.verticies[2] = v3.copy();
	}

	public double getAverageZ() {
		double sum = 0;

		for (Vector3d v3d : projectedVerticies) {

			if (v3d == null) {
				break;
			}

			sum += v3d.getZ();
		}
		return sum / projectedVerticies.length;
	}

	public void renderWire(Graphics g, Camera camera, Matrix meshViewMatrix, Matrix meshTransformedMatrix) {
		Polygon p = render(g, camera, meshViewMatrix, meshTransformedMatrix);
		g.drawPolygon(p);
	}

	public void renderFill(Graphics g, Camera camera, Matrix meshViewMatrix, Matrix meshTransformedMatrix) {
		Polygon p = render(g, camera, meshViewMatrix, meshTransformedMatrix);
		g.fillPolygon(p);
	}

	Polygon render(Graphics g, Camera camera, Matrix meshViewMatrix, Matrix meshTransformedMatrix) {

		Polygon polygon = new Polygon();
		g.setColor(color);

		// Vector3d[] transformedVerticies = new Vector3d[verticies.length];
		for (int i = 0; i < verticies.length; i++) {

//			transformedVerticies[i] = new Vector3d(Vector4d.BuildFromMatrix(
//					Matrix.MultiplyMatrixVector(meshTransformedMatrix, new Vector4d(verticies[i], 1))));

			Vector4d v4d = new Vector4d(verticies[i], 1);
			Vector4d vProjected = camera.getProjection(meshViewMatrix, v4d);
			projectedVerticies[i] = new Vector3d(vProjected);

			double splitScreenCutoff = camera.isSplitScreen() ? camera.getScreenConfiguration().getWidth() / 2.0 : 0;

			if ((int) vProjected.getX() > camera.getScreenConfiguration().getWidth()
					|| (int) vProjected.getX() < splitScreenCutoff
					|| (int) vProjected.getY() > camera.getScreenConfiguration().getHeight()
					|| (int) vProjected.getY() < 0) {
				return EMPTY_POLY;
			} else if (vProjected.getZ() < camera.getNear() || vProjected.getZ() > camera.getFar()) {
				return EMPTY_POLY;
			}
			polygon.addPoint((int) vProjected.getX(), (int) vProjected.getY());

		}

//		if (camera.needCull(transformedVerticies[0], getSurfaceNormal(transformedVerticies))) {
//			return EMPTY_POLY;
//		}

		wasProjected = true;
		return polygon;
	}

	public Vector3d getSurfaceNormal(Vector3d[] meshTransformedVertices) {

		Vector3d p = Vector3d.subtract(meshTransformedVertices[0], meshTransformedVertices[1]);
		Vector3d q = Vector3d.subtract(meshTransformedVertices[0], meshTransformedVertices[2]);
		// System.out.println((p.cross(q)));
		return (p.cross(q)).normalize();

	}

	public boolean wasProjected() {
		return wasProjected;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
