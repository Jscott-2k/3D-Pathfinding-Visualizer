package io.pathfinder.math;

public class Vector4d extends Vector{
	private double x, y, z, w = 0;
	
	public Vector4d(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	public Vector4d() {
		this(0,0,0,0);
	}
	public Vector4d(Vector3d v3d, int w) {
		this(v3d.getX(), v3d.getY(), v3d.getZ(), w); 
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	public double getW() {
		return w;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public void setW(double w) {
		this.w = w;
	}

	public Vector4d copy() {
		return new Vector4d(x, y, z, w);
	}

	public Matrix toMatrix() {
		Matrix matrix = new Matrix(1,4);
		matrix.set(0, 0, x);
		matrix.set(0, 1, y);
		matrix.set(0, 2, z);
		matrix.set(0, 3, w);
		return matrix;
	}
	
	public static Vector4d BuildFromMatrix(Matrix matrix) {
		if(matrix.isDimension(1, 4)) {
			return new Vector4d(matrix.get(0, 0), matrix.get(0, 1), matrix.get(0, 2), matrix.get(0, 3));
		}
		return null;
	}
	
	@Override
	public String toString() {
		return x + ", " + y + ", " + z + ", " + w;
	}
	
	public Vector4d add(Vector4d other) {

			this.x += other.getX();
			this.y += other.getY();
			this.z += other.getZ();
			this.w += other.getW();
			
			return new Vector4d(x, y, z, w);
	
	}
}