package com.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.game.main.Game;
import com.game.main.Handler;

public class MenuAdvanced extends Menu {
	
	public MenuAdvanced(String title, int size, int posX, int posY, Handler handler, Button[] options) {
		super(title, size, posX, posY, 0, 0, handler, options);
	}
	
	protected boolean canClick = false;
	public void tick() {
		for (int i = 0; i < options.length; i++) {
			if (options[i].isHovered && handler.input.isButtonDown(1) && handler.input.mouseDragDistance >= 0 && handler.input.mouseDragDistance <= 45 && canClick) {
				this.action(i, 1);
			} else if (options[i].isHovered && handler.input.isButtonDown(3) && handler.input.mouseDragDistance >= 0 && handler.input.mouseDragDistance <= 45 && canClick) {
				this.action(i, 3);
			}
		}
		if (handler.input.isButtonDown(1) || handler.input.isButtonDown(3))
			canClick = false;
		else
			canClick = true;
	}
	
	/**
	 * Renders the menu.  Should never be called ouside the handler.
	 */
	public void render(Graphics2D g) {
		g.setColor(Color.white);
		Game.world.drawScaledText(g, title, 15, 50, 50);
		
		//this.posX = 243; //psX = 243, posY = 225
		
		//options[4].posX = 125; //0 = 10,10 | 1 = 241,10 | 2 = 10,71 | 3 = 241,71 | 4 = 125,133 
		
		for (int i = 0; i < size; i++) {
			options[i].render(g, i, posX + options[i].posX, posY + options[i].posY);
		}
	}
	
	public void action(int pressed, int id) {}
}
