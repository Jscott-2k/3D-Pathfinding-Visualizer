package io.pathfinder.input;


import java.awt.event.MouseEvent;

@FunctionalInterface
public interface MouseInputEvent{
	public void onEvent(MouseEvent event);
}

