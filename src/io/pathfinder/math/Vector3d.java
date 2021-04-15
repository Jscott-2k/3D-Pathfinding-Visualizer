package io.pathfinder.math;

public class Vector3d extends Vector{
	
	private double x, y, z = 0;
	
	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector3d() {
		this(0,0,0);
	}
	public Vector3d(Vector4d v4d) {
		this(v4d.getX(),v4d.getY(),v4d.getZ());
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
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setZ(double z) {
		this.z = z;
	}

	public Vector3d copy() {
		return new Vector3d(x, y, z);
	}
	
	@Override
	public String toString() {
		return (float)x + ", " + (float)y + ", " + (float)z;
	}

	public Matrix toMatrix() {
		Matrix matrix = new Matrix(1,3);
		matrix.set(0, 0, x);
		matrix.set(0, 1, y);
		matrix.set(0, 2, z);
		return matrix;
	}
	
	public static Vector3d BuildFromMatrix(Matrix matrix) {
		if(matrix.isDimension(1, 3)) {
			return new Vector3d(matrix.get(0, 0), matrix.get(0, 1), matrix.get(0, 2));
		}
		return null;
	}
	
	public Vector3d add(Vector3d other) {
		this.x += other.getX();
		this.y += other.getY();
		this.z += other.getZ();
		return this;
	}
	public Vector3d subtract(Vector3d other) {
		this.x -= other.getX();
		this.y -= other.getY();
		this.z -= other.getZ();
		return this;
	}
	public double magnitude() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	public Vector3d normalize() {
		double m = magnitude();
		this.x /= m;
		this.y /= m;
		this.z /= m;
		return this;
	}
	
	public Vector3d cross(Vector3d other) {
		Vector3d v = new Vector3d();
		v.setX(y * other.z - z * other.y);
		v.setY(z * other.x - x * other.z);
		v.setZ(x * other.y - y * other.x);
		return v;
		
	}
	public static Vector3d subtract(Vector3d v1, Vector3d v2) {
		
		Vector3d v1copy = v1.copy();
		Vector3d v2copy = v2.copy();
		
		return v1copy.subtract(v2copy);
	}
	public static double dot(Vector3d v1, Vector3d v2) {
		return v1.getX() * v2.getX() 
				+ v1.getY() + v2.getY() 
				+ v1.getZ() + v2.getZ();
	}
	public static Vector3d cross(Vector3d v1, Vector3d v2) {
		return v1.cross(v2);
	}
	public static Vector3d multiply(Vector3d v1, double s) {
		Vector3d v1copy = v1.copy();		
		return v1copy.multiply(s);
	}
	public Vector3d multiply(double s) {
		this.x *= s;
		this.y *= s;
		this.z *= s;
		return this;
	}
	public static Vector3d add(Vector3d v1, Vector3d v2) {
		Vector3d v1copy = v1.copy();
		Vector3d v2copy = v2.copy();
		
		return v1copy.add(v2copy);
	}
		
}