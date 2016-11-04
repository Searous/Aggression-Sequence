package com.game.action;

import com.game.item.Item;
import com.game.main.Game;
import com.game.main.Handler;

public class ActionAPhaseEngineD extends Action {

	public ActionAPhaseEngineD(float x, float y, Handler handler) {
		super(x, y, handler);
	}

	@Override
	public void perform(Item item) {
		handler.getPlayer().setPos((int) ((handler.input.mouseX / Game.world.ppu) - 10), (int)((handler.input.mouseY / Game.world.ppu) - 10));
	}

}
