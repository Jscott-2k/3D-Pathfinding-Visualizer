package io.pathfinder;

import java.awt.Graphics;
import java.util.ArrayList;

public class TextRenderer {
	
	private ArrayList<Text> texts;
	
	public TextRenderer(int capacity) {
		texts = new ArrayList<Text>();
		texts.ensureCapacity(32);
	}
	
	public void render(Graphics g) {
		
		for(Text text : texts) {
			g.setFont(text.getFont());
			g.setColor(text.getColor());
			g.drawString(text.getText(), text.getX(), text.getY());
		}
	
	}
	public void add(Text text) {
		if(!texts.contains(text)) {
			texts.add(text);
		}
	}
	
	public void clear() {
		texts.clear();
	}
	public Text getTextObject(int index) {
		return texts.get(index);
	}

	public void setAllX(int x) {
		for(Text text : texts) {
			text.setX(x);
		}
	}
}
