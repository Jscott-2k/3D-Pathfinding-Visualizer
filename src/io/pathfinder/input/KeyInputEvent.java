package io.pathfinder.input;

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
