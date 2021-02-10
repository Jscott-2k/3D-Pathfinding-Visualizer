package io.pathfinder;

import io.pathfinder.math.Matrix;
import io.pathfinder.math.MatrixBuilder;
import io.pathfinder.math.Vector3d;

public class Transform {
	
	private Vector3d position;
	private Vector3d rotation;
	
	private Matrix rotationMatrix;
	private Matrix positionMatrix;
	
	public Transform(Vector3d position, Vector3d rotation)
	{
		setPosition(position);
		setRotation(rotation.getX(), rotation.getY(), rotation.getZ());
	}
	
	public Transform() {
		this(new Vector3d(), new Vector3d());
	}

	public Vector3d getPosition() {
		return this.position;
	}
	public void translate(Vector3d positionOffsets) {
		position.add(positionOffsets);
		positionMatrix = MatrixBuilder.getTranslationMatrix(position.getX(), position.getY(), position.getZ());
	}
	public void setPosition(Vector3d position) {
		setPosition(position.getX(), position.getY(),position.getZ());
	}
	
	public void setPosition(double x, double y, double z) {
		this.position = new Vector3d(x,y,z);
		this.positionMatrix = MatrixBuilder.getTranslationMatrix(x, y, z);
	}
	
	public Matrix getPositionMatrix() {
		return positionMatrix;
	}
	
	public Vector3d getRotation() {
		return rotation;
	}
	
	public Matrix getRotationMatrix() {
		return rotationMatrix;
	}
	
	public Matrix getTransformationMatrix() {
		return Matrix.MultiplyMatrix(rotationMatrix, positionMatrix);
	}
	
	public void setRotation(double xRadians, double yRadians, double zRadians) {
		this.rotation = new Vector3d(xRadians, yRadians, zRadians);
		this.rotationMatrix = MatrixBuilder.rotationMatrix(xRadians, yRadians, zRadians);
	}

	public void setRotation(Vector3d v3d) {
		setRotation(v3d.getX(), v3d.getY(), v3d.getZ());
	}

	public void translate(double xOffset, double yOffset, double zOffset) {
		translate(new Vector3d(xOffset, yOffset, zOffset));
	}

	public void rotateByDegree(Vector3d rotationDegreeOffsets) {
		Vector3d rotationDegreeOffsetsConv = rotationDegreeOffsets.copy();
		rotationDegreeOffsetsConv.setX(Math.toRadians(rotationDegreeOffsets.getX()));
		rotationDegreeOffsetsConv.setY(Math.toRadians(rotationDegreeOffsets.getY()));
		rotationDegreeOffsetsConv.setZ(Math.toRadians(rotationDegreeOffsets.getZ()));
		
		this.rotation.add(rotationDegreeOffsetsConv);
		this.rotationMatrix = MatrixBuilder.rotationMatrix(rotation.getX(), rotation.getY(), rotation.getZ());
		
	}
	public void rotateByDegree(double xDegreeOffset, double yDegreeOffset, double zDegreeOffset) {
		rotateByDegree(new Vector3d(xDegreeOffset, yDegreeOffset, zDegreeOffset));
		
	}
	
}