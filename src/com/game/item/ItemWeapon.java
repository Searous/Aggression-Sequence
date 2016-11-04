package com.game.item;

import com.game.main.Handler;

public class ItemWeapon extends Item {

	public float damage;
	public int speed;
	public int numProjectile;
	public int fireRate;
	
	public ItemWeapon(String name, Handler handler) {
		super(Type.WEAPON, name, handler);
		
	}

	@Override
	public void update() {

	}
	
	
	public void setStats() {
		this.stats.clear();
		this.addStat("Damage", ""+(int)damage);
		this.addStat("Fire Rate", (60 / fireRate)+"/s");
		this.addStat("Shots", ""+numProjectile);
		this.addStat("Shot Speed", ""+speed);
	}
	
	@Override
	public void use() {

	}

}
