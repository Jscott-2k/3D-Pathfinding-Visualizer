package io.pathfinder.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

/**
 * The InputManager contains the implementations for all keyEvents and mouseEvents
 * Performs callbacks to stored KeyInputEvent and MouseInputEvents for handling each event.
 * @author Justin Scott
 *
 */
public class InputManager implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener {

	
	/**
	 * Keys can have multiple states depending on the event. State tells which key event to execute in the InputHandler
	 */
	private HashMap<Integer, KeyState> keyStateMap;

	private MouseInputEvent onMove;
	private MouseInputEvent onLeftPress;
	private MouseInputEvent onLeftRelease;
	private MouseInputEvent onRightPress;
	private MouseInputEvent onRightRelease;
	private MouseInputEvent onScroll;
	private MouseInputEvent onMouseWheelMoveDown;
	private MouseInputEvent onMouseWheelMoveUp;

	public void setOnMouseWheelMoveDown(MouseInputEvent onMouseWheelMoveDown) {
		this.onMouseWheelMoveDown = onMouseWheelMoveDown;
	}

	public void setOnMouseWheelMoveUp(MouseInputEvent onMouseWheelMoveUp) {
		this.onMouseWheelMoveUp = onMouseWheelMoveUp;
	}

	public InputManager() {
		this.keyStateMap = new HashMap<Integer, KeyState>();
	}

	public HashMap<Integer, KeyState> getKeyStateMap() {
		return keyStateMap;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(e.isControlDown()) {
			return;
		}
		
		keyStateMap.put(keyCode, KeyState.PRESS);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keyStateMap.get(keyCode)!=KeyState.PRESS) {
			return;
		}
		
		keyStateMap.put(keyCode, KeyState.RELEASE);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.onMove.onEvent(e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() < 0 ) {
			onMouseWheelMoveDown.onEvent(e);
		}else {
			onMouseWheelMoveUp.onEvent(e);
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {

//		switch(e.getButton() ) {
//			case MouseEvent.BUTTON1:
//				this.onLeftPress.onEvent(e);
//				break;
//			case MouseEvent.BUTTON3:
//				this.onRightPress.onEvent(e);
//				break;
//			default:
//				break;
//		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			this.onLeftPress.onEvent(e);
			break;
		case MouseEvent.BUTTON3:
			this.onRightPress.onEvent(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			this.onLeftRelease.onEvent(e);
			break;
		case MouseEvent.BUTTON3:
			this.onRightRelease.onEvent(e);
			break;
		default:
			break;
		}
	}

	public MouseInputEvent getOnMove() {
		return onMove;
	}

	public void setOnMove(MouseInputEvent onMove) {
		this.onMove = onMove;
	}

	public MouseInputEvent getOnLeftPress() {
		return onLeftPress;
	}

	public void setOnLeftPress(MouseInputEvent onLeftPress) {
		this.onLeftPress = onLeftPress;
	}

	public MouseInputEvent getOnLeftRelease() {
		return onLeftRelease;
	}

	public void setOnLeftRelease(MouseInputEvent onLeftRelease) {
		this.onLeftRelease = onLeftRelease;
	}

	public MouseInputEvent getOnRightPress() {
		return onRightPress;
	}

	public void setOnRightPress(MouseInputEvent onRightPress) {
		this.onRightPress = onRightPress;
	}

	public MouseInputEvent getOnRightRelease() {
		return onRightRelease;
	}

	public void setOnRightRelease(MouseInputEvent onRightRelease) {
		this.onRightRelease = onRightRelease;
	}

	public MouseInputEvent getOnScroll() {
		return onScroll;
	}

	public void setOnScroll(MouseInputEvent onScroll) {
		this.onScroll = onScroll;
	}

	public void setKeyDownMap(HashMap<Integer, KeyState> keyStateMap) {
		this.keyStateMap = keyStateMap;
	}

}
