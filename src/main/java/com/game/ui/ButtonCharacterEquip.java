package com.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.game.main.Game;
import com.game.main.Handler;

public class ButtonCharacterEquip extends Button {

	public ButtonCharacterEquip(String text, Image idleSprite, Image hoverSprite, Image disableSprite, int sizeX, int sizeY, Handler handler) {
		super(text, idleSprite, hoverSprite, disableSprite, sizeX, sizeY, handler);
		
	}
	
	String name = "";
	String type = "";
	int width = 0;
	float calc = 0.0f;
	int level = 1;
	boolean master = false;
	float max,a,xxx,cc;
	int draw;
	boolean firstUpdate = true;
	int lastXP = 0;
	int buttonID = -1;
	BufferedImage spriteIdle;
	BufferedImage spriteHover;
	public void render(Graphics2D g, int buttonID, int offsetX, int offsetY) {
		 //System.out.println("rendering");
		//this.setEnabled(false);
		if (this.buttonID == -1) this.buttonID = buttonID;
		if (Game.equipSelected == 0) {
			level = handler.items.get(handler.invShips[buttonID + 1]).level;
			name = handler.items.get(handler.invShips[buttonID + 1]).name;
			master = handler.items.get(handler.invShips[buttonID + 1]).upgrades[4];
			type = "Ship";
			if (level != 15) {
				max = Game.xpVals[handler.items.get(handler.invShips[buttonID + 1]).level];
				a = (float)(handler.items.get(handler.invShips[buttonID + 1]).xp);
				calc = (256.0f / max) * a;
				width = (int)calc;
			}
			if (handler.items.get(handler.invShips[buttonID + 1]).xp != this.lastXP) this.update();
		} else if (Game.equipSelected == 1) {
			level = handler.items.get(handler.invWeapons[buttonID + 1]).level;
			name = handler.items.get(handler.invWeapons[buttonID + 1]).name;
			master = handler.items.get(handler.invWeapons[buttonID + 1]).upgrades[4];
			type = "Weapon";
			if (level != 15) {
				max = Game.xpVals[handler.items.get(handler.invWeapons[buttonID + 1]).level];
				a = (float)(handler.items.get(handler.invWeapons[buttonID + 1]).xp);
				calc = (256.0f / max) * a;
				width = (int)calc;
			}
			if (handler.items.get(handler.invWeapons[buttonID + 1]).xp != this.lastXP) this.update();
		} else if (Game.equipSelected == 2) {
			level = handler.items.get(handler.invUtilities[buttonID + 1]).level;
			name = handler.items.get(handler.invUtilities[buttonID + 1]).name;
			master = handler.items.get(handler.invUtilities[buttonID + 1]).upgrades[4];
			type = "Utility";
			if (level != 15) {
				max = Game.xpVals[handler.items.get(handler.invUtilities[buttonID + 1]).level];
				a = (float)(handler.items.get(handler.invUtilities[buttonID + 1]).xp);
				calc = (256.0f / max) * a;
				width = (int)calc;
			}
			if (handler.items.get(handler.invUtilities[buttonID + 1]).xp != this.lastXP) this.update();
		} else if (Game.equipSelected == 3) {
			level = handler.items.get(handler.invSupers[buttonID + 1]).level;
			name = handler.items.get(handler.invSupers[buttonID + 1]).name;
			master = handler.items.get(handler.invSupers[buttonID + 1]).upgrades[4];
			type = "Super";
			if (level != 15) {
				max = Game.xpVals[handler.items.get(handler.invSupers[buttonID + 1]).level];
				a = (float)(handler.items.get(handler.invSupers[buttonID + 1]).xp);
				calc = (256.0f / max) * a;
				width = (int)calc;
			}
			if (handler.items.get(handler.invSupers[buttonID + 1]).xp != this.lastXP) this.update();
		}
		
		//master = false;
		
		if (width <= 0) width = 1;
		
		xxx = (float)sizeX;
		cc = (xxx / (256.0f / calc)) * Game.world.ppu;
		
		draw = (int)cc;
		
//		if (firstUpdate) {
//			this.update();
//			this.firstUpdate = false;
//		}
		
		if (enabled) { //When enabled
			//When hovered
			if (handler.input.mouseX >= offsetX * Game.world.ppu && handler.input.mouseX <=  (sizeX + offsetX) * Game.world.ppu && handler.input.mouseY >= offsetY * Game.world.ppu && handler.input.mouseY <= (sizeY + offsetY) * Game.world.ppu) {
				if (!isHovered) isHovered = true;
				
				if (level == 15) {
					Game.world.drawScaledImage(g, handler.sprite.getSprite("button_hover"), (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
				} else {
					Game.world.drawScaledImage(g, hoverSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));					
					Game.world.drawScaledImage(g, spriteHover, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), draw, (int)(sizeY * Game.world.ppu));
				}
				
				g.setColor(Color.white);
				Game.world.drawScaledText(g, type, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				Game.world.drawScaledText(g, "LV "+level, (int)(offsetX + (sizeX * 0.77)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				if (master) g.setColor(Color.yellow);
				Game.world.drawScaledText(g, name, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), name.length() > 15 ? (int)(sizeX / (name.length() * 0.67)) : (int)(sizeX / 9));
				
				if (toolTip != null) {
					//TODO Implament tooltips for buttons
				}
			} else { //When idle.
				if (isHovered) isHovered = false;
				
				if (level == 15) {
					Game.world.drawScaledImage(g, handler.sprite.getSprite("button"), (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
				} else {
					Game.world.drawScaledImage(g, idleSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
					Game.world.drawScaledImage(g, spriteIdle, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), draw, (int)(sizeY * Game.world.ppu));
				}
				
				g.setColor(Color.white);
				Game.world.drawScaledText(g, type, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				Game.world.drawScaledText(g, "LV "+level, (int)(offsetX + (sizeX * 0.77)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				if (master) g.setColor(Color.yellow);
				Game.world.drawScaledText(g, name, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), name.length() > 15 ? (int)(sizeX / (name.length() * 0.67)) : (int)(sizeX / 9));
				
				if (toolTip != null) {
					
				}
			}
		} else { //When disabled
			Game.world.drawScaledImage(g, handler.sprite.getSprite("button"), (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
			g.setColor(Color.white);
			Game.world.drawScaledText(g, type, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
			Game.world.drawScaledText(g, "LV ??", (int)(offsetX + (sizeX * 0.77)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
			g.setColor(Color.lightGray);
			Game.world.drawScaledText(g, "Locked", (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), name.length() > 15 ? (int)(sizeX / (name.length() * 0.67)) : (int)(sizeX / 9));
		}
	}
	
	public void update() {
		if (Game.equipSelected == 0) {
			this.lastXP = handler.items.get(handler.invShips[buttonID + 1]).xp;
		} else if (Game.equipSelected == 1) {
			this.lastXP = handler.items.get(handler.invWeapons[buttonID + 1]).xp;
		} else if (Game.equipSelected == 2) {
			this.lastXP = handler.items.get(handler.invUtilities[buttonID + 1]).xp;
		} else if (Game.equipSelected == 3) {
			this.lastXP = handler.items.get(handler.invSupers[buttonID + 1]).xp;
		}
		if (width <= 0) width = 1;
		spriteHover = (BufferedImage)Handler.sprite.getSprite("button_character_hovered_1");
		spriteHover = spriteHover.getSubimage(0, 0, width, Handler.sprite.getSprite("button_character_hovered_1").getHeight(null));
	
		spriteIdle = (BufferedImage)Handler.sprite.getSprite("button_character_idle_1");
		spriteIdle = spriteIdle.getSubimage(0, 0, width, Handler.sprite.getSprite("button_character_idle_1").getHeight(null));
	}
}
