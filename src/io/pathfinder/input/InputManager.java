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

public class InputManager implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener{

	private HashMap<Integer, Boolean> keyDownMap;
	
	
	private MouseInputEvent onMove;
	private MouseInputEvent onLeftPress;
	private MouseInputEvent onLeftRelease;
	private MouseInputEvent onRightPress;
	private MouseInputEvent onRightRelease;
	private MouseInputEvent onScroll;
	
	public InputManager() {
		this.keyDownMap = new HashMap<Integer, Boolean>();
	}
	
	public HashMap<Integer, Boolean> getKeyDownMap() {
		return keyDownMap;
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		keyDownMap.put(keyCode, true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		keyDownMap.put(keyCode, false);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		switch(e.getButton() ) {
			case MouseEvent.BUTTON1:
				this.onLeftPress.onEvent(e);
				break;
			case MouseEvent.BUTTON2:
				this.onRightPress.onEvent(e);
				break;
			default:
				break;
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

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

	public void setKeyDownMap(HashMap<Integer, Boolean> keyDownMap) {
		this.keyDownMap = keyDownMap;
	}


}
