package io.pathfinder.math;

/**
 * Abstract class for vector. 
 * All vectors requires a copy and toMatrix conversion implementation.
 * 
 * @author Justin Scott
 */
public abstract class Vector{
	protected abstract Matrix toMatrix();
	protected abstract Vector copy();
}