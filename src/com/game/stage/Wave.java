package com.game.stage;

import java.util.ArrayList;
import java.util.List;

import com.game.main.Game;
import com.game.object.GameObject;

public class Wave {
	public List<Enemy> enemies;
	public int startDelay;
	public int currentEnemy = 0;
	/**
	 * Creates a new enemy wave.
	 * @param enemies A list of all enemies (by ID) to spawn in this wave.
	 * @param startDelay The time (in ticks) before the wave starts spawning enemies.
	 */
	public Wave(int startDelay, Enemy[] enemies) {
		this.enemies = new ArrayList<Enemy>();
		this.startDelay = startDelay;
		for (int i = 0; i < enemies.length; i++) {
			this.enemies.add(enemies[i]);
		}
	}
	
	public Enemy getNextEnemy() {
		return this.enemies.get(Game.currentEnemy);
	}

	public int getNextSpawnTime() {
		return enemies.size() < Game.currentEnemy + 1 ? enemies.get(Game.currentEnemy + 1).spawnDelay : 0;
	}
}