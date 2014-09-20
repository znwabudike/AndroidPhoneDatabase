package com.github.znwabudike.androidphonedatabase.db;

import java.io.IOException;
import java.sql.Statement;
import java.util.ArrayList;

import com.github.znwabudike.androidphonedatabase.struct.AndroidDevice;


public class DbHandler {


	private DbHelper dbhelper;
	
	public DbHandler(){
		this.dbhelper = new DbHelper();
		
	}
	
	public boolean insertDevice(AndroidDevice device){
		try {
			
			Statement statement = dbhelper.createConnection();
			checkTable(statement, DBSettings.TABLE_NAME);
			String sql = StatementBuilder.buildAddDeviceCommand(device, DBSettings.COLUMN_NAMES);
			log(sql);
			return (dbhelper.executeInsert(statement, sql) > 0);
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean insertDevices(ArrayList<AndroidDevice> devices){
		try {
			
			Statement statement = dbhelper.createConnection();
			checkTable(statement, DBSettings.TABLE_NAME);
			String sql = StatementBuilder.buildAddDevicesCommand(devices, DBSettings.COLUMN_NAMES);
			log(sql);
			return (dbhelper.executeInsert(statement, sql) > 0);
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean checkTable(Statement statement, String tableName) {
		boolean result = DbCreator.createAndroidDeviceTable(statement, DBSettings.COLUMN_NAMES, DBSettings.TABLE_NAME);
		log("Table Created? " + result);
		return result;
	}

	private void log(String string) {
		String TAG = this.getClass().getSimpleName();
		System.out.println(TAG + " : " + string);
	}
	
	
}
