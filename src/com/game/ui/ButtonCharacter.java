package com.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.game.main.Game;
import com.game.main.Handler;

public class ButtonCharacter extends Button {
	
	public ButtonCharacter(String text, Image idleSprite, Image hoverSprite, Image disableSprite, int sizeX, int sizeY, Handler handler) {
		super(text, idleSprite, hoverSprite, disableSprite, sizeX, sizeY, handler);

	}
	
	String name = "";
	int width = 0;
	float calc = 0.0f;
	int level = 1;
	boolean master = false;
	float max,a,xxx,cc;
	int draw;
	boolean firstUpdate = true;
	int lastXP;
	int buttonID = -1;
	BufferedImage spriteIdle;
	BufferedImage spriteHover;
	BufferedImage spriteDisabled;
	public void render(Graphics2D g, int buttonID, int offsetX, int offsetY) {
		//String name = buttonID == 0 ? handler.getPlayer().ship.name : buttonID == 1 ? handler.getPlayer().weapon.name : buttonID == 2 ? handler.getPlayer().utility.name : handler.getPlayer().sup.name;

		//handler.getPlayer().ship.xp = 79;
		if (this.buttonID == -1) this.buttonID = buttonID;
		if (buttonID == 0) {
			level = handler.getPlayer().ship.level;
			name = handler.getPlayer().ship.name;
			master = handler.getPlayer().ship.upgrades[4];
			if (level != 15) {
				max = Game.xpVals[handler.getPlayer().ship.level];
				a = (float)(handler.getPlayer().ship.xp);
				calc = (256.0f / max) * a;
				width = (int)calc;
			}
			if (handler.getPlayer().ship.xp != this.lastXP) this.update();
		} else if (buttonID == 1) {
			level = handler.getPlayer().weapon.level;
			name = handler.getPlayer().weapon.name;
			master = handler.getPlayer().weapon.upgrades[4];
			if (level != 15) {
				max = Game.xpVals[handler.getPlayer().weapon.level];
				a = (float)(handler.getPlayer().weapon.xp);
				calc = (256.0f / max) * a;
				width = (int)calc;
			}
			if (handler.getPlayer().weapon.xp != this.lastXP) this.update();
		} else if (buttonID == 2) {
			level = handler.getPlayer().utility.level;
			name = handler.getPlayer().utility.name;
			master = handler.getPlayer().utility.upgrades[4];
			if (level != 15) {
				max = Game.xpVals[handler.getPlayer().utility.level];
				a = (float)(handler.getPlayer().utility.xp);
				calc = (256.0f / max) * a;
				width = (int)calc;
			}
			if (handler.getPlayer().utility.xp != this.lastXP) this.update();
		} else if (buttonID == 3) {
			level = handler.getPlayer().sup.level;
			name = handler.getPlayer().sup.name;
			master = handler.getPlayer().sup.upgrades[4];
			if (level != 15) {
				max = Game.xpVals[handler.getPlayer().sup.level];
				a = (float)(handler.getPlayer().sup.xp);
				calc = (256.0f / max) * a;
				width = (int)calc;
			}
			if (handler.getPlayer().sup.xp != this.lastXP) this.update();
		}
		
		//master = false;
		
		//System.out.println(master);
		
		if (width <= 0) width = 1;
		
		xxx = (float)sizeX;
		cc = (xxx / (256.0f / calc)) * Game.world.ppu;
		
		draw = (int)cc;
		
//		if (firstUpdate) {
//			this.update();
//			this.firstUpdate = false;
//		}
		
		//this.update();
		
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
				Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				Game.world.drawScaledText(g, "LV "+level, (int)(offsetX + (sizeX * 0.79)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
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
				Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				Game.world.drawScaledText(g, "LV "+level, (int)(offsetX + (sizeX * 0.79)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
				if (master) g.setColor(Color.yellow);
				Game.world.drawScaledText(g, name, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), name.length() > 15 ? (int)(sizeX / (name.length() * 0.67)) : (int)(sizeX / 9));
				
				if (toolTip != null) {
					
				}
			}
		} else { //When disabled
			if (isHovered) isHovered = false;
			
			if (level == 15) {
				Game.world.drawScaledImage(g, handler.sprite.getSprite("button_character_disabled_2"), (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
			} else {
				Game.world.drawScaledImage(g, disableSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));		
				Game.world.drawScaledImage(g, spriteDisabled, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), draw, (int)(sizeY * Game.world.ppu));
			}
			Game.world.drawScaledImage(g, handler.sprite.getSprite("connector"), (int)((offsetX + sizeX - 4) * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
			
			g.setColor(Color.white);
			Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
			Game.world.drawScaledText(g, "LV "+level, (int)(offsetX + (sizeX * 0.79)), (int)(offsetY + (sizeY * 0.30)), (int)(sizeX / 15));
			if (master) g.setColor(Color.yellow);
			Game.world.drawScaledText(g, name, (int)(offsetX + (sizeX * 0.04)), (int)(offsetY + (sizeY * 0.70)), name.length() > 15 ? (int)(sizeX / (name.length() * 0.67)) : (int)(sizeX / 9));
			
		}
	}
	public void update() {
		if (buttonID == 0) {
			this.lastXP = handler.getPlayer().ship.xp;
		} else if (buttonID == 1) {
			this.lastXP = handler.getPlayer().weapon.xp;
		} else if (buttonID == 2) {
			this.lastXP =handler.getPlayer().utility.xp;
		} else if (buttonID == 3) {
			this.lastXP =handler.getPlayer().sup.xp;
		}
		if (width <= 0) width = 1;
		spriteHover = (BufferedImage)Handler.sprite.getSprite("button_character_hovered_1");
		spriteHover = spriteHover.getSubimage(0, 0, width, Handler.sprite.getSprite("button_character_hovered_1").getHeight(null));
	
		spriteIdle = (BufferedImage)Handler.sprite.getSprite("button_character_idle_1");
		spriteIdle = spriteIdle.getSubimage(0, 0, width, Handler.sprite.getSprite("button_character_idle_1").getHeight(null));
		
		spriteDisabled = (BufferedImage)Handler.sprite.getSprite("button_character_disabled_1");
		spriteDisabled = spriteDisabled.getSubimage(0, 0, width, Handler.sprite.getSprite("button_character_disabled_1").getHeight(null));
	}
}
