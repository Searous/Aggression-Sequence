package com.game.main;

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.HashMap;
import java.util.Map;

public class Input {
	private Handler handler;
	private KeyEvent keys;
	private MouseEvent mouse;
	private Map buttonDown;
	private int lastPressed = -1;
	public int mouseX,mouseY;
	public int mouseDragDistance;
	public boolean mouseDragged;
	
	public Input(Handler handler) {
		this.handler = handler;
		this.buttonDown = new HashMap();
		this.keys = new KeyEvent(this);
		this.mouse = new MouseEvent(this);
		handler.game.addKeyListener(this.keys);
		handler.game.addMouseListener(this.mouse);
		handler.game.addMouseMotionListener(this.mouse);
	}
	private void addButton(int id) {
		this.buttonDown.put(id, true);
	}
	private void removeButton(int id) {
		this.buttonDown.remove(id);
	}
	
	public boolean isButtonDown(int id) {
		if (this.buttonDown.containsKey(id)) 
			return true;
		else
			return false;
	}
	public int getLastPressed() {
		return lastPressed;
	}
	
	public static String getButtonName(int id) {
		switch(id) {
			case -1: return "PRESS A KEY";
			//Mouse Buttons
			case 1: return "Left-Click";
			case 2: return "Middle-Click";
			case 3: return "Right-Click";
			case 4: return "MB 4";
			case 5: return "MB 5";
			//Alphabet.  QWERTY order
			case 86: return "Q";
			case 92: return "W";
			case 74: return "E";
			case 87: return "R";
			case 89: return "T";
			case 94: return "Y";
			case 90: return "U";
			case 78: return "I";
			case 84: return "O";
			case 85: return "P";
			case 70: return "A";
			case 88: return "S";
			case 73: return "D";
			case 75: return "F";
			case 76: return "G";
			case 77: return "H";
			case 79: return "J";
			case 80: return "K";
			case 81: return "L";
			case 95: return "Z";
			case 93: return "X";
			case 72: return "C";
			case 91: return "V";
			case 71: return "B";
			case 83: return "N";
			case 82: return "M";
			//Numbers and symbols
			case 54: return "1";
			case 55: return "2";
			case 56: return "3";
			case 57: return "4";
			case 58: return "5";
			case 59: return "6";
			case 60: return "7";
			case 61: return "8";
			case 62: return "9";
			case 53: return "0";
			case 197: return "~";
			case 50: return "-";
			case 66: return "=";
			case 96: return "[";
			case 98: return "]";
			case 64: return ";";
			case 227: return "'";
			case 97: return "\\";
			case 49: return ",";
			case 51: return ".";
			case 52: return "/";
			//F keys
			case 117: return "F1";
			case 118: return "F2";
			case 119: return "F3";
			case 120: return "F4";
			case 121: return "F5";
			case 122: return "F6";
			case 123: return "F7";
			case 124: return "F8";
			case 125: return "F9";
			case 126: return "F10";
			case 127: return "F11";
			case 128: return "F12";
			//Numbpad and arrows
			case 43: return "Up Arrow";
			case 42: return "Left Arrow";
			case 45: return "Down Arrow";
			case 44: return "Right Arrow";
			case 101: return "Num 0";
			case 102: return "Num 1";
			case 103: return "Num 2";
			case 104: return "Num 3";
			case 105: return "Num 4";
			case 106: return "Num 5";
			case 107: return "Num 6";
			case 108: return "Num 7";
			case 109: return "Num 8";
			case 110: return "Num 9";
			case 116: return "Num /";
			case 111: return "Num *";
			case 114: return "Num -";
			case 112: return "Num +";
			case 115: return "Num .";
			case 149: return "Num Lock";
			//Misc Keys
			case 32: return "Escape";
			case 25: return "Caps Lock";
			case 21: return "Shift";
			case 22: return "Ctrl";
			case 529: return "Win";
			case 23: return "Alt";
			case 37: return "Space";
			case 530: return "Context";
			case 13: return "Backspaced";
			case 15: return "Enter";
			case 150: return "Scroll Lock";
			case 24: return "Pause";
			case 160: return "Insert";
			case 41: return "Home";
			case 38: return "Page Up";
			case 132: return "Delete";
			case 40: return "End";
			case 39: return "Page Down";
			
			default: return "UKNOWN_"+id;
		}
	}
	
	private class KeyEvent extends KeyAdapter {
		private Input input;
		public KeyEvent(Input input) {
			this.input = input;
		}
		
		public void keyPressed(java.awt.event.KeyEvent e) {
			input.addButton(e.getKeyCode() + 5);
			input.lastPressed = e.getKeyCode() + 5;
		}
		public void keyReleased(java.awt.event.KeyEvent e) {
			input.removeButton(e.getKeyCode() + 5);
			input.lastPressed = -1;
		}
	}
	private class MouseEvent extends MouseAdapter {
		private Input input;
		private int dragX,dragY;
		private boolean dragStart;
		public MouseEvent(Input input) {
			this.input = input;
		}
		
		public void mousePressed(java.awt.event.MouseEvent e) {
			input.mouseDragged = false;
			input.mouseDragDistance = 0;
			input.addButton(e.getButton());
			input.lastPressed = e.getButton();
		}
		public void mouseReleased(java.awt.event.MouseEvent e) {
			input.mouseDragged = false;
			input.mouseDragDistance = 0;
			input.removeButton(e.getButton());
			input.lastPressed = -1;
		}
		public void mouseMoved(java.awt.event.MouseEvent e) {
			input.mouseX = e.getX();
			input.mouseY = e.getY();
		}
		public void mouseDragged(java.awt.event.MouseEvent e) {
			input.mouseDragged = true;
			input.mouseX = e.getX();
			input.mouseY = e.getY();
			if (this.dragStart) {
				this.dragY = e.getX();
				this.dragX = e.getY();
				this.dragStart = false;
			}
			input.mouseDragDistance = (dragX - e.getX()) + (dragY - e.getY()) / 2;
			if (input.mouseDragDistance < 0) {
				input.mouseDragDistance *= -1;
			}
		}
	}
}