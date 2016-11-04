package com.game.object.background;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.game.enums.ID;
import com.game.main.Game;
import com.game.main.Handler;
import com.game.object.GameObject;

public class Star extends GameObject {
	
	
	public Star(float x, float y, float velX, float velY, ID id, Handler handler) {
		super(x, y, id, handler);
		this.velX = velX;
		this.velY = velY;
	}

	public void tick() {
		
	}

	public void render(Graphics2D g) {
		g.setColor(Color.lightGray);
		g.fillRect((int)x, (int)y, (int)(2 * Game.world.ppu), (int)(2 * Game.world.ppu));
	}

	public Rectangle getBounds() {
		return null;
	}

}
