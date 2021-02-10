package io.pathfinder;

import java.awt.Color;
import java.awt.Graphics;

public class EndCrystal {
	
	private Mesh testCube1, testCube2, testCube3;
	
	public EndCrystal(double x, double y, double z) {
		testCube1 = new Cube(Color.WHITE, 12);
		testCube1.translate(x, y, z);
		
		testCube2 = new Cube(Color.GRAY, 7);
		testCube2.translate(x, y, z);
		
		testCube3 = new Cube(Color.MAGENTA, 3);
		testCube3.translate(x, y, z);
	}
	
	public void update() {
		testCube1.rotate(0,0,0);
		testCube2.rotate(3,0,3);
		testCube3.rotate(5,5,5);
	}
	
	public void render(Graphics graphics, Camera camera) {
		testCube3.render(graphics, camera);
		testCube2.render(graphics, camera);
		testCube1.render(graphics, camera);
	}
}
