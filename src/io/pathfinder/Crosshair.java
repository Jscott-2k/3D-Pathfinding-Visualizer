package io.pathfinder;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * Class for the crosshair. Replaces the mouse and serves to reference view
 * position (forward) for the camera
 * 
 * @author Justin Scott
 */
public class Crosshair {

	private int size = 5;

	private int lastMouseX, lastMouseY = 0;
	private Robot robot;

	private boolean enabled;

	private static Cursor invisibleCursor;

	/**
	 * Gets the size
	 * 
	 * @return
	 */
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * 
	 * Initializes the robot object and setting cursor Constructs the crosshair
	 * object
	 * 
	 * @param screenCenterX - the center x position of the display device
	 * @param screenCenterY - the center y position of the display device
	 * @param size          - size of the cursor
	 */
	public Crosshair(int screenCenterX, int screenCenterY, int size, Toolkit frameToolkit) {
		this.size = size;
		this.enabled = true;

		int adjustedScreenCenterX = (int) (screenCenterX + (size / 2.0));
		int adjustedScreenCenterY = (int) (screenCenterY - (size / 2.0));

		
		if (invisibleCursor == null) {
			invisibleCursor = frameToolkit.createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
					new Point(0, 0), "null");
		}

		try {
			robot = new Robot();
			
			robot.mouseMove(adjustedScreenCenterX, adjustedScreenCenterY);
			lastMouseX = adjustedScreenCenterX;
			lastMouseY = adjustedScreenCenterY;
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public Crosshair(int screenCenterX, int screenCenterY, Toolkit frameToolkit) {
		this(screenCenterX, screenCenterY, 12, frameToolkit);
	}

	public void render(Graphics graphics, int centerX, int centerY) {

		if (!enabled) {
			return;
		}

		graphics.setColor(Color.RED);
		graphics.drawLine(centerX - size, centerY, centerX + size, centerY);
		graphics.drawLine(centerX, centerY - size, centerX, centerY + size);
	}

	/**
	 * 
	 * @param screenCenterX
	 * @param screenCenterY
	 * @param camera
	 */
	public void update(int screenCenterX, int screenCenterY, Camera camera) {

		if (!enabled) {
			return;
		}

		int x = MouseInfo.getPointerInfo().getLocation().x;
		int y = MouseInfo.getPointerInfo().getLocation().y;

		int dx = x - lastMouseX;
		int dy = y - lastMouseY;

		camera.rotateByMouse(dx, dy);

		lastMouseX = x;
		lastMouseY = y;

		int adjustedScreenCenterX = (int) (screenCenterX + (size / 2.0));
		int adjustedScreenCenterY = (int) (screenCenterY - (size / 2.0));

		if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
			robot.mouseMove(adjustedScreenCenterX, adjustedScreenCenterY);
			lastMouseX = adjustedScreenCenterX;
			lastMouseY = adjustedScreenCenterY;
		}

	}

	public void toggle(Screen screen) {
		enabled = !enabled;

		if (enabled) {
			screen.setCursor(invisibleCursor);
		} else {
			screen.setCursor(Cursor.getDefaultCursor());
		}

	}

	public boolean isEnabled() {
		return enabled;
	}
}
