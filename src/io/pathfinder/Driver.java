package io.pathfinder;

public class Driver {
	
	private static Driver driver;
	private static Screen display;
	private Thread displayThread;
	private boolean running;
	
	public static void main(String[] args) {
		driver = new Driver();
		driver.init();
		driver.start();
		driver.appLoop();

	}
	
	private void init() {
		//System.out.println("Driver Init");
		display = new Screen();
		displayThread = new Thread(display);
	}
	
	private void start() {
		//System.out.println("Driver Start");
		running = true;
		displayThread.start();
		//System.out.println("Driver Ended");
	}
	
	private void appLoop() {
		//System.out.println("App Loop Start");
	}
	
}