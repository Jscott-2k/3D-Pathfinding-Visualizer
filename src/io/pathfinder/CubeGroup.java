package io.pathfinder;

import java.awt.Color;
import java.awt.Graphics;

public class CubeGroup {
	
	private Mesh testCube1, testCube2, testCube3;
	
	public CubeGroup(double x, double y, double z) {
//		testCube1 = new Cube(Color.DARK_GRAY, 6);
//		testCube1.translate(x, y, z);
		
//		testCube2 = new Cube(Color.GRAY, 7);
//		testCube2.translate(x, y, z);
		
		testCube3 = new Cube(Color.BLACK, 3);
		testCube3.translate(x, y, z);
	}
	
	public void update() {
//		testCube1.rotate(0,0,0);
	//	testCube2.rotate(1,0,1);
		testCube3.rotate(2,2,2);
	}
	
	public void render(Graphics graphics, Camera camera) {
		testCube3.render(graphics, camera);
		//testCube2.render(graphics, camera);
//		testCube1.render(graphics, camera);
	}
}
