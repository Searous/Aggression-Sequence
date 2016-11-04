package com.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.game.main.Game;
import com.game.main.Handler;

public class ButtonCharacterUpgrade extends Button {

	public ButtonCharacterUpgrade(String text, Image idleSprite, Image hoverSprite, Image disableSprite, int posX, int posY, int sizeX, int sizeY, Handler handler) {
		super(text, idleSprite, hoverSprite, disableSprite, posX, posY, sizeX, sizeY, handler);
		
	}
	
	String name = "";
	String level = "";
	boolean master = false;
	public void render(Graphics2D g, int buttonID, int offsetX, int offsetY) {
		//System.out.println(Game.equipSelected);
		//handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[0]
		if (buttonID == 0) {
			level = "LV 5";
			name = handler.getItem(handler.getInv(Game.equipSelected)[0]).upgradeNames[buttonID];
			master = handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[buttonID];
			if (handler.getItem(handler.getInv(Game.equipSelected)[0]).level < 5) this.enabled = false; else this.enabled = true;
		} else if (buttonID == 1) {
			level = "LV 5";
			name = handler.getItem(handler.getInv(Game.equipSelected)[0]).upgradeNames[buttonID];
			master = handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[buttonID];
			if (handler.getItem(handler.getInv(Game.equipSelected)[0]).level < 5) this.enabled = false; else this.enabled = true;
		} else if (buttonID == 2) {
			level = "LV 10";
			name = handler.getItem(handler.getInv(Game.equipSelected)[0]).upgradeNames[buttonID];
			master = handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[buttonID];
			if (handler.getItem(handler.getInv(Game.equipSelected)[0]).level < 10) this.enabled = false; else this.enabled = true;
		} else if (buttonID == 3) {
			level = "LV 10";
			name = handler.getItem(handler.getInv(Game.equipSelected)[0]).upgradeNames[buttonID];
			master = handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[buttonID];
			if (handler.getItem(handler.getInv(Game.equipSelected)[0]).level < 10) this.enabled = false; else this.enabled = true;
		} else if (buttonID == 4) {
			level = "LV 15";
			name = handler.getItem(handler.getInv(Game.equipSelected)[0]).upgradeNames[buttonID];
			master = handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[buttonID];
			if (handler.getItem(handler.getInv(Game.equipSelected)[0]).level < 15) this.enabled = false; else this.enabled = true;
		}
		
		if (enabled) { //When enabled
			//When hovered
			if (handler.input.mouseX >= offsetX * Game.world.ppu && handler.input.mouseX <=  (sizeX + offsetX) * Game.world.ppu && handler.input.mouseY >= offsetY * Game.world.ppu && handler.input.mouseY <= (sizeY + offsetY) * Game.world.ppu) {
				if (!isHovered) isHovered = true;
				
				Game.world.drawScaledImage(g, hoverSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
				g.setColor(Color.white);
				Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				Game.world.drawScaledText(g, level, (int)(offsetX + (sizeX * 0.79)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				if (master) g.setColor(Color.yellow);
				Game.world.drawScaledText(g, name, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), name.length() > 15 ? (int)(sizeX / (name.length() * 0.67)) : (int)(sizeX / 9));
				
				if (toolTip != null) {
					
				}
			} else { //When idle.
				if (isHovered) isHovered = false;
				
				Game.world.drawScaledImage(g, idleSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
				g.setColor(Color.white);
				Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				Game.world.drawScaledText(g, level, (int)(offsetX + (sizeX * 0.79)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				if (master) g.setColor(Color.yellow);
				Game.world.drawScaledText(g, name, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), name.length() > 15 ? (int)(sizeX / (name.length() * 0.67)) : (int)(sizeX / 9));
				
				if (toolTip != null) {
					
				}
			}
		} else { //When disabled
			if (isHovered) isHovered = false;
			
			Game.world.drawScaledImage(g, disableSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));		

			g.setColor(Color.white);
			Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
			Game.world.drawScaledText(g, level, (int)(offsetX + (sizeX * 0.79)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
			g.setColor(Color.lightGray);
			Game.world.drawScaledText(g, "Locked", (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), name.length() > 15 ? (int)(sizeX / (name.length() * 0.67)) : (int)(sizeX / 9));
			
		}
	}
}
