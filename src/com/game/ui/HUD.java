package com.game.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.game.main.Game;
import com.game.main.Handler;

public class HUD {
	
	public static int health = 100;
	public static int vh = health;
	public Handler handler;
	
	public static double hudScale = 5.0d;
	
	public HUD(Handler handler) {
		this.handler = handler;
	}
	
	public void tick() {
		if (bg == null) {
			bg = (BufferedImage)handler.sprite.getSprite("hud_background");
		}
		if (hp == null) {
			hp = (BufferedImage)handler.sprite.getSprite("hud_health");
		}
		if (en == null) {
			en = (BufferedImage)handler.sprite.getSprite("hud_energy");
		}
				
		max = (double)handler.getPlayer().ship.hp;
		a = (double)(Game.health);
		calc = (256.0f / max) * a;
		width = (int)calc;
		
		if (width <= 0) width = 1;
		
		xxx = (double)48.0d;
		cc = (xxx / (256.0f / calc)) * hudScale;
		
		draw = (int)cc;
		
		max1 = (double) handler.getPlayer().ship.energy;
		a1 = (double)(Game.energy);
		calc1 = (256.0f / max1) * a1;
		width1 = calc1;
		
		if (width1 <= 0) width1 = 1;
		
		xxx1 = (double)48.0d;
		cc1 = (xxx1 / (256.0f / calc1)) * hudScale;
		
		draw1 = (int)cc1;
	}
	
	//Background, health, energy, cooldown 1, cooldown 2
	double width;
	double calc = 0.0d;
	double max,a,xxx,cc;
	double draw;
	
	double width1;
	double calc1 = 0.0d;
	double max1,a1,xxx1,cc1;
	double draw1;
	
	double ticks;
	
	BufferedImage bg,hp,en,c1,c2;
	public void render(Graphics2D g) {
		//TODO Rewrite HUD code and fix HUD textures
		Game.world.drawScaledImage(g, bg, (int)(10.0d * Game.world.ppu), (int)(10.0d * Game.world.ppu), (int)((48.0d * hudScale) * Game.world.ppu), (int)((12.0d * hudScale) * Game.world.ppu));
		
		Game.world.drawScaledImage(g, hp, (int)((13.0d * Game.world.ppu)),(int)((13.5d * Game.world.ppu)),(int)(draw * Game.world.ppu),(int)((6.0d * hudScale) * Game.world.ppu));
		
		Game.world.drawScaledImage(g, en, (int)((13.0d * Game.world.ppu)),(int)((39.5d * Game.world.ppu)),(int)(draw1 * Game.world.ppu),(int)((6.0d * hudScale) * Game.world.ppu));
		
		//Game.world.drawScaledImage(g,en,(int)((10.0d * Game.world.ppu)),(int)((10.6d * Game.world.ppu)),(int)(draw1 * Game.world.ppu),(int)(((6.6d) * hudScale) * Game.world.ppu));
		
		if (!Game.isUtilReady) {
			ticks = (double)handler.getTicker("utilTicker");
			g.setColor(Color.gray);
			Game.world.fillScaledRect(g, (int)(handler.getPlayer().getX() - 10), (int)(handler.getPlayer().getY() - 10), 36, 3);
			g.setColor(Color.cyan);
			Game.world.fillScaledRect(g, (int)(handler.getPlayer().getX() - 10), (int)(handler.getPlayer().getY() - 10), (int) ((36.0d / (double)(handler.getTicker("utilTicker",null).max)) * ticks), 3);
		}
		if (!Game.isSuperReady) {
			ticks = (double)handler.getTicker("superTicker");
			g.setColor(Color.gray);
			Game.world.fillScaledRect(g, (int)(handler.getPlayer().getX() - 10), (int)(handler.getPlayer().getY() + 24), 36, 3);
			g.setColor(Color.orange);
			Game.world.fillScaledRect(g, (int)(handler.getPlayer().getX() - 10), (int)(handler.getPlayer().getY() + 24), (int) ((36.0d / (double)(handler.getTicker("superTicker",null).max)) * ticks), 3);

		}
		
		//Game.world.drawScaledImage(g,c1,20,89,176,22);
		//Game.world.drawScaledImage(g,c2,191,89,176,22);
		
		g.setColor(Color.white);
		if (Game.debugHud) {
			Game.world.drawScaledText(g, "FPS: " + Game.fps, 15, 80);
			Game.world.drawScaledText(g, "Objects: " + handler.getSize(), 15, 96);
			Game.world.drawScaledText(g, "X: " + handler.getPlayer().getX(), 15, 112);
			Game.world.drawScaledText(g, "Y: " + handler.getPlayer().getY(), 15, 128);
			Game.world.drawScaledText(g, "EN: " + Game.energy, 15, 144);
		}
	}
	
	public void update() {
		max = (double)handler.getPlayer().ship.hp;
		a = (double)(Game.health);
		calc = (256.0f / max) * a;
		width = (int)calc;
		
		if (width <= 0) width = 1;
		
		xxx = (double)48.0d;
		cc = (xxx / (256.0f / calc)) * hudScale;
		
		draw = (int)cc;
		
		max1 = (double) handler.getPlayer().ship.energy;
		a1 = (double)(Game.energy);
		calc1 = (256.0f / max1) * a1;
		width1 = calc1;
		
		if (width1 <= 0) width1 = 1;
		
		xxx1 = (double)48.0d;
		cc1 = (xxx1 / (256.0f / calc1)) * hudScale;
		
		draw1 = (int)cc1;
		
		hp = (BufferedImage)Handler.sprite.getSprite("hud_health");
		hp = hp.getSubimage(0, 0, (int)width, Handler.sprite.getSprite("hud_health").getHeight(null));
		
		en = (BufferedImage)Handler.sprite.getSprite("hud_energy");
		en = en.getSubimage(0, 0, (int)width1, Handler.sprite.getSprite("hud_energy").getHeight(null));
	}
}
