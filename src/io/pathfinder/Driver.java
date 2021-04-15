package io.pathfinder;

public class Driver {
	
	private static Driver driver;
	private static Screen screen;
	private Thread screenThread;
	private boolean running;
	
	
	public Thread getScreenThread() {
		return screenThread;
	}
	
	public static Driver getDriver() {
		
		if(driver==null) {
			driver = new Driver();
		}
		return driver;
	}
	
	private Driver() {}
	
	public static void main(String[] args) {
		driver = Driver.getDriver();
		driver.init();
		driver.start();
		driver.appLoop();

	}
	
	private void init() {
		System.out.println("Driver Init");
		screen = Screen.getScreen();
		screenThread = new Thread(screen);
	}
	
	private void start() {
		System.out.println("Driver Start");
		running = true;
		screenThread.start();
		System.out.println("Driver Ended");
	}
	
	private void appLoop() {
		System.out.println("App Loop Start");
	}

	public Screen getScreen() {
		return screen;
	}

	public Object getScreenLock() {
		return screen.getLock();
	}	
}