package com.game.item;

import com.game.main.Handler;

public class ItemSuper extends Item {

	public float size,deployDelay,duration,damage;
	
	public ItemSuper(String name, Handler handler) {
		super(Type.SUPER, name, handler);
	}

	@Override
	public void update() {

	}

	@Override
	public void use() {

	}

}
