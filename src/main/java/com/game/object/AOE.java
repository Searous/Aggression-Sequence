package com.game.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import com.game.enums.ID;
import com.game.main.Game;
import com.game.main.Handler;

public class AOE extends GameObject {

	public int deployDelay,duration;
	public double size,size_;
	public boolean deployed = false;
	private ID iidd;
	
	public AOE(float x, float y, float size, int deployDelay, int duration, ID id, GameObject parent, Handler handler) {
		super(x, y, id, handler);
		this.iidd = id;
		this.deployDelay = deployDelay;
		this.duration = duration;
		this.size = size;
		this.parent = parent;
		handler.addTicker(parent.getUniqueName()+"_aoe_"+this.getUniqueName()+"_deployDelay", 0, deployDelay);
		handler.setTickerActive(parent.getUniqueName()+"_aoe_"+this.getUniqueName()+"_deployDelay", false);
		handler.addTicker(parent.getUniqueName()+"_aoe_"+this.getUniqueName()+"_duration", 0, duration);
		handler.setTickerActive(parent.getUniqueName()+"_aoe_"+this.getUniqueName()+"_duration", false);
		
		this.active = false;
	}


	public void tick() {
		if (!deployed) {
			handler.setTickerActive(parent.getUniqueName()+"_aoe_"+this.getUniqueName()+"_deployDelay", true);
			
			size_ = (size / deployDelay) * handler.getTicker(parent.getUniqueName()+"_aoe_"+this.getUniqueName()+"_deployDelay");

			if (handler.getTicker(parent.getUniqueName()+"_aoe_"+this.getUniqueName()+"_deployDelay", null).isMax()) {
				this.deployed = true;
				
			}
		} else {
			handler.setTickerActive(parent.getUniqueName()+"_aoe_"+this.getUniqueName()+"_duration", true);
			this.active = true;
			
			if (handler.getTicker(parent.getUniqueName()+"_aoe_"+this.getUniqueName()+"_duration", null).isMax()) {
				this.kill();
			}
		}
	}

	public Rectangle getBounds() {
		double offset = 20.0d;
		
		return new Rectangle((int)((x - (size / 2.0d) + offset) * Game.world.ppu), (int)((y - (size / 2.0d) + offset) * Game.world.ppu),(int)((size - offset) * Game.world.ppu),(int)((size - offset) * Game.world.ppu));
	}
	
	public void render(Graphics2D g) {
		if (!deployed) {
			Game.world.drawScaledImage(g, handler.sprite.getSprite("aoe_green"), (int)(((x + 9) - (size_ / 2)) * Game.world.ppu), (int)(((y + 9) - (size_ / 2)) * Game.world.ppu), (int)(size_ * Game.world.ppu), (int)((size_) * Game.world.ppu));
		} else {
			Game.world.drawScaledImage(g, handler.sprite.getSprite("aoe_green"), (int)(((x + 9) - (size / 2)) * Game.world.ppu), (int)(((y + 9) - (size / 2)) * Game.world.ppu), (int)(size * Game.world.ppu), (int)(size * Game.world.ppu));
		}
		
		g.setColor(Color.red);
		g.draw(this.getBounds());
		
		//g.setColor(Color.red);
		//Game.world.drawScaledEllipse(g, 0, 0, (int)5, (int)5);
	}
	
	public void kill() {
		super.kill();
		handler.removeTicker(parent.getUniqueName()+"_aoe_"+this.getUniqueName()+"_deployDelay");
		handler.removeTicker(parent.getUniqueName()+"_aoe_"+this.getUniqueName()+"_duration");
		System.out.println("killed");
	}

}
