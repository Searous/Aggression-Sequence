package com.game.action;

import com.game.item.Item;
import com.game.main.Handler;

/**
 * Bullet Patterns and other things.  These mainly just make life easier.  i.e. you only need to program a bullet pattern once for it to work.
 * 
 * Subclass naming:
 * Action[type][name][segment]
 * ActionBPPlazmaCannonD
 * ActionBPPlazmaCannonT1U1
 * 
 * BP = Bullet Pattern
 * A = Ability
 * 
 * D = Default
 * T = Tier
 * U = Upgrade
 * 
 * @author Searous
 */
public abstract class Action {
	public float x,y;
	public Handler handler;
	
	public Action(float x, float y, Handler handler) {
		this.x = x;
		this.y = y;
		this.handler = handler;
	}
	
	public abstract void perform(Item item);
	
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
