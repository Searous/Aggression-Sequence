package com.game.stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.game.enums.State;
import com.game.main.Game;
import com.game.main.Log;
import com.game.object.Fighter;

public abstract class Stage {
	public List<Wave> waves;
	public String name;
	public int level,currentWave;
	public Random r = new Random(System.currentTimeMillis());
	public String[] description = new String[]{
			 "The ammount of text that",
			 "can be fit on a signle",
			 "line depends heavilly",
			 "on the characters on a",
			 "given line.",
			 "",
			 "I might want to write",
			 "a program to make these",
			 "stage descriptions",
			 "easier to write and edit.",
			 "As of now, it is tedious",
			 "and annoying.  That's",
			 "what I get for not using",
			 "a monospace font :/",
			 "----1----2----3----4----5-",
			 "This is the max line size?"
	};
//	public  String[] description = new String[]{
//			 "1 This is a line counter",
//			 "2",
//			 "3",
//			 "4",
//			 "5",
//			 "6",
//			 "7",
//			 "8",
//			 "9",
//			 "10",
//			 "11",
//			 "12",
//			 "13",
//			 "14",
//			 "15",
//			 "16",
//			 "17",
//			 "18",
//			 "19",
//			 "20",
//			 "21",
//			 "22",
//			 "23",
//			 "24",
//			 "25"
//	};
	public void start() {
		Game.currentEnemy = 0;
		Game.currentWave = 0;
		Game.waveKills = 0;
		
		Game.handler.addTicker("spawnTimer", 0, waves.get(Game.currentWave).getNextSpawnTime());
		Game.handler.setTickerActive("spawnTimer", true);
		Game.handler.addTicker("waveTimer", 0, 300);
		Game.handler.setTickerActive("waveTimer", true);
	}
	
	int side = 0; //0 - top, 1 - right, 2 - bottom, 3 - left
	int baseX,baseY,offsetX,offsetY = 0;
	@SuppressWarnings("incomplete-switch")
	public void tick() {
		if (Game.currentWave != waves.size()) {
			if (Game.handler.getTicker("waveTimer", null).isMax()) {
				if (Game.waveKills != waves.get(Game.currentWave).enemies.size()) {
					if (Game.handler.getTicker("spawnTimer", null).isMax()) {
						if (Game.world.mobCount < Game.world.mobLimit && Game.currentEnemy != waves.get(Game.currentWave).enemies.size()) {
							switch(waves.get(Game.currentWave).getNextEnemy().id) {
							case Fighter: Game.handler.addObject(new Fighter(waves.get(Game.currentWave).getNextEnemy())); break;
							
							}
							
							Game.world.mobCount++;
							
							Game.handler.addTicker("spawnTimer", 0, waves.get(Game.currentWave).getNextSpawnTime());
							Game.currentEnemy++;
						}
					} else {
						//Between enemies
					}
				} else {
					//Switch waves
					Game.currentWave++;
					if (Game.currentWave != waves.size()) {
						Game.handler.addTicker("waveTimer", 0, waves.get(Game.currentWave).startDelay);
						Game.waveKills = 0;
						Game.currentEnemy = 0;
					}
				}
			} else {
				//between waves
				
			}
		} else {
			//End of stage
			Game.state = State.Start_Menu;
		}
	}
}
