package com.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import com.game.main.Game;
import com.game.main.Handler;

public class ButtonStart extends Button {

	public ButtonStart(String text, Image idleSprite, Image hoverSprite, Image disableSprite, int sizeX, int sizeY, Handler handler) {
		super(text, idleSprite, hoverSprite, disableSprite, sizeX, sizeY, handler);
	}
	
	public void render(Graphics2D g, @SuppressWarnings("unused") int buttonID, int offsetX, int offsetY) {	
		if (enabled) { //When enabled
			//When hovered
			if (handler.input.mouseX >= offsetX * Game.world.ppu && handler.input.mouseX <=  (sizeX + offsetX) * Game.world.ppu && handler.input.mouseY >= offsetY * Game.world.ppu && handler.input.mouseY <= (sizeY + offsetY) * Game.world.ppu && !noHover) {
				if (!isHovered) isHovered = true;
				Game.world.drawScaledImage(g, hoverSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
				g.setColor(Color.white);
				Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.07)), (int)(offsetY + (sizeY * 0.68)), (int)(sizeX / 7));
			} else { //When idle.
				if (isHovered) isHovered = false;
				Game.world.drawScaledImage(g, idleSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
				g.setColor(Color.white);
				Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.07)), (int)(offsetY + (sizeY * 0.68)), (int)(sizeX / 7));
			}
		} else { //When disabled
			if (isHovered) isHovered = false;
			Game.world.drawScaledImage(g, disableSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
			g.setColor(Color.white);
			Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.07)), (int)(offsetY + (sizeY * 0.68)), (int)(sizeX / 7));
			Game.world.drawScaledImage(g, handler.sprite.getSprite("connector"), (int)((offsetX + sizeX - 4) * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
		}
	}
}
