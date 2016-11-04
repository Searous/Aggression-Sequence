package com.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import com.game.main.Game;
import com.game.main.Handler;

public class ButtonStage extends Button {

	public ButtonStage(String text, Image idleSprite, Image hoverSprite, Image disableSprite, int posX, int posY, int sizeX, int sizeY, Handler handler) {
		super(text, idleSprite, hoverSprite, disableSprite, posX, posY, sizeX, sizeY, handler);
	}
	
	int level;
	String name;
	boolean completed;
	public void render(Graphics2D g, int buttonID, int offsetX, int offsetY) {	
		try {
			name = handler.getStage((Game.areaSelected * 5) + buttonID).newInstance().name;
			level = handler.getStage((Game.areaSelected * 5) + buttonID).newInstance().level;
		} catch (Exception e) {
			e.printStackTrace();
		}
		completed = false;
		
		if (enabled) { //When enabled
			//When hovered
			if (handler.input.mouseX >= offsetX * Game.world.ppu && handler.input.mouseX <=  (sizeX + offsetX) * Game.world.ppu && handler.input.mouseY >= offsetY * Game.world.ppu && handler.input.mouseY <= (sizeY + offsetY) * Game.world.ppu && !noHover) {
				if (!isHovered) isHovered = true;
				Game.world.drawScaledImage(g, hoverSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
				Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				Game.world.drawScaledText(g, "LV "+level, (int)(offsetX + (sizeX * 0.77)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				if (completed) g.setColor(Color.yellow);
				Game.world.drawScaledText(g, name, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), name.length() > 15 ? (int)(sizeX / (name.length() * 0.67)) : (int)(sizeX / 9));
			} else { //When idle.
				if (isHovered) isHovered = false;
				Game.world.drawScaledImage(g, idleSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
				Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				Game.world.drawScaledText(g, "LV "+level, (int)(offsetX + (sizeX * 0.77)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				if (completed) g.setColor(Color.yellow);
				Game.world.drawScaledText(g, name, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), name.length() > 15 ? (int)(sizeX / (name.length() * 0.67)) : (int)(sizeX / 9));
			}
		} else { //When disabled
			if (isHovered) isHovered = false;
			Game.world.drawScaledImage(g, disableSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
			Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
			Game.world.drawScaledText(g, "LV "+level, (int)(offsetX + (sizeX * 0.77)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
			if (completed) g.setColor(Color.yellow);
			Game.world.drawScaledText(g, name, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), name.length() > 15 ? (int)(sizeX / (name.length() * 0.67)) : (int)(sizeX / 9));
		}
	}
}
