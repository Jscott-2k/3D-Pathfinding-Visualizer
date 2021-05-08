package io.pathfinder.input;

/**
 * For custom implementation of key events to be handled by InputHandler.
 * Requires the keyCode and the two implementation of onKeyDown and onKeyRelease()
 * @author Justin Scott
 */
public abstract class KeyInputEvent {
	
	protected int keyCode;
	
	public KeyInputEvent(int keyCode) {
		this.keyCode = keyCode;
	}
	
	public Integer getKeyCode() {
		return keyCode;
	}
	
	protected abstract void onKeyDown();
	protected abstract void onKeyRelease();
	

}
