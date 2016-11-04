package com.game.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class World {
	public static int WORLD_SCALE_X = 720, WORLD_SCALE_Y = WORLD_SCALE_X / 12 * 9;
	public static float ppu = (float)Game.WIDTH / WORLD_SCALE_X;
	public static Rectangle bounds = new Rectangle(0, 0, WORLD_SCALE_X, WORLD_SCALE_Y);
	public static final int mobLimit = 60;
	public static int mobCount = 0;
	
	private Handler handler;
	
	Font font;
	
	public World(Handler handler) {
		this.handler = handler;
		
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/ag_font.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
        font = font.deriveFont(Font.PLAIN,20);
        GraphicsEnvironment ge =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
	}
	
	public void drawScaledRect(Graphics g, int x, int y, int width, int height) {
		g.drawRect((int)(x * ppu), (int)(y * ppu), (int)(width * ppu), (int)(height * ppu));
	}
	
	public void fillScaledRect(Graphics g, int x, int y, int width, int height) {
		g.fillRect((int)(x * ppu), (int)(y * ppu), (int)(width * ppu), (int)(height * ppu));
	}
	
	public void drawScaledLine(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine((int)(x1 * ppu), (int)(y1 * ppu), (int)(x2 * ppu), (int)(y2 * ppu));
	}
	
	public void setScalledStroke(Graphics2D g2, int size) {
		g2.setStroke(new BasicStroke(size * ppu));
	}
	
	public void drawScaledEllipse(Graphics2D g2, float x, float y, int sizeX, int sizeY) {
		g2.draw(new Ellipse2D.Float(x * ppu, y * ppu, sizeX * ppu, sizeY * ppu));
	}
	
	public void fillScaledElipse(Graphics2D g2, float x, float y, int sizeX, int sizeY) {
		g2.fill(new Ellipse2D.Float(x * ppu, y * ppu, sizeX * ppu, sizeY * ppu));
	}
	
	public void drawScaledText(Graphics g, String text, int x, int y) {
		Font font = new Font("arial", 1, (int)(15 * ppu));
		g.setFont(font);
		g.drawString(text, (int)(x * ppu), (int)(y * ppu));
	}
	
	public void drawScaledText(Graphics2D g, String text, int x, int y, int size) {
		//Font font = new Font("8-bit Operator+ 8", 1, (int)(size * ppu));
		
		g.setFont(Localization.useUnicodeFont ? new Font("Dialog", Font.PLAIN, (int)(size * ppu)) : font.deriveFont(1, (int)(size * ppu)));
		g.drawString(text, (int)(x * ppu), (int)(y * ppu));
	}
	
	public void drawScaledImage(Graphics g, Image img, int x, int y, int width, int height) {
		g.drawImage(img, x, y, width, height, null);
	}
	
	public void drawScaledRotatedImage(Graphics g, Image img, int rot, int x, int y, int width, int height) {
		//BufferedImage image = (BufferedImage)img;
		Graphics2D g2d = (Graphics2D)g;
		
		AffineTransform oldat = g2d.getTransform();
		
//		AffineTransform at = new AffineTransform();
//		g2d.setTransform(at);
		g2d.translate((x) * ppu, (y) * ppu);
		//g2d.fillRect(0, 0, 2, 2);
		g2d.rotate(Math.toRadians(rot));
		
		g2d.drawImage(img, (int)((0 - (width / 2)) * ppu), (int)((0 - (height / 2)) * ppu), (int)(width * ppu), (int)(height * ppu), null);
		
		g2d.setTransform(oldat);
		g2d.translate(0, 0);
		g2d.rotate(Math.toRadians(0));
		
		// The required drawing location
//		int drawLocationX = 300;
//		int drawLocationY = 300;

		// Rotation information

//		double rotationRequired = Math.toRadians (rot);
//		double locationX = image.getWidth() / 2;
//		double locationY = image.getHeight() / 2;
//		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
//		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
//
//		// Drawing the rotated image at the required drawing locations
//		g2d.drawImage(op.filter(image, null), x, y, width, height, null);
		
		
	}
	
	public void drawScaledColoredImage(Graphics g, Image img, Color color, int x, int y, int width, int height) {
		g.drawImage(img, x, y, width, height, color, null);
	}
	
}
