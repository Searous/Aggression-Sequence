package com.game.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Log {
	
	public static boolean doOutput = true;
	public static List<String> log = new ArrayList();
	
	public static void write(String str, String type) {
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        
		String s =  "[" + sdf.format(cal.getTime()) + "] " + type + " - " + str ;
		
		log.add(s);
		
		if (doOutput) {
	        System.out.println(s);
		}
	}
}
