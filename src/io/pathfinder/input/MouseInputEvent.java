package io.pathfinder.input;


import java.awt.event.MouseEvent;

/**
 * This could have also been an abstract class similar to KeyInputEvent.
 * Was testing out how functional interfaces work. 
 * @author Justin Scott
 *
 */
@FunctionalInterface
public interface MouseInputEvent{
	public void onEvent(MouseEvent event);
}