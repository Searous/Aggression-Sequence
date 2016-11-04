package com.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.game.enums.State;
import com.game.main.Game;
import com.game.main.Handler;

/**
 * Menu class.  Instances must be register in the handler to use.
 * @author Searous
 */
public class Menu {
	public String title;
	public int size;
	public Button[] options;
	public int posX,posY,sepX,sepY;
	protected Handler handler;
	
	/**
	 * Creates a standard menu object.
	 * @param title The title of the menu to be displayed.
	 * @param size The number of buttons the menu will display.  Menus may contain more than this.
	 * @param posX The X origin of this menu. 0,0 is top left corner.
	 * @param posY The Y origin of this menu. 0,0 is top left corner.
	 * @param sepX Number of game units to separate each button horizontally.
	 * @param sepY Number of game units to separate each button vertically.
	 * @param options Button[] - The list of Button objects this menu will use.
	 */
	public Menu(String title, int size, int posX, int posY, int sepX, int sepY, Handler handler, Button[] options) {
		this.title = title;
		this.size = size;
		this.options = options;
		this.posX = posX;
		this.posY = posY;
		this.sepX = sepX;
		this.sepY = sepY;
		this.handler = handler;
	}
	
	/**
	 * Gets a button object with the given index.
	 * @param index int - The button ID to return
	 * @return Button object
	 */
	public Button getButton(int index) {
		return options[index];
	}
	
	private int ticks;
	private boolean canClick = false;
	/**
	 * Updates the menu.  Should never be called outside the handler.
	 */
	public void tick() {
		//System.out.println(Handler.mouse.distanceDragged);
		for (int i = 0; i < options.length; i++) {
			if (options[i].isHovered && handler.input.isButtonDown(1) && handler.input.mouseDragDistance >= 0 && handler.input.mouseDragDistance <= 45 && canClick) {
				this.action(i);
			}
		}
		if (handler.input.isButtonDown(1) || handler.input.isButtonDown(3))
			canClick = false;
		else
			canClick = true;
//		if (ticks >= 1) {
//			ticks = 0;
//			for (int i = 0; i < size; i++) {
//				options[i].update();
//			}
//		}
//		ticks++;
	}
	
	/**
	 * Renders the menu.  Should never be called ouside the handler.
	 */
	public void render(Graphics2D g) {
		g.setColor(Color.white);
		Game.world.drawScaledText(g, title, 15, 50, 50);
		
		for (int i = 0; i < size; i++) {
//			int offsetX = 0;
//			int offsetY = 0;
//			if (sepX > 0) {
//				offsetX = (sepX > 0 ? ((options[i].getSizeX() + sepX) * i) : 0);
//			}
//			if (sepY > 0) {
//				offsetX = (sepY > 0 ? ((options[i].getSizeY() + sepY) * i) : 0);
//			}
			options[i].render(g, i, posX + (sepX > 0 ? ((options[i].getSizeX() + sepX) * i) : 0), posY + (sepY > 0 ? ((options[i].getSizeY() + sepY) * i) : 0));
		}
	}
	
	private boolean update = false;
	public void updateButtons() {
		
		update = true;
	}
	
	public void action(int pressed) {}
}
