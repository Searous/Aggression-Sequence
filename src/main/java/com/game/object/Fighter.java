package com.game.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.game.action.Action;
import com.game.action.ActionBPEliteFighter;
import com.game.enums.ID;
import com.game.main.Game;
import com.game.main.Handler;
import com.game.main.Log;
import com.game.stage.Enemy;

public class Fighter extends GameObject {
	
	public float baseHP;
	public float hp = 10;
	private int dmgImunDur = 2;
	private int ticks = 0;
	private boolean flag = false;
	private GameObject player;
	private int rot;
	private Action action;
	private Action ai;
	
	public Fighter(float x, float y, int rot, Handler handler) {
		super(x, y, ID.Fighter, handler);
		this.sizeX = 20;
		this.sizeY = 20;
		this.rot = rot;
		this.layer = 1;
		baseHP = hp;
		this.player = handler.getPlayer();
		this.action = new ActionBPEliteFighter(x, y, 30, 7, this, handler);
		
		this.energyDrop = 1;
	}
	public Fighter(Enemy e) {
		this(e.x,e.y,e.rot,Game.handler);
//		try {
//			this.ai = e.ai.newInstance();
//		} catch (InstantiationException | IllegalAccessException e1) {
//			Log.write("Failed to spawn entity", "Error");
//			e1.printStackTrace();
//		}
	}
	public Rectangle getBounds() {
		return new Rectangle((int)((x - 3 - sizeX / 2) * Game.world.ppu), (int)((y - 3 - sizeY / 2) * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
	}
	//private boolean doX,doY;
	private byte fireIntervul = 0;
	private byte flash = 0;
	public void tick() {
		if (player == null) {
			player = handler.getPlayer();
		}
		//Log.write(getUniqueName(), "Debug");
		ticks++;
		fireIntervul++;
		flash++;
		
		action.setPos(x,y);
		
		boolean doX = true;
		boolean doY = true;
		
		if (flash >= 2) { 
			flag = false;
			flash = 0;
		}
		
		if (hp <= 0) kill();
		
		int offset = 9;
		float difX = x - player.getX() - offset;
		float difY = y - player.getY() - offset;
		float distance = (float)Math.sqrt((x - player.getX() - offset) * (x - player.getX() - offset) + (y - player.getY() - offset) * (y - player.getY() - offset));
		//System.out.println(Game.world.ppu);
		// (SPEED / distance) * dif<x/y>
		velX = (float)((-3.0 / distance) * difX);
		velY = (float)((-3.0 / distance) * difY);
		
//		float px = player.getX() + (player.getVelX());
//		float py = player.getY() + (player.getVelY());
//		float difX1 = x - px - offset;
//		float difY1 = y - py - offset;
//		float distance1 = (float)Math.sqrt((x - px - offset) * (x - px - offset) + (y - py - offset) * (y - py - offset));
//		px = (float) (player.getX() + Math.pow(player.getVelX(), distance));
//		py = (float) (player.getY() + Math.pow(player.getVelY(), distance));
//		float vx = (float)((-7.5 / distance) * difX);
//		float vy = (float)((-7.5 / distance) * difY);
//		
//		if (player.getVelX() > 0 || player.getVelY() > 0) {
//			vx = (float)((-7.5 / distance1) * difX1);
//			vy = (float)((-7.5 / distance1) * difY1);
//		}
//		if (fireIntervul >= 30) {
//			handler.addObject(new Bullet(15, x, y, ID.EnemyBullet, vx, vy, Color.pink, this, handler));
//			fireIntervul = 0;
//		}
		
		action.perform(null);
		
		callision();
		
		for (int i = 0; i < handler.getSize(); i++) {
			GameObject obj = handler.getObject(i);
			//Rectangle r = new Rectangle((int)((obj.getX() - 3 - obj.sizeX / 2) * Game.world.ppu), (int)((obj.getY + velY - 3 - sizeY / 2) * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
			if (obj.getID() == ID.Fighter && obj != this) {	
				if (new Rectangle((int)((x + velX - 3 - sizeX / 2) * Game.world.ppu), (int)((y - 3 - sizeY / 2) * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu)).intersects(obj.getBounds()) && !(obj == this)) {
					doX = false;
				}
				if (new Rectangle((int)((x - 3 - sizeX / 2) * Game.world.ppu), (int)((y + velY - 3 - sizeY / 2) * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu)).intersects(obj.getBounds()) && !(obj == this)) {
					doY = false;
				}
			}
		}
		
//		if (!doX) 
//			x += velX;
//		if (!doY)
//			y += velY;
		if (doX) {
			x += velX;
		}
		if (doY) {
			y += velY;
		}
//		x = Util.clamp(x, sizeX / 2, Game.world.WORLD_SCALE_X - 13);
//		y = Util.clamp(y, sizeY / 2, Game.world.WORLD_SCALE_Y - 33);
	}
	
	private void callision() {
		for (int i = 0; i < handler.getSize(); i++) {
			GameObject obj = handler.getObject(i);
			if (obj.getID() == ID.Bullet && obj.parent instanceof Player) {
				Bullet b = (Bullet)obj;
				if (getBounds().intersects(obj.getBounds())) {
					hp -= b.damage;
					handler.removeObject(obj);
					flag = true;
				}
			} else if (obj.getID() == ID.AOE && obj.parent instanceof Player && obj instanceof AOE) {
				AOE a = (AOE)obj;
				Player p = (Player)a.parent;
				if (a.getBounds().intersects(getBounds())) {
					hp -= p.sup.damage;
				}
			} else if (obj.getID() == ID.Fighter && obj != this) {
//				if (new Rectangle((int)x - 2, (int)y - 2, sizeX + 2, sizeY + 2).intersects(obj.getBounds())) {
//					if (x > obj.getX()) {
//						if (player.getX() < x) {
//							doY = true;
//							y += velY * -1.1;
//							
//						}
//					}  if (x < obj.getX()) {
//						if (player.getX() > x) {
//							doY = true;
//							y += velY * -1.1;
//							
//						}
//					}  if (y > obj.getY()) {
//						if (player.getY() < y) {
//							doX = true;
//							x += velX * -1.1;
//							
//						}
//					}  if (y < obj.getY()) {
//						if (player.getY() > y) {
//							doX = true;
//							x  += velX * -1.1;
//							
//						}
//					} 
//				}
				
//				if (!new Rectangle((int)(x + velX), (int)y, sizeX, sizeY).intersects(obj.getBounds()) && !(obj == this)) {
//					doX = false;
//				}
//				if (!new Rectangle((int)x, (int)(y + velY), sizeX, sizeY).intersects(obj.getBounds()) && !(obj == this)) {
//					doY = false;
//				}
				
				if (this.getBounds().intersects(obj.getBounds())) {
					
				}
				
			}
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		@SuppressWarnings("unused")
		Color color;
		if (flag) 
			color = Color.white;
		else
			color = Color.red;
		
		//Game.world.fillScaledRect(g,(int)x - (sizeX / 2), (int)y - (sizeY / 2), sizeX, sizeY);
//		Image img = null;
//		try {
//			if (flag) 
//				img = ImageIO.read(new File("assets/fighter.png"));
//			else
//				img = ImageIO.read(new File("assets/fighter1.png"));
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		

	
		//System.out.println(Game.world.ppu);
		// (SPEED / distance) * dif<x/y>
		//g2.draw(new Rectangle((int)(x + velX), (int)(y + velY), sizeX, sizeY));
//		Game.world.drawScaledRotatedImage(g, flag ? Game.sprite.getSprite("fighter_red") : Game.sprite.getSprite("fighter_white"), (int)(90 + (Math.atan2(velY, velX) * 180 / Math.PI)), (int)((x - (35 / 2))), (int)((y - (35 / 2))), (int)(35), (int)(35));
		
		rot = (int)(90 + (Math.atan2(velY, velX) * 180 / Math.PI));
		Game.world.drawScaledRotatedImage(g, flag ? Handler.sprite.getSprite("fighter_white") : Handler.sprite.getSprite("fighter_red"), rot, (int)(x), (int)(y), (int)(35), (int)(35));
		
		//g2.fill(new Ellipse2D.Float(x, y, sizeX, sizeY));
		if (Game.drawEnemyHealthBars) {
			g.setColor(Color.green);
			//Game.world.drawScaledLine(g,(int)x - (sizeX / 2), (int)y - (sizeY / 2) + sizeY, (int)((sizeX / baseHP) * hp + x - 1 - (sizeX / 2)),(int)y - (sizeY / 2) + sizeY);
			Game.world.fillScaledRect(g, (int)x - (sizeX / 2), (int)y - (sizeY / 2) + sizeY, (int)((sizeX / baseHP) * hp), 2);
		}
		//Game.world.fillScaledRect((int)x - sizeX / 2, (int)y - sizeY / 2, sizeX, sizeY);
		
//		g.setColor(Color.green);
//		Game.world.drawScaledLine((int)this.x + 6, (int)this.y + 6, (int)player.getX() + 16, (int)player.getY() + 16);
	}
	
	public void kill() {
		super.kill();
		Game.world.mobCount--;
		Game.waveKills++;
	}
	
//	public getRot() {
//		
//	}
}
