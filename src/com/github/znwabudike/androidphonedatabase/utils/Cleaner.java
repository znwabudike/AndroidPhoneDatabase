package com.github.znwabudike.androidphonedatabase.utils;

import com.github.znwabudike.androidphonedatabase.settings.Settings;

public class Cleaner {
	
	public static String cleanString(String string) {
		if (string.contains("-")){
			log(string);
			string = string.replaceAll("-","002D");
			log(string);
		}
		if (string.contains("\n")){
			string = string.replaceAll("\n", "");
		}
		if (string.contains("/")){
			string = string.replaceAll("\\/", "002F");
		}
		if (string.contains("TAB")){
			string = string.replaceAll("TAB", "TAB");
		}
		if (string.contains("(")){
			string = string.replaceAll("\\(", "0028");
		}
		if (string.contains(")")){
			string = string.replaceAll("\\)", "0029");
		}
		if (string.contains("'")){
			string = string.replaceAll("\\'", "0027");
		}
		string = (((string.charAt(string.length()-1) +"").compareTo(" ")==0) 
				? string.substring(0,string.lastIndexOf(" ")) : string) ;
		
		return string;
	}
	
	private static void log(String string) {
		if (Settings.DEBUG){
			String TAG = Cleaner.class.getSimpleName();
			System.out.println(TAG + " : " + string);
		}
	}
}
