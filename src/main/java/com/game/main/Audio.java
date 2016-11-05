package com.game.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Audio {
	
	private static Map<String, Sound> sound = new HashMap<String, Sound>();
	private static Map<String, Music> music = new HashMap<String, Music>();
	public static String playing = "-";
	
	public static void load() {
		try {
			music.put("temp", new Music(Util.getResourceURL("sound/music/temp.ogg")));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public static void playSound(String name) {
		sound.get(name).play();
	}
	public static void playSound(String name, float volume, float pitch) {
		sound.get(name).play(pitch, volume);
	}
	
	public static void setMusic(String name) {
		//stopMusic();
		music.get(name).loop();
		playing = name;
	}
	public static void stopMusic() {
		music.get(playing).stop();
	}
}
