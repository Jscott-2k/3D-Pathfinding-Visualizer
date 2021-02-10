package io.pathfinder;

import java.awt.Color;
import java.awt.Graphics;

public class Crosshair{
	
	private int size = 5;

	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void render(Graphics graphics, int centerX, int centerY) {
		graphics.setColor(Color.RED);
		graphics.drawLine(centerX - size, centerY, centerX + size, centerY);
		graphics.drawLine(centerX , centerY - size, centerX , centerY + size);
		//graphics.drawOval(centerX - size, centerY - size, size * 2, size * 2);
	}
}
