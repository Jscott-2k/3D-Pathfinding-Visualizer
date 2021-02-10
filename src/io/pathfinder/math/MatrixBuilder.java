package io.pathfinder.math;

final public class MatrixBuilder {
	
	private MatrixBuilder() {}

	private static Matrix getXRotationMatrix(double radians) {
		Matrix rotationMatrix = new Matrix(4);
		rotationMatrix.set(0, 0, 1);
		rotationMatrix.set(1, 1, Math.cos(radians));
		rotationMatrix.set(1, 2, Math.sin(radians));
		rotationMatrix.set(2, 1, -Math.sin(radians));
		rotationMatrix.set(2, 2, Math.cos(radians));
		rotationMatrix.set(3, 3, 1);
		return rotationMatrix; 
	}
	
	
	private static Matrix getYRotationMatrix(double radians) {
		Matrix rotationMatrix = new Matrix(4);
		rotationMatrix.set(0, 0, Math.cos(radians));
		rotationMatrix.set(0, 2, -Math.sin(radians));
		rotationMatrix.set(1, 1, 1);
		rotationMatrix.set(2, 0, Math.sin(radians));
		rotationMatrix.set(2, 2, Math.cos(radians));
		rotationMatrix.set(3, 3, 1);
		return rotationMatrix;
		
	}
	private static Matrix getZRotationMatrix(double radians) {
		Matrix rotationMatrix = new Matrix(4);
		rotationMatrix.set(0, 0, Math.cos(radians));
		rotationMatrix.set(0, 1, -Math.sin(radians));
		rotationMatrix.set(1, 0, Math.sin(radians));
		rotationMatrix.set(1, 1, Math.cos(radians));
		rotationMatrix.set(2, 2, 1);
		rotationMatrix.set(3, 3, 1);
		return rotationMatrix;
	}
	
	public static Matrix rotationMatrix(double xRadians, double yRadians, double zRadians) {
		Matrix xRotationMatrix = getXRotationMatrix(xRadians);
		Matrix yRotationMatrix = getYRotationMatrix(yRadians);
		Matrix zRotationMatrix = getZRotationMatrix(zRadians);
		return xRotationMatrix.
				multiplyMatrix(yRotationMatrix.multiplyMatrix(zRotationMatrix));	
	}
	
	public static Matrix getProjectionMatrix(double aspectRatio, double fieldOfViewAngle, double aspectFovA, double far, double near) {
		
		Matrix projectionMatrix = new Matrix(4);
				
		projectionMatrix.set(0, 0, aspectRatio * fieldOfViewAngle);
		projectionMatrix.set(1, 1, fieldOfViewAngle);
		projectionMatrix.set(2, 2, far / (far - near));
		projectionMatrix.set(3, 2, (far * near) / (far - near));
		projectionMatrix.set(2, 3, 1);
		
//		projectionMatrix.set(0, 0, aspectRatio * fieldOfViewAngle);
//		projectionMatrix.set(1, 1, fieldOfViewAngle);
//		projectionMatrix.set(2, 2,  far / (far - near));
//		projectionMatrix.set(3, 2, 1);
//		projectionMatrix.set(2, 3, -((far*near) / (far - near)));
		
		return projectionMatrix;
		
	}
	
	
	public static Matrix getTranslationMatrix(double xOffset, double yOffset, double zOffset)
	{
		Matrix translationMatrix = new Matrix(4);
		
		translationMatrix.set(0, 0, 1);
		translationMatrix.set(1, 1, 1);
		translationMatrix.set(2, 2, 1);

		translationMatrix.set(3, 0, xOffset);
		translationMatrix.set(3, 1, yOffset);
		translationMatrix.set(3, 2, zOffset);
		translationMatrix.set(3, 3, 1);
		
		return translationMatrix;
	}
	
	
	public static Matrix getIdentityMatrix(int order) {
		Matrix identityMatrix = new Matrix(order);
		for(int i=0;i<order;i++) {
			identityMatrix.set(i, i, 1);
		}
		return identityMatrix;
	}
	
}
