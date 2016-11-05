package com.game.main;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.HashMap;

public class Localization {
	private static HashMap lang = new HashMap();
	private static HashMap defaultLang = null;
	
	public static boolean useUnicodeFont = false;
	
	public static void setLocalization(String locale) throws IOException {
		// Just call it whenever language changes. Who cares.
		loadDefaultLang();
		lang.clear();
		lang = loadLang(locale);
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
	
	private static HashMap<String, Object> loadLang(String locale) throws IOException {
		return Util.getConfiguration(Util.getResourceStream("lang/" + locale + ".lang"));
	}

	private static void loadDefaultLang() throws IOException {
		if (defaultLang == null) {
			defaultLang = loadLang("en_US");
		}
	}
}
