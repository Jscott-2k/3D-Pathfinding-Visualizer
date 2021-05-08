package io.pathfinder;

/**
 * Singleton data class which stores information regarding the display.
 * @author Justin Scott
 *
 */
public class ScreenConfiguration {
	
	private int width;
	private int height;
	private int targetFPS = 60; 	
	private String title;
	
	private static ScreenConfiguration defaultConfiguration;
	
	public ScreenConfiguration(int width, int height, int targetFPS, String title) {
		
		this.width = width;
		this.height = height;
		this.targetFPS = targetFPS;
		this.title = title;
	}
	public static ScreenConfiguration getDefaultConfiguration() {
		
		if(defaultConfiguration==null) {
			defaultConfiguration = new ScreenConfiguration(1200, 900, 60, "3D Pathfinding Visualizer");
		}
		
		return defaultConfiguration;
	}
	public int getTargetFPS() {
		return targetFPS;
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public String getTitle() {
		return title;
	}
}