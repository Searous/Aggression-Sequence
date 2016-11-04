package com.game.action;

import java.awt.Color;

import com.game.enums.Control;
import com.game.enums.ID;
import com.game.item.Item;
import com.game.main.Game;
import com.game.main.Handler;
import com.game.object.Bullet;

public class ActionBPPlasmaCannonD extends Action {
	public ActionBPPlasmaCannonD(float x, float y,  Handler handler) {
		super(x, y, handler);
		
	}

	@Override
	public void perform(Item item) {
		if (handler.isPressed(Control.USE_WEAPON) && handler.getTicker("fireInterval") >= handler.getPlayer().weapon.fireRate) {
			float velX = 0.0f;
			float velY = 0.0f;
			handler.getPlayer();
			int offset = 10;
			float difX = ((handler.getPlayer().getX() + offset) * Game.world.ppu) - (handler.input.mouseX);
			float difY = ((handler.getPlayer().getY() + offset) * Game.world.ppu) - (handler.input.mouseY);
			float distance = (float)Math.sqrt(difX * difX + difY * difY);
			
			// (SPEED / distance) * dif<x/y>
			velX = (float)((handler.getPlayer().weapon.speed / distance) * difX);
			velY = (float)((handler.getPlayer().weapon.speed / distance) * difY);
			
			double angle = (Math.atan2(velY, velX) * -180 / Math.PI) - 90;
			
			handler.addObject(new Bullet(handler.getPlayer().weapon.damage, handler.getPlayer().getX() + offset, handler.getPlayer().getY() + offset, ID.Bullet, handler.getPlayer().weapon.speed, angle, Color.orange, handler.getPlayer(), handler));
			
//			handler.addObject(new Bullet(handler.getPlayer().weapon.damage, handler.getPlayer().getX() + offset, handler.getPlayer().getY() + offset, ID.Bullet, handler.getPlayer().weapon.speed, angle+20, Color.orange, handler.getPlayer(), handler));
//			
//			handler.addObject(new Bullet(handler.getPlayer().weapon.damage, handler.getPlayer().getX() + offset, handler.getPlayer().getY() + offset, ID.Bullet, handler.getPlayer().weapon.speed, angle-20, Color.orange, handler.getPlayer(), handler));

//			handler.addObject(new Bullet(handler.getPlayer().weapon.damage, handler.getPlayer().getX() + offset, handler.getPlayer().getY() + offset, ID.Bullet, handler.getPlayer().weapon.speed, (72 + rot - 4) * 8, Color.orange, handler.getPlayer(), handler));
//			handler.addObject(new Bullet(handler.getPlayer().weapon.damage, handler.getPlayer().getX() + offset, handler.getPlayer().getY() + offset, ID.Bullet, handler.getPlayer().weapon.speed, (144 + rot - 4) * 8, Color.orange, handler.getPlayer(), handler));
//			handler.addObject(new Bullet(handler.getPlayer().weapon.damage, handler.getPlayer().getX() + offset, handler.getPlayer().getY() + offset, ID.Bullet, handler.getPlayer().weapon.speed, (216 +  rot - 4) * 8, Color.orange, handler.getPlayer(), handler));
//			handler.addObject(new Bullet(handler.getPlayer().weapon.damage, handler.getPlayer().getX() + offset, handler.getPlayer().getY() + offset, ID.Bullet, handler.getPlayer().weapon.speed, (288 + rot - 4) * 8, Color.orange, handler.getPlayer(), handler));
//			handler.addObject(new Bullet(handler.getPlayer().weapon.damage, handler.getPlayer().getX() + offset, handler.getPlayer().getY() + offset, ID.Bullet, handler.getPlayer().weapon.speed, (360 + rot - 4) * 8, Color.orange, handler.getPlayer(), handler));

			
			handler.setTicker("fireInterval", 0);
		}
	}

}
