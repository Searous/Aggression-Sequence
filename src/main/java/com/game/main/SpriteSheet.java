package com.game.main;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class SpriteSheet {
	private static Map<String, Image> sheet = new HashMap();
	
	public void register(String name, String path) {
		Game.step = "Register sprite: "+name+" @ "+path;
		Image img = null;
		boolean ok;
		try {
			img = ImageIO.read(Util.getResourceStream(path + ".png"));
			sheet.put(name, img);
			
			ok = true;
		} catch (IOException e) {
			
			e.printStackTrace();
			ok = false;
		}
		if (ok) 
			Log.write("Registered sprite \""+"assets/" + path + ".png\" with the name \""+name + "\"", "Info");
		else
			Log.write("Failed to register sprite \""+"assets/" + path + ".png\" with the name \""+name+"\"", "Warning");
		  
		Game.loaded++;
	}
	
	public Image getSprite(String name) {
		try {
			if (sheet.get(name) == null)
				throw new NullPointerException("Invalid sprite name \""+name+"\"");
			return sheet.get(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
