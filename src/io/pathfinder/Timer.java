package io.pathfinder;

public class Timer {
	
	private long lastTime = 0;
	private long beginTime = 0;
	private double deltaTime = 0;

	public static long getSystemTime() {
		return System.nanoTime();
	}
	
	public void setBeginTime(long t) {
		beginTime = t;
	}
	
	public void calcDeltaTime() {
		deltaTime = nanoToMilli(lastTime) - nanoToMilli(beginTime);
		lastTime =  System.nanoTime();
	}
	
	public double getDeltaTime() {
		return milliToSecond(deltaTime);
	}
	
	private double milliToSecond(double t) {
		return t / 1000.0;
	}
	private double nanoToMilli(long t) {
		return t / 1000000.0;
	}

	public long getBeginTime() {
		return this.beginTime;
	}
}
