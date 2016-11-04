package com.game.object;

import java.awt.Color;
import java.awt.Graphics2D;

import com.game.enums.ID;
import com.game.main.Game;
import com.game.main.Handler;

public class BasicPartical extends GameObject {
	
	public int angle,decay,speed;
	private Color color;
	
	public BasicPartical(float x, float y, int sizeX, int sizeY, int angle, int speed, int decay, Color color, Handler handler) {
		super(x, y, ID.BasicPartical, handler);
		this.angle = angle;
		this.decay = decay;
		this.color = color;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.speed = speed;
	}

	public void tick() {
		decay--;
		if(decay <= 0) { handler.removeObject(this); return;}
		
		x += Math.sin(Math.toRadians(angle)) * speed;
		y += Math.cos(Math.toRadians(angle)) * speed;
	}

	public void render(Graphics2D g) {
		g.setColor(color);
		Game.world.fillScaledRect(g, (int)x, (int)y, sizeX, sizeY);
	}

}
