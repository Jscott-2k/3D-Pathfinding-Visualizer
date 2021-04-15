package io.pathfinder;

import java.awt.Color;
import java.awt.Font;


public class Text {
	
	private Font font;
	private String name;
	private int size;
	private int weight;
	private String text;
	private Color color;
	private int x;
	private int y;
	
	public Text(String name, int weight, int size, String text, Color color, int x, int y) {
		this.weight = weight;
		this.size = size;
		this.name = name;
		this.text = text;
		this.color = color;
		this.x = x;
		this.y = y;
		this.font = new Font(name, weight, size);
	}
		
	public Text(Font font, String text, Color color, int x, int y) {
		this.font = font;
		this.weight = font.getStyle();
		this.size = font.getSize();
		this.name = font.getName();
		this.color = color;
		this.text = text;
		this.x = x;
		this.y = y;
		
		
	}
		
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Font getFont() {
		return font;
	}

}
