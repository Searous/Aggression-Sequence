package com.game.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.game.action.Action;
import com.game.main.Game;
import com.game.main.Handler;

public abstract class Item {
	public Handler handler;
	public String name;
	public Type type;
	
	public boolean[] upgrades;
	public Action action;
	public int xp;
	public int level;
	
	public List<String[]> stats;
	public String[] upgradeNames;
	public String[] subText;
	
	public String saveName;
	
	public Item(Type type, String name, Handler handler) {
		this.stats = new ArrayList<String[]>();
		this.type = type;
		this.handler = handler;
		this.name = name;
		this.upgrades = new boolean[7];
		setLVXP(new Random());
		this.setItem();
		this.setStats();
		
		
	}
	
	public abstract void update();
	public abstract void use();
	public void setItem(){};
	
	public void loadItem() {
		
	}
	public String getName() {
		return this.name;
	}
	public Type getType() {
		return this.type;
	}
	public Item setLVXP(int level, int xp) {
		this.xp = xp;
		this.level = level;
		return this;
	}
	public Item setLVXP(Random r) {
		//System.out.println(r.nextInt(15));
		this.level = r.nextInt(14) + 1;
		//System.out.println(Game.xpVals[level]);
		this.xp = r.nextInt(Game.xpVals[level]);
		return this;
	}
	public void addStat(String name, String value) {
		stats.add(new String[]{name, value});
	}
	public void setStats() {}
	
	public enum Type {
		SHIP,UTILITY,WEAPON,SUPER;
	}
}