package com.game.action;

import java.awt.Shape;

import org.newdawn.slick.geom.Circle;

import com.game.enums.ID;
import com.game.item.Item;
import com.game.item.ItemSuper;
import com.game.main.Game;
import com.game.main.Handler;
import com.game.object.AOE;

public class ActionAHighEnergyPrismD extends Action {

	private Circle bounds;
	
	public ActionAHighEnergyPrismD(float x, float y, Handler handler) {
		super(x, y, handler);
		
	}

	public void perform(Item item) {
		ItemSuper item_ = (ItemSuper)item;
		if (bounds == null) {		
			bounds = new Circle(handler.getPlayer().getX(), handler.getPlayer().getY(), item_.size * Game.world.ppu);
		}
		
		System.out.println("exect");
		handler.addObject(new AOE(handler.getPlayer().getX(), handler.getPlayer().getY(), item_.size * Game.world.ppu, (int)item_.deployDelay, 3600, ID.AOE, handler.getPlayer(), handler));
	}

}
