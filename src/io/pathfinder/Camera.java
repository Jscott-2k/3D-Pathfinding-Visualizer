package io.pathfinder;

import java.awt.event.KeyEvent;

import io.pathfinder.input.KeyInputEvent;
import io.pathfinder.input.InputHandler;
import io.pathfinder.math.Matrix;
import io.pathfinder.math.MatrixBuilder;
import io.pathfinder.math.Vector2d;
import io.pathfinder.math.Vector3d;
import io.pathfinder.math.Vector4d;

public class Camera {
	
	private double near, far, fieldOfView, aspectRatio, fieldOfViewAngle;
	private Transform transform;
	private Matrix projectionMatrix;
	private Matrix cameraMatrix;
	private Matrix viewMatrix;
	
	private final Vector3d vUp = new Vector3d(0,1,0);

	private Vector3d vTarget;
	private double moveSpeed;
	private Vector3d vLookDir;
	private Vector3d nForward;
	private Vector3d nUp;
	private Vector3d nRight;
	
	public Camera() {
		
		moveSpeed = 1;
		transform = new Transform();
		near = .1;
		far = 1000.0;
		fieldOfView = 120.0;
		aspectRatio = (double) Screen.HEIGHT / (double) Screen.WIDTH;
		fieldOfViewAngle = 1 / Math.tan( Math.toRadians(fieldOfView/2)); 
		double aspectFovA = 1 / (aspectRatio * Math.tan( Math.toRadians(fieldOfView/2)));
		
		projectionMatrix = MatrixBuilder.getProjectionMatrix(aspectRatio, fieldOfViewAngle, aspectFovA,far, near);		
	}

	public void update() {
		createView();
	}
	
	public Matrix lookAt(Vector3d position, Vector3d target, Vector3d up) {
		
		
		nForward = Vector3d.subtract(target,position);
		Vector3d a = Vector3d.multiply(nForward,Vector3d.dot(up, nForward));
		nRight = Vector3d.cross(up, nForward);
		nUp = nRight.cross(nForward);
		
		
		nForward.normalize();
		nUp.normalize();
		nRight.normalize();
		
		
		Matrix m = new Matrix(4,4);
		
		m.set(0, 0, nRight.getX()); 
		m.set(0, 1, nUp.getX());
		m.set(0, 2, nForward.getX());
		m.set(0, 3, position.getX()); 
		
		m.set(1, 0, nRight.getY()); 
		m.set(1, 1, nUp.getY());
		m.set(1, 2, nForward.getY()); 
		m.set(1, 3, position.getY());
		
		m.set(2, 0, nRight.getZ());
		m.set(2, 1, nUp.getZ()); 
		m.set(2, 2, nForward.getZ());
		m.set(2, 3, position.getZ());
		
		
		m.set(3, 3, 1);
		
		
		
		
		return m;
		
	}

	public Matrix createView() {
		
		
		//vLookDirection = Vector4d.BuildFromMatrix(Matrix.MultiplyMatrixVector(transform.getRotationMatrix(),new Vector4d(vTarget,1)));
	
		//vTarget = Vector3d.add(transform.getPosition(), new Vector3d(vLookDirection));
		vTarget = new Vector3d(0,0,1);
				
		vLookDir = new Vector3d(
				Vector4d.BuildFromMatrix(
						Matrix.MultiplyMatrixVector(transform.getRotationMatrix(), new Vector4d(vTarget,0))));
		vTarget = Vector3d.add(transform.getPosition(), vLookDir);
		
		cameraMatrix = lookAt(transform.getPosition(),vTarget, vUp);
		//this.viewMatrix = Matrix.QuickInverse(cameraMatrix);
		this.viewMatrix = cameraMatrix.getInverse();
		
//		System.out.println("Quick Inverse");
//		System.out.println(Matrix.QuickInverse(cameraMatrix));
//		System.out.println("Normal Inverse");
//		System.out.println(viewMatrix);
		
		return viewMatrix;
	
	}
	
	public Vector4d getProjection(Matrix mvm,Vector3d v3d) {
		return getProjection(mvm, new Vector4d(v3d, 1));
	}
	public Vector4d getProjection(Matrix mvm, Vector4d v4d) {
		
		Matrix projectionResult = Matrix.MultiplyMatrixVector(Matrix.MultiplyMatrix(mvm,projectionMatrix), v4d);
		
		Vector4d projectedResultVector = Vector4d.BuildFromMatrix(projectionResult);
		
		double x = projectedResultVector.getX();
		double y = projectedResultVector.getY();
		double z = projectedResultVector.getZ();
		double w = projectedResultVector.getW();
		
		if(projectedResultVector.getW()!=0) {
			x /= w;
			y /= w;

		}

		x *= (double) Screen.WIDTH / 2.0;
		y *= (double) Screen.HEIGHT / 2.0;
		
		x += (double) Screen.WIDTH / 2.0;
		y += (double) Screen.HEIGHT / 2.0;
		
		return (new Vector4d(x, y, z, w));
	}
	
	public void addInputEvents(InputHandler inputHandler) {
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_W) {
			@Override
			public void onKeyDown() {
				Vector3d f = Vector3d.multiply(nForward, moveSpeed);
				transform.translate(f);
			}
		});
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_S) {
			@Override
			public void onKeyDown() {
				Vector3d f = Vector3d.multiply(nForward, -moveSpeed);
				transform.translate(f);
			}
		});
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_A) {
			@Override
			public void onKeyDown() {
				Vector3d f = Vector3d.multiply(nRight, -moveSpeed);
				transform.translate(f);
				
			}
		});
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_SHIFT) {
			@Override
			public void onKeyDown() {
//				Vector3d f = Vector3d.multiply(nUp, moveSpeed);
//				transform.translate(f);
				
				transform.translate(0,-moveSpeed,0);
			}
		});
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_SPACE) {
			@Override
			public void onKeyDown() {
//				Vector3d f = Vector3d.multiply(nUp, -moveSpeed);
//				transform.translate(f);
				
				transform.translate(0,moveSpeed,0);
			}
		});
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_A) {
			@Override
			public void onKeyDown() {
				Vector3d f = Vector3d.multiply(nRight, -moveSpeed);
				transform.translate(f);
				
			}
		});
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_D) {
			@Override
			public void onKeyDown() {
				Vector3d f = Vector3d.multiply(nRight, moveSpeed);
				transform.translate(f);	
			}
		});
		
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_Q) {
			@Override
			public void onKeyDown() {
				transform.rotateByDegree(moveSpeed,0,0);
			}
		});
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_E) {
			@Override
			public void onKeyDown() {
				transform.rotateByDegree(-moveSpeed,0,0);
			}
		});
	}
	
	public Matrix getView() {
		return viewMatrix;
	}

	public void rotate(double dx, double dy) {
		transform.rotateByDegree(-dy / 2, dx / 2, 0);
	}

	public Matrix getProjectionMatrix() {
		return this.projectionMatrix;
	}

	public String getPosition() {
		return transform.getPosition().toString();
	}
}