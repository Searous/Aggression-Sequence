package com.game.stage;

import com.game.action.Action;
import com.game.enums.ID;

public class Enemy {
	public int spawnDelay,rot,speed;
	public float x,y;
	public ID id;
	public Class<? extends Action> ai;
	
	public Enemy(int spawnDelay, ID id, Class<? extends Action> ai, float x, float y, int rot, int speed) {
		this.spawnDelay = spawnDelay;
		this.id = id;
		this.ai = ai;
		this.x = x;
		this.y = y;
		this.rot = rot;
		this.speed = speed;
	}
}
