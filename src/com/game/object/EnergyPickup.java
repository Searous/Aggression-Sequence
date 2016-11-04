package com.game.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.game.enums.ID;
import com.game.main.Game;
import com.game.main.Handler;

public class EnergyPickup extends GameObject {
	
	private boolean spriteSwitch = true;
	private int angle;
	private double speed;
	
	public EnergyPickup(float x, float y, Handler handler) {
		super(x, y, ID.EnergyPickup, handler);
		angle = Game.r.nextInt(360);
		speed = Game.r.nextInt(5) + Game.r.nextDouble();
		if (speed == 0) speed = 1;
		
		this.sizeX = 10;
		this.sizeY = 10;
	}
	
	public void tick() {
		if (x < 0 || x > 720 || y < 0 || y > 540) {
			handler.removeObject(this);
			return;
		}
		
		spriteSwitch = !spriteSwitch;
		
		if (speed > 0) {
			speed -= 0.05;
		}
		
		if (speed < 0) {
			speed = 0;
		}
		
		//Is the player within a 10 unit radius from this orb.
		if (Math.sqrt(((handler.getPlayer().getX() - x) * (handler.getPlayer().getX() - x)) + ((handler.getPlayer().getY() - y) * (handler.getPlayer().getY() - y))) <= 75.0d) {
			int offset = 9;
			float difX = x - handler.getPlayer().getX() - offset;
			float difY = y - handler.getPlayer().getY() - offset;
			float distance = (float)Math.sqrt((x - handler.getPlayer().getX() - offset) * (x - handler.getPlayer().getX() - offset) + (y - handler.getPlayer().getY() - offset) * (y - handler.getPlayer().getY() - offset));
			velX = (float)((-3.0 / distance) * difX);
			velY = (float)((-3.0 / distance) * difY);
			
			angle = (int)Math.toDegrees(Math.atan2(difX, difY)) - 180;
			if (speed < 3) speed = 3;
		}
		
		x += Math.sin(Math.toRadians(angle)) * speed;
		y += Math.cos(Math.toRadians(angle)) * speed;
		

	}

	public void render(Graphics2D g) {
		if (spriteSwitch)
			Game.world.drawScaledImage(g, handler.sprite.getSprite("energy_orb_0"), (int)(x * Game.world.ppu), (int)(y * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
		else
			Game.world.drawScaledImage(g, handler.sprite.getSprite("energy_orb_1"), (int)(x * Game.world.ppu), (int)(y * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)((x - 3) * Game.world.ppu), (int)((y - 3) * Game.world.ppu), (int)((sizeX + 3) * Game.world.ppu), (int)((sizeY + 3) * Game.world.ppu));
	}

}
