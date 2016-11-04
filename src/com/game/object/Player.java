package com.game.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.game.enums.Control;
import com.game.enums.ID;
import com.game.item.ItemShip;
import com.game.item.ItemSuper;
import com.game.item.ItemUtility;
import com.game.item.ItemWeapon;
import com.game.main.Game;
import com.game.main.Handler;
import com.game.main.Ticker;
import com.game.main.Util;
import com.game.ui.HUD;

public class Player extends GameObject {
	
	public boolean b,v = false;
	private int ticks = 0;
	//public int dmgImunDur = (Game.difficulty == 0) ? 12 : (Game.difficulty == 1) ? 6 : (Game.difficulty == 2) ? 3 : 0;
	public int rot = 0;
	
	public ItemShip ship;
	public ItemWeapon weapon;
	public ItemUtility utility;
	public ItemSuper sup;
	public int hp = 100;
	
	public Player(float x, float y, int sizeX, int sizeY, ID id, Handler handler) {
		super(x, y, id, handler);
		//this.velX = velX;
		//this.velY = velY;
		this.sizeX = 20;
		this.sizeY = 20;
		this.layer = 2;
		
		this.ship = (ItemShip) handler.getItem((String) handler.save.get("ship"));
		this.weapon = (ItemWeapon) handler.getItem((String) handler.save.get("weapon"));
		this.utility = (ItemUtility) handler.getItem((String) handler.save.get("utility"));
		this.sup = (ItemSuper) handler.getItem((String) handler.save.get("super"));
	
		handler.addTicker("fireInterval",0,weapon.fireRate);
		handler.getTicker("fireInterval", null).active = false;
		handler.addTicker("utilTicker",0,330);
		handler.getTicker("utilTicker", null).active = false;
		handler.addTicker("superTicker",0,1800);
		handler.getTicker("superTicker", null).active = false;
		Game.isUtilReady = true;
		Game.isSuperReady = true;
	}
	
	public Rectangle getBounds() {
		int offset = 2;
		
		return new Rectangle((int)((x - offset) * Game.world.ppu), (int)((y - offset) * Game.world.ppu), (int)((10 * 2) * Game.world.ppu), (int)((10 * 2) * Game.world.ppu));
	}
	
	private void callision() {
		if (ticks >= ship.immuneDur) {
			for (int i = 0; i < handler.getSize(); i++) {
				GameObject obj = handler.getObject(i);
				if (!Game.godMode) {
					if (obj.getID() == ID.Fighter) {
						if (getBounds().intersects(obj.getBounds())) {
							Game.health -= 5;
							handler.hud.update();
							break;
						}
					} else if (obj.getID() == ID.EnemyBullet) {
						if (getBounds().intersects(obj.getBounds())) {
							Bullet b = (Bullet) obj;
							Game.health -= b.damage;
							handler.removeObject(obj);
							handler.hud.update();
							break;
						}
					} else if (obj.getID() == ID.EnergyPickup) {
						if (getBounds().intersects(obj.getBounds())) {
							if (Game.energy < ship.energy && Game.energy != ship.energy) Game.energy += 5;
							if (Game.energy > ship.energy) Game.energy = ship.energy;
							handler.removeObject(obj);
							handler.hud.update();
							break;
						}
					} 
				}
			}
			ticks = 0;
		} 
	}

