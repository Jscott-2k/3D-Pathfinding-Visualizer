package io.pathfinder.input;

public abstract class KeyInputEvent {
	protected int keyCode;
	public KeyInputEvent(int keyCode) {
		this.keyCode = keyCode;
	}
	public abstract void onKeyDown();
	public Integer getKeyCode() {
		return keyCode;
	}
}
