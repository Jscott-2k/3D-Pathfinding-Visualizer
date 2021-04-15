package io.pathfinder.input;

import java.util.ArrayList;
import java.util.HashMap;

public class InputHandler{
	
	private InputManager manager;
	
	private HashMap<Integer, KeyInputEvent> keyEventMap;
	private ArrayList<Integer> resetKeyState;
	
	public InputHandler() {
		manager = new InputManager();
		keyEventMap = new HashMap<Integer, KeyInputEvent>();
		resetKeyState = new ArrayList<Integer>();
		resetKeyState.ensureCapacity(200);
	}
	
	public InputManager getManager() {
		return manager;
	}

	public void addEvent(KeyInputEvent event){
		keyEventMap.put(event.getKeyCode(), event);
	}
	
	public void handle() {
		
		HashMap<Integer, KeyState> keyStateMap = manager.getKeyStateMap();
		keyStateMap.forEach((key, value) -> {
			
			
				KeyInputEvent event = keyEventMap.get(key);
				if(event!=null) {
		
					switch(value) {
						case PRESS:
							event.onKeyDown();
							break;
						case RELEASE:
							event.onKeyRelease();
							resetKeyState.add(key);
							break;
						default:
							break;
					}
					
				}
		});
	
		resetKeyState.forEach((key)->{
			keyStateMap.put(key, KeyState.NONE);
		});
		
		resetKeyState.clear();
	}
}