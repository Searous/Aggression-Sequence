package com.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import com.game.main.Game;
import com.game.main.Handler;

public class Cursor {
	private static int ticks = 0;
	private static int rot = 0;
	private static int rotSpd = 2;
	private static int distance = 5;
	public static int size = 5;
	public static Image sprite = Handler.sprite.getSprite("cursor");
	public static boolean enabled = false;
	
	public static void tick() { 
		
	}
	
	public static void draw(Graphics g, int mx, int my, Handler handler) {
		if (enabled) {
			g.drawImage(sprite, (int)(mx - ((2.6 * size))), (int)(my - ((2.6 * size))), (int)((3 * size) * Game.world.ppu), (int)((3 * size) * Game.world.ppu), null);
		}
		
		//Game.world.drawScaledImage(g, sprite, (int)(mx - (9)), (int)(my - (9)), (int)(18), (int)(18));
		

//		Graphics2D g2 = (Graphics2D)g;
//		
//		Polygon p = new Polygon(new int[]{0, 0 - size, 0 + size}, new int[]{0 + size, 0 - size, 0 - size}, 3);
//		AffineTransform af = new AffineTransform();
//		
//		g2.setTransform(af);
//		g2.translate((double)mx, (double)my);
//		g2.rotate(Math.toRadians(rot));
//		
//		//Game.world.setScalledStroke(g2, 1);
//		g2.setColor(Color.green);
//		g2.draw(p);
		
		//g2.fillRect(mx - 1, my - 1, (int)(2), (int)(2));
	}
}
