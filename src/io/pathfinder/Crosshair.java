package io.pathfinder;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Robot;

public class Crosshair{
	
	private int size = 5;

	private int lastMouseX, lastMouseY = 0;
	private Robot robot;
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public Crosshair(int screenCenterX, int screenCenterY) {
		try {
			robot = new Robot();
			robot.mouseMove(screenCenterX, screenCenterY);
			lastMouseX = screenCenterX;
			lastMouseY = screenCenterY;
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics graphics, int centerX, int centerY) {
		graphics.setColor(Color.RED);
		graphics.drawLine(centerX - size, centerY, centerX + size, centerY);
		graphics.drawLine(centerX , centerY - size, centerX , centerY + size);
		//graphics.drawOval(centerX - size, centerY - size, size * 2, size * 2);
	}
	
	public void update(int screenCenterX, int screenCenterY, Camera camera) {

		int x = MouseInfo.getPointerInfo().getLocation().x;
		int y = MouseInfo.getPointerInfo().getLocation().y;
				
		int dx = x - lastMouseX;
		int dy = y - lastMouseY;
		
		camera.rotateByMouse(dx, dy);
				
		lastMouseX = x;
		lastMouseY = y;

		if(Math.abs(dx) > 1 || Math.abs(dy) > 1) {
			robot.mouseMove(screenCenterX, screenCenterY);
			lastMouseX = screenCenterX;
			lastMouseY = screenCenterY;
		}

	}
}
