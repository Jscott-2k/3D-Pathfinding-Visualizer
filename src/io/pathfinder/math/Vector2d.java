package io.pathfinder.math;

public class Vector2d extends Vector{
	
	private double x, y = 0;
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public Vector2d() {
		this(0,0);
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	@Override
	public String toString() {
		return getX() + ", " + getY();
	}

	public Vector2d copy() {
		return new Vector2d(x, y);
	}
	
	public Matrix toMatrix() {
		Matrix matrix = new Matrix(1,2);
		matrix.set(0, 0, x);
		matrix.set(0, 1, y);
		return matrix;
	}
	
	public static Vector2d BuildFromMatrix(Matrix matrix) {
		if(matrix.isDimension(1, 2)) {
			return new Vector2d(matrix.get(0, 0), matrix.get(0, 1));
		}
		return null;
	}

	public Vector2d add(Vector2d other) {
		this.x += other.getX();
		this.y += other.getY();
		return new Vector2d(x, y);
	}

}