package io.pathfinder.astar;


public class NodeScore {
	private double g = 0;
	private double f = 0;
	
	public NodeScore() {
		this.g=0;
		this.f=0;
	}
	
	public double getF() {
		return f;
	}
	public double getG() {
		return g;
	}

	public void setF(double f) {
		this.f = f;
	}
	public void setG(double g) {
		this.g = g;
	}
}
