package com.game.item;

import com.game.main.Game;
import com.game.main.Handler;

public class ItemShip extends Item {
	
	public int hp;
	public int energy;
	public float energyPickup;
	public int speed;
	public int immuneDur;
	
	public ItemShip(String name, Handler handler) {
		super(Type.SHIP, name, handler);
		
	}
	
	@Override
	public void update() {
		
	}

	public void setStats() {
		this.stats.clear();
		this.addStat("Health", ""+hp);
		this.addStat("Energy", ""+energy);
//		this.addStat("EN Multi", ""+energyPickup);
		this.addStat("Speed", ""+speed);
		this.addStat("Immune Dur", ""+immuneDur);
		
	}
	
	@Override
	public void use() {

	}

}
