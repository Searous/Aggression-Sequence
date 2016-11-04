package com.game.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.game.enums.ID;
import com.game.main.Game;
import com.game.main.Handler;

public abstract class GameObject {
	protected float x, y;
	protected ID id;
	protected float velX, velY;
	protected Rectangle bounds;
	protected int sizeX, sizeY;
	protected byte layer;
	protected Handler handler;
	protected GameObject parent;
	protected String uniqueName;
	protected int rot;
	protected int energyDrop;
	protected boolean active;
	
	public GameObject(float x, float y, ID id, Handler handler) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.handler = handler;
		this.uniqueName = id.name()+"_"+handler.getSize();
	}
	
	public abstract void tick();
	public abstract void render(Graphics2D g);
	
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public float getX() {
		return this.x;
	}
	public float getY() {
		return this.y;
	}
	public void setID(ID id) {
		this.id = id;
	}
	public ID getID() {
		return this.id;
	}
	public void setVelX(int velX) {
		this.velX = velX;
	}
	public void setVelX(float velX) {
		this.velX = velX;
	}
	public void setVelY(int velY) {
		this.velY = velY;
	}
	public void setVelY(float velY) {
		this.velY = velY;
	}
	public float getVelX() {
		return this.velX;
	}
	public float getVelY() {
		return this.velY;
	}
	public Rectangle getBounds() {
		return new Rectangle((int)((x - sizeX / 2) * Game.world.ppu), (int)((y - sizeY / 2) * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
	}
	public byte getLayer() {
		return this.layer;
	}
	public void kill() {
		handler.removeObject(this);
		
		for (int i = 0; i < this.energyDrop; i++)
			handler.addObject(new EnergyPickup(this.x, this.y, handler));
		
		
	}
	public String getUniqueName() {
		return this.uniqueName;
	}
	public int getRot() {
		return this.rot;
	}

	public GameObject setPos(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
}
