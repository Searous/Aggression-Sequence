package com.game.action;

import java.awt.Color;

import com.game.enums.ID;
import com.game.item.Item;
import com.game.main.Game;
import com.game.main.Handler;
import com.game.main.Log;
import com.game.main.Ticker;
import com.game.object.Bullet;
import com.game.object.GameObject;

public class ActionBPEliteFighter extends Action {
	
	private int fireRate,speed;
	private GameObject parent;
	
	public ActionBPEliteFighter(float x, float y, int fireRate, int speed, GameObject parent, Handler handler) {
		super(x, y, handler);
		handler.addTicker(parent.getUniqueName(), 0, fireRate);
		handler.setTickerActive(parent.getUniqueName(), true);
		this.fireRate = fireRate;
		this.speed = speed;
		this.parent = parent;
	}

	@Override
	public void perform(Item item) {
		if (handler.getTicker(parent.getUniqueName()) >= fireRate) {
			float velX,velY = 0.0f;
			
			int offset = 1;
			float difX = x - handler.getPlayer().getX() - offset;
			float difY = y - handler.getPlayer().getY() - offset;
			float distance = (float)Math.sqrt((x - handler.getPlayer().getX() - offset) * (x - handler.getPlayer().getX() - offset) + (y - handler.getPlayer().getY() - offset) * (y - handler.getPlayer().getY() - offset));

			velX = (float)((distance) * difX);
			velY = (float)((distance) * difY);

			double angle = (int)((Math.atan2(velY, velX) * -180 / Math.PI) - 90);
			
			handler.addObject(new Bullet(15, x + offset, y + offset, ID.EnemyBullet, speed, angle, Color.orange, parent, handler));
			
			//handler.addObject(new Bullet(15, x + offset, y + offset, ID.EnemyBullet, speed, angle+20, Color.orange, parent, handler));
			
			//handler.addObject(new Bullet(15, x + offset, y + offset, ID.EnemyBullet, speed, angle-20, Color.orange, parent, handler));
			
//			handler.addObject(new Bullet(5, x + offset, y + offset, ID.EnemyBullet, 7, 0+(((handler.getTicker("bulletspin") * 3) + ((handler.getTicker("bulletspin") * 8)))), Color.orange, parent, handler));
//			
//			handler.addObject(new Bullet(5, x + offset, y + offset, ID.EnemyBullet, 7, 72+(((handler.getTicker("bulletspin") * 3) + ((handler.getTicker("bulletspin") * 8)))), Color.orange, parent, handler));
//			
//			handler.addObject(new Bullet(5, x + offset, y + offset, ID.EnemyBullet, 7, 144+(((handler.getTicker("bulletspin") * 3) + ((handler.getTicker("bulletspin") * 8)))), Color.orange, parent, handler));
//			
//			handler.addObject(new Bullet(5, x + offset, y + offset, ID.EnemyBullet, 7, 216+(((handler.getTicker("bulletspin") * 3) + ((handler.getTicker("bulletspin") * 8)))), Color.orange, parent, handler));
//			
//			handler.addObject(new Bullet(5, x + offset, y + offset, ID.EnemyBullet, 7, 288+(((handler.getTicker("bulletspin") * 3) + ((handler.getTicker("bulletspin") * 8)))), Color.orange, parent, handler));
//			
			
			handler.setTicker(parent.getUniqueName(), 0);
		}
	}

}
