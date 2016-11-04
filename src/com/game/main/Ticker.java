package com.game.main;

public class Ticker {
	public int min,max,ticks = 0;
	public boolean active = true;
	public String name;
	public Ticker(int min, int max, String name) {
		this.min = min;
		this.max = max;
		this.name = name;
		if (min == max) {
			Log.write("Ticker \""+name+"\" has equal min/max values!", "Warning");
		}
	}
	public void tick() {
		if (active) {
			if (ticks >= max) {
				ticks = max;
			} else if (ticks < min) {
				ticks = min;
			} else {
				ticks++;
			}
		}
	}
	public boolean isMax() {
		return this.ticks == this.max;
	}
}
