package com.game.main;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class Util {
	public static float clamp(float var, float min, float max) {
		if (var >= max)
			return var = max;
		else if (var <= min)
			return var = min;
		else
			return var;
	}
	public static  int clamp(int var, int min, int max) {
		if (var >= max)
			return var = max;
		else if (var <= min)
			return var = min;
		else
			return var;
	}

	/**
	 * Get the InputStream for a resource included in the JAR.
	 * @param path path of the resource
	 * @return
     */
	public static InputStream getResourceStream(String path) {
		return ClassLoader.getSystemClassLoader().getResourceAsStream(path);
	}

	public static URL getResourceURL(String path) {
		return ClassLoader.getSystemClassLoader().getResource(path);
	}

	/**
	 * Read configuration settings from a file.
	 *
	 * @param path path to the file
	 * @return a HashMap of key,value pairs
	 * @throws IOException if the file cannot be found or a read error occurs
     */
	public static HashMap<String, Object> getConfiguration(Path path) throws IOException {
		if (Files.exists(path)) {
			//Load from existing save file
			List<String> ls = Files.readAllLines(path);
			return readConfiguration(ls);
		} else {
			throw new IOException("File does not exist \"" + path.toString() + "\"");
		}
	}

	/**
	 * Read configuration settings from an InputStream.
	 *
	 * @param in
	 * @return
	 * @throws IOException
     */
	public static HashMap<String, Object> getConfiguration(InputStream in) throws IOException {
		// First we need to read lines from the InputStream
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		List<String> lines = new ArrayList<>();


		String line = br.readLine();
		while (line != null) {
			lines.add(line);
			line = br.readLine();
		}

		return readConfiguration(lines);
	}

	/**
	 * Read a list of lines as a configuration file.
	 *
	 * @return
     */
	private static HashMap<String, Object> readConfiguration(List<String> ls) {
		HashMap config = new HashMap();

		for (int line = 0; line < ls.size(); line++) for (int sep = 0; sep < ls.get(line).length(); sep++) if (ls.get(line).charAt(sep) == '=') {
			String name = ls.get(line).substring(0, sep), value = ls.get(line).substring(sep + 1).trim();

			if (value.charAt(0) == '#' || value.charAt(0) == ' ') {
				//Is a comment, or empty

			} else if (value.charAt(0) == '0' || value.charAt(0) == '1' || value.charAt(0) == '2' || value.charAt(0) == '3' || value.charAt(0) == '4' || value.charAt(0) == '5' || value.charAt(0) == '6' || value.charAt(0) == '7' || value.charAt(0) == '8' || value.charAt(0) == '9') {
				//Value is a number
				if (value.contains(".")) {
					//Is a double or float
					if (value.contains("d") || value.contains("D")) {
						//Is a double
						config.put(name, Double.parseDouble(value.substring(0, value.length() - 1)));
					} else if (value.contains("f") || value.contains("F")) {
						//Is a float
						config.put(name, Float.parseFloat(value.substring(0, value.length() - 1)));
					}
				} else {
					//Is an integer
					config.put(name, Integer.decode(value));
				}
			} else if (value.charAt(0) == '{') {
				//Is an array of strings
				String[] entries = value.substring(1, value.length() - 1).split("\\|", -1);
				for (int i = 0; i < entries.length; i++) {
					config.put(name+"."+i, entries[i].trim());
				}
			} else {
				//Is a string or boolean
				if (value.contains("true") || value.contains("false")) {
					//Is a boolean
					config.put(name, Boolean.parseBoolean(value));
				} else {
					//Is a string
					config.put(name, value);
				}
			}
		}
    	return config;
	}

	public static void restartApplication() throws URISyntaxException, IOException
	{
	  final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
	  final File currentJar = new File(Game.class.getProtectionDomain().getCodeSource().getLocation().toURI());

	  /* is it a jar file? */
	  if(!currentJar.getName().endsWith(".jar"))
	    return;

	  /* Build command: java -jar application.jar */
	  final ArrayList<String> command = new ArrayList<String>();
	  command.add(javaBin);
	  command.add("-jar");
	  command.add(currentJar.getPath());

	  final ProcessBuilder builder = new ProcessBuilder(command);
	  builder.start();
	  System.exit(0);
	}
}
