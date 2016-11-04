package com.game.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Window extends Canvas {
	public Cursor oldc;
	public Cursor newc;
	public JFrame frame;
	private Game game;
	private String title;
	private LayoutManager layout;
	/**
	 * Distance between the game and the left edge of the screen in fullscreen mode
	 */
	public int fullscreenOffset;
	//TODO Implement fullscreen mode
	public Window(int width, int height, String title, Game game) {
		this.game = game;
		this.title = title;
		this.game.setBounds(0,0,width, height);
		
		frame = new JFrame(title);
		try {
			frame.setIconImage(ImageIO.read(new File("assets/icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		oldc = frame.getContentPane().getCursor();
		newc = Toolkit.getDefaultToolkit().createCustomCursor(
			    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		frame.getContentPane().setCursor(newc);
		
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (game.fullscreen) {
			frame.setUndecorated(true);
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int w = gd.getDisplayMode().getWidth(), h = gd.getDisplayMode().getHeight();
			
			frame.toFront();
			
			//frame.setAlwaysOnTop(true);
			game.WIDTH = h * 12 / 9;
			game.HEIGHT = h;
			game.setBounds((w / 2) - (game.WIDTH / 2), 0, game.WIDTH, game.HEIGHT);
			
			Game.world.ppu = (float)Game.WIDTH / Game.world.WORLD_SCALE_X;
			
			fullscreenOffset = (w / 2) - (game.WIDTH / 2);
			
			frame.getContentPane().setLayout(null);
			frame.getContentPane().setBackground(Color.black);
			
			frame.setSize(w, h);
		} else {
			frame.pack();
		}
		
		frame.add(game);
		//frame.pack();
		
		frame.setVisible(true);
		
		this.layout = frame.getLayout();
		game.start();
	}
	
	public void setFullscreen(boolean enabled) {
		if (enabled) {
//			frame.setUndecorated(true);
//			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//			int w = gd.getDisplayMode().getWidth(), h = gd.getDisplayMode().getHeight();
//			
//			frame.toFront();
//			
//			//frame.setAlwaysOnTop(true);
//			game.WIDTH = h * 12 / 9;
//			game.HEIGHT = h;
//			game.setBounds((w / 2) - (game.WIDTH / 2), 0, game.WIDTH, game.HEIGHT);
//			
//			Game.world.ppu = (float)Game.WIDTH / Game.world.WORLD_SCALE_X;
//			
//			frame.getContentPane().setLayout(null);
//			frame.getContentPane().setBackground(Color.black);
//			
//			frame.setSize(w, h);
		} else {
			frame.getContentPane().setLayout(this.layout);
			frame.setAlwaysOnTop(false);
			frame.pack();
		}
	}
}
