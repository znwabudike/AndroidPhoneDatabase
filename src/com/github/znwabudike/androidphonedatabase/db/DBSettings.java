package com.github.znwabudike.androidphonedatabase.db;

public class DBSettings {

//	public static final String PATH_TO_DB = "/com/github/znwabudike/androidphonedatabase/res/raw/db/";
	public static final String PATH_TO_COPY_DB = null;
	public static final String TABLE_NAME = "phonestable";
	public static final String DB_NAME = "phonesdb.db";
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_BRAND_NAME = "name";
	public static final String KEY_MODEL = "model";
	public static final String KEY_COMMON_NAME = "common";
		
	public static final String[] COLUMN_NAMES = 
		{KEY_ROWID, 
		KEY_BRAND_NAME,
		KEY_COMMON_NAME,
		KEY_MODEL};
	



}
