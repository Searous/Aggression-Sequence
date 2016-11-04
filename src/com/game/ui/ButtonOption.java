package com.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import com.game.main.Game;
import com.game.main.Handler;

public class ButtonOption extends Button {
	
	public ButtonOption(String text, String[] option, Image idleSprite, Image hoverSprite, Image disableSprite, int posX, int posY, int sizeX, int sizeY, Handler handler) {
		super(text, idleSprite, hoverSprite, disableSprite, posX, posY, sizeX, sizeY, handler);
		this.option = new ArrayList<String>();
		for (int i = 0; i < option.length; i++) {
			this.option.add(option[i]);
		}
	}
	
	public void render(Graphics2D g, int buttonID, int offsetX, int offsetY) {
		if (buttonID == 0 && Game.fullscreen) {
			this.enabled = false;
			option.set(selected, Game.WIDTH+"x"+Game.HEIGHT);
		}
		
		if (enabled) { //When enabled
			//When hovered
			if (handler.input.mouseX >= offsetX * Game.world.ppu && handler.input.mouseX <=  (sizeX + offsetX) * Game.world.ppu && handler.input.mouseY >= offsetY * Game.world.ppu && handler.input.mouseY <= (sizeY + offsetY) * Game.world.ppu) {
				if (!isHovered) isHovered = true;
				
				Game.world.drawScaledImage(g, hoverSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));					
				
				g.setColor(Color.white);
				Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				if (flag) g.setColor(Color.yellow);
				Game.world.drawScaledText(g, option.get(selected), (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), option.get(selected).length() > 15 ? (int)(sizeX / (option.get(selected).length() * 0.67)) : (int)(sizeX / 9));
			} else { //When idle.
				if (isHovered) isHovered = false;
				
				Game.world.drawScaledImage(g, idleSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));					
				
				g.setColor(Color.white);
				Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				if (flag) g.setColor(Color.yellow);
				Game.world.drawScaledText(g, option.get(selected), (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), option.get(selected).length() > 15 ? (int)(sizeX / (option.get(selected).length() * 0.67)) : (int)(sizeX / 9));

			}
		} else { //When disabled
			Game.world.drawScaledImage(g, disableSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));					
			
			g.setColor(Color.white);
			Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
			Game.world.drawScaledText(g, option.get(selected), (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), option.get(selected).length() > 15 ? (int)(sizeX / (option.get(selected).length() * 0.67)) : (int)(sizeX / 9));
		}
	}
}