	public boolean infinateEnergy = true;
	public void tick() {
		ticks++;
		rot++;
		ticks = (int) Util.clamp((float)ticks, 0f, (float)ship.immuneDur);
		
		if (infinateEnergy) {
			Game.energy = ship.energy;
		}
		
		weapon.action.perform(weapon);
		
		if (Game.isUtilReady && Game.energy >= 10) {
			if (handler.isPressed(Control.USE_UTILITY)) {
				
				utility.action.perform(utility); 
				Game.isUtilReady = false; 
				handler.getTicker("utilTicker", null).ticks = 0;
				handler.getTicker("utilTicker", null).active = true;
				Game.energy -= 10;
				
			}
		} else {
			if (handler.getTicker("utilTicker", null).isMax()) {
				handler.getTicker("utilTicker", null).active = false;
				Game.isUtilReady = true;
			}
		}
		
		if (Game.isSuperReady && Game.energy >= 45) {
			if (handler.isPressed(Control.USE_SUPER)) {
				sup.action.perform(sup);
				Game.isSuperReady = false;
				handler.getTicker("superTicker", null).ticks = 0;
				handler.getTicker("superTicker", null).active = true;
				Game.energy -= 45;
				
				//handler.addObject(new GameObject());
			}
		} else {
			if (handler.getTicker("superTicker", null).isMax()) {
				handler.getTicker("superTicker", null).active = false;
				Game.isSuperReady = true;
			}
		}
			
//		if (x >= Game.WIDTH - 32) {
//			b = true;
//		} else if (x <= 0) {
//			b = false;
//		}
//		if (y >= Game.HEIGHT - 64) {
//			v = true;
//		} else if (y <= 0) {
//			v = false;
//		}
//		
//		if (b) {
//			x -= velX;			
//		} else {
//			x += velX;			
//		}
//		if (v) {
//			y -= velY;
//		} else {
//			y += velY;
//		}
		
		
//		y += velY;
//		x += velX;
		
		int angle = 0;
		boolean move = false;
		
		if (handler.isPressed(Control.MOVE_UP) && handler.isPressed(Control.MOVE_LEFT)) {
			angle = 225;
			move = true;
		} else if (handler.isPressed(Control.MOVE_UP) && handler.isPressed(Control.MOVE_RIGHT)) {
			angle = 135;
			move = true;
		} else if (handler.isPressed(Control.MOVE_DOWN) && handler.isPressed(Control.MOVE_LEFT)) {
			angle = 315;
			move = true;
		} else if (handler.isPressed(Control.MOVE_DOWN) && handler.isPressed(Control.MOVE_RIGHT)) {
			angle = 45;
			move = true;
		} else if (handler.isPressed(Control.MOVE_UP)) {
			angle = 180;
			move = true;
		} else if (handler.isPressed(Control.MOVE_DOWN)) {
			angle = 0;
			move = true;
		} else if (handler.isPressed(Control.MOVE_LEFT)) {
			angle = 270;
			move = true;
		} else if (handler.isPressed(Control.MOVE_RIGHT)) {
			angle = 90;
			move = true;
		}
		
		if (move) {
			x += Math.sin(Math.toRadians(angle)) * ship.speed;
			y += Math.cos(Math.toRadians(angle)) * ship.speed;
		}
		
		Rectangle bounds = this.getBounds();
		
		x = Util.clamp(x, 4, (float) ((Game.WIDTH / Game.world.ppu) - (bounds.getWidth() / 2)));
		y = Util.clamp(y, 4, (float) ((Game.HEIGHT / Game.world.ppu) - (bounds.getHeight() / 2)));
		
		callision();
	}

	public void render(Graphics2D g) {
//		g.setColor(Color.white);
//		//g.fillRect((int)x, (int)y, sizeX, sizeY);
		g.setColor(Color.white);
		
//		Stroke oldStroke = g2.getStroke();
//		
//		Game.world.setScalledStroke(g2, 8);
//		Game.world.drawScaledEllipse(g2, x, y, sizeX, sizeY);
//		
//		g.setColor(Color.black);
//		Game.world.setScalledStroke(g2, 5);
//		Game.world.drawScaledEllipse(g2, x, y, sizeX, sizeY);
//		
//		g2.setStroke(oldStroke);
		
		Game.world.drawScaledRotatedImage(g, Handler.sprite.getSprite("player"), rot, (int)(x + 8), (int)(y + 8), (int)(32), (int)(32));
		
//		Image image = null;
//		try {
//			image = ImageIO.read(new File("assets/player.png"));
//			g.drawImage(image, x, y, sizeX, sizeY, null);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		
	}

	public double getSizeX() {
		// TODO Auto-generated method stub
		return this.sizeX;
	}

}
