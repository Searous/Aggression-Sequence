package com.game.object;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import com.game.enums.ID;
import com.game.main.Game;
import com.game.main.Handler;

public class Bullet extends GameObject {

	private float velX,velY;
	private Color color;
	private int speed;
	public float damage;
	public double angle = 0;
	/**
	 * Standard bullet.  Plasma Cannon projectile.
	 * @param x The X position to spawn the Bullet at.
	 * @param y The Y position to spawn the Bullet at.
	 * @param id The object ID.  Should be ID.Bullet
	 * @param velX Starting velocity X
	 * @param velY Starting velocity Y
	 * @param color The color of the Bullet.
	 * @param handler Required handler pass through.
	 */
	public Bullet(float damage, float x, float y, ID id, int speed, double angle, Color color, GameObject parent, Handler handler) {
		super(x, y, id, handler);
		this.angle = angle;
		this.speed = speed;
		this.color = color;
		this.sizeX = 12;
		this.sizeY = 12;
		this.layer = 0;
		this.parent = parent;
		this.damage = damage;
	}

	public void tick() {
//		x += velX;
//		y += velY;
		
		x += Math.sin(Math.toRadians(angle)) * speed;
		y += Math.cos(Math.toRadians(angle)) * speed;
		
		if (x < 0 || x > 720 || y < 0 || y > 540) {
//			this.kill();
			handler.removeObject(this);
		}
		
		
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		//Graphics2D g2 = (Graphics2D) g;
		//g.fillRect((int)x - sizeX / 2, (int)y - sizeY / 2, sizeX, sizeY);
		//Game.world.fillScaledElipse(g2, x - sizeX / 2, y - sizeY / 2, sizeX, sizeY);
		//Game.world.fillScaledRect(g, (int)x - sizeX / 2, (int)y - sizeY / 2, sizeX, sizeY);
		
		Game.world.drawScaledRotatedImage(g, this.parent instanceof Player ? Handler.sprite.getSprite("bullet_orange") : this.parent instanceof Fighter ? Handler.sprite.getSprite("bullet_light_pink") : Handler.sprite.getSprite("bullet"), (int)angle, (int)(x), (int)(y), (int)(16), (int)(16));
		
	}
	
	/**
	 * not used
	 */
	private AlphaComposite makeTransparent(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return AlphaComposite.getInstance(type, alpha);
	}
}
