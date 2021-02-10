package io.pathfinder.input;

import java.util.HashMap;

public class InputHandler{
	
	private InputManager manager;
	
	private HashMap<Integer, KeyInputEvent> keyEventMap;
	
	public InputHandler() {
		manager = new InputManager();
		keyEventMap = new HashMap<Integer, KeyInputEvent>();
	}
	
	public InputManager getManager() {
		return manager;
	}

	public void addEvent(KeyInputEvent event){
		keyEventMap.put(event.getKeyCode(), event);
	}
	
	public void handle() {
		manager.getKeyDownMap().forEach((key, value) -> {
			if(value) {
				KeyInputEvent event = keyEventMap.get(key);
				if(event!=null) {
					keyEventMap.get(key).onKeyDown();
				}
			}
		});
	}
}