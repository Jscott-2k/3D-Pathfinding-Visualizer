package io.pathfinder;

import java.awt.event.KeyEvent;

import io.pathfinder.input.KeyInputEvent;
import io.pathfinder.input.InputHandler;
import io.pathfinder.math.Matrix;
import io.pathfinder.math.MatrixBuilder;
import io.pathfinder.math.Vector2d;
import io.pathfinder.math.Vector3d;
import io.pathfinder.math.Vector4d;

/**
 * A camera responsible for converting points to a 3D space to coordinates which can be displayed on the screen
 * Contains a transform object allowing it to act as an object in 3D space which can move around and rotate
 * There are 3 matrices fields that are used to calculate the displayed coordinates. (Projection, Camera, and View Matrix)
 * 
 * @author Justin Scott
 *
 */
public class Camera {
	
	private double near, far, fieldOfView, aspectRatio, fieldOfViewAngle;
	private ScreenConfiguration screenConfiguration;
	private Transform transform;
	
	private Matrix projectionMatrix;
	private Matrix cameraMatrix;
	private Matrix viewMatrix;
	
	private final Vector3d vUp = new Vector3d(0,1,0);

	private Vector3d vTarget;
	private double moveSpeed;
	private Vector3d vLookDir;
	
	private Vector3d nForward;
	private Vector3d vForward;
	private Vector3d nUp;
	private Vector3d nRight;
	
	private boolean splitScreen = false;
	private int splitScreenXMod = 1;
	
	private double sensitivity = 5;
		
	public Camera(double moveSpeed, double near, double far, double fieldOfView, ScreenConfiguration screenConfiguration) {
		transform = new Transform();
		this.moveSpeed = moveSpeed;
		this.screenConfiguration = screenConfiguration;
		
		//setSplitScreen(false);
		loadProjectionMatrix(near,far, fieldOfView, screenConfiguration);
	}

	/**
	 * Initializes the camera for the perspective projection and creates the projection matrix 
	 * @param near
	 * @param far
	 * @param fieldOfView
	 * @param screenConfiguration
	 */
	public void loadProjectionMatrix(double near, double far, double fieldOfView, ScreenConfiguration screenConfiguration) {
		
		this.screenConfiguration = screenConfiguration;
		this.near = near;
		this.far = far;
		this.fieldOfView = fieldOfView;
		
		this.aspectRatio = (double)screenConfiguration.getHeight() / (double)screenConfiguration.getWidth() * splitScreenXMod;
		fieldOfViewAngle = 1 / Math.tan( Math.toRadians(fieldOfView/2)); 
		double aspectFovA = 1 / (aspectRatio * Math.tan( Math.toRadians(fieldOfView/2)));
		projectionMatrix = MatrixBuilder.getProjectionMatrix(aspectRatio, fieldOfViewAngle, aspectFovA,far, near);	
	}
	
	/**
	 * Adjust projection matrix for splitscreen.
	 * @param splitScreen
	 */
	public void setSplitScreen(boolean splitScreen) {
		this.splitScreen = splitScreen;
		this.splitScreenXMod = splitScreen ? 2 : 1;
		loadProjectionMatrix(near, far, fieldOfView, screenConfiguration);
	}
	
	public void update() {
		createView();
	}
	
	/**
	 * Creates the look at matrix and calculates the forward vector of the camera given a target and up vector.
	 * @param position
	 * @param target
	 * @param up
	 * @return
	 */
	public Matrix lookAt(Vector3d position, Vector3d target, Vector3d up) {
		
		nForward = Vector3d.subtract(target,position);
		nRight = Vector3d.cross(up, nForward);
		nUp = nRight.cross(nForward);
		
		vForward = nForward.copy();
		
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

	/**
	 * Create the camera view matrix using the inverse of the lookAt (camera) matrix
	 * @return
	 */
	public Matrix createView() {
		
	
		vTarget = new Vector3d(0,0,1);
				
		vLookDir = new Vector3d(
				Vector4d.BuildFromMatrix(
						Matrix.MultiplyMatrixVector(transform.getRotationMatrix(), new Vector4d(vTarget,0))));
		
		vTarget = Vector3d.add(transform.getPosition(), vLookDir);
		
		cameraMatrix = lookAt(transform.getPosition(), vTarget, vUp);

		this.viewMatrix = cameraMatrix.getInverse();
		
		//System.out.println(viewMatrix);
		
		return viewMatrix;
	
	}
	
	public Vector4d getProjection(Matrix mvm,Vector3d v3d) {
		return getProjection(mvm, new Vector4d(v3d, 1));
	}
	
	/**
	 * Gets the projected x,y,z of a point given the model view matrix. 
	 * @param mvm
	 * @param v4d
	 * @return
	 */
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
		
		// Scale normalized points back up
		x *= (double) screenConfiguration.getWidth() / (2.0 * splitScreenXMod);
		y *= (double) screenConfiguration.getHeight() / 2.0;
		
		x += (splitScreenXMod - 1 ) * (screenConfiguration.getWidth() / 2.0);
	
		x += (double) screenConfiguration.getWidth() / (2.0 * splitScreenXMod);
		y += (double) screenConfiguration.getHeight() / 2.0;
		
		return (new Vector4d(x, y, z, w));
	}
	
	public boolean isSplitScreen() {
		return this.splitScreen;
	}
	
	public void addInputEvents(InputHandler inputHandler) {
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_W) {
			@Override
			public void onKeyDown() {
				Vector3d f = Vector3d.multiply(nForward, moveSpeed);
				transform.translate(f);
			}

			@Override
			public void onKeyRelease() {
				// TODO Auto-generated method stub
				
			}
		});
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_S) {
			@Override
			public void onKeyDown() {
				Vector3d f = Vector3d.multiply(nForward, -moveSpeed);
				transform.translate(f);
			}

			@Override
			public void onKeyRelease() {
				// TODO Auto-generated method stub
				
			}
		});
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_A) {
			@Override
			public void onKeyDown() {
				Vector3d f = Vector3d.multiply(nRight, -moveSpeed);
				transform.translate(f);
				
			}

			@Override
			public void onKeyRelease() {
				// TODO Auto-generated method stub
				
			}
		});
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_SHIFT) {
			@Override
			public void onKeyDown() {
//				Vector3d f = Vector3d.multiply(nUp, moveSpeed);
//				transform.translate(f);
				
				transform.translate(0,-moveSpeed,0);
			}

			@Override
			public void onKeyRelease() {
				// TODO Auto-generated method stub
				
			}
		});
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_SPACE) {
			@Override
			public void onKeyDown() {
//				Vector3d f = Vector3d.multiply(nUp, -moveSpeed);
//				transform.translate(f);
				
				transform.translate(0,moveSpeed,0);
			}

			@Override
			public void onKeyRelease() {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_D) {
			@Override
			public void onKeyDown() {
				Vector3d f = Vector3d.multiply(nRight, moveSpeed);
				transform.translate(f);	
			}

			@Override
			public void onKeyRelease() {
				// TODO Auto-generated method stub
				
			}
		});
		

	}
	
	public Matrix getView() {
		return viewMatrix;
	}

	public void rotateByMouse(double dx, double dy) {
		transform.rotateByDegree(dy / sensitivity, dx / sensitivity, 0);
	}

	public Matrix getProjectionMatrix() {
		return this.projectionMatrix;
	}

	public String getPositionAsString() {
		return transform.getPosition().toString();
	}
	public Vector3d getNormForward() {
		return nForward;
	}
	public boolean needCull(Vector3d v0, Vector3d surfaceNormal) {
		Vector3d vCameraRay = Vector3d.subtract( v0,vForward);
		//return false;
		return Vector3d.dot(surfaceNormal,vCameraRay) < 0 ;
	}

	public Vector3d getPosition() {
		return transform.getPosition();
	}

	public ScreenConfiguration getScreenConfiguration() {
		return screenConfiguration;
	}

	public double getNear() {
		return this.near;
	}

	public double getFar() {
		// TODO Auto-generated method stub
		return this.far;
	}
}