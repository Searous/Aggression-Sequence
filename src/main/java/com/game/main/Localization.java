package com.game.main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.util.HashMap;

import javax.swing.text.StyledEditorKit.FontSizeAction;

public class Localization {
	private static HashMap lang = new HashMap();
	private static HashMap defaultLang = new HashMap();
	private static Path file;
	
	public static boolean useUnicodeFont = false;
	
	public static void setLocalization(Path path) {
		file = path;
		lang.clear();
		loadLang();
		useUnicodeFont = (boolean)lang.get("useUnicodeFont");
	}
	public static String get(String unlocalizedString) {
		try {
			if (lang.get(unlocalizedString) != null) {
				return (String)lang.get(unlocalizedString);
			} else throw new InvalidKeyException("Invalid key \""+unlocalizedString+"\"");
		} catch(Exception e) {
			Log.write("Missing or invalid localization key \""+unlocalizedString+"\"", "Warning");
			return (String)defaultLang.get(unlocalizedString) != null ? (String)defaultLang.get(unlocalizedString) : unlocalizedString;
		}
	}
	
	private static void loadLang() {
		try {
			lang = (HashMap<String, Object>)Util.getConfigurationFile(file);
			defaultLang = (HashMap<String, Object>)Util.getConfigurationFile(Paths.get("assets/lang/en_US.lang"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
