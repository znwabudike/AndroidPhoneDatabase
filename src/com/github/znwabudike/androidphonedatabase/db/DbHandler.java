package com.github.znwabudike.androidphonedatabase.db;

import java.io.IOException;
import java.sql.Statement;
import java.util.ArrayList;

import com.github.znwabudike.androidphonedatabase.settings.DBSettings;
import com.github.znwabudike.androidphonedatabase.settings.Settings;
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
			String sql = DbStatementBuilder.buildAddDeviceCommand(device, DBSettings.COLUMN_NAMES);
			log(sql);
			return (dbhelper.executeInsert(statement, sql) > 0);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean insertDevices(ArrayList<AndroidDevice> devices, int index){
		boolean result = false;
		log("size = " + devices.size());
		if (index != devices.size()){
			log("inception on: " + index);
			int start = index;
			ArrayList<AndroidDevice> recursiveDevices = new ArrayList<AndroidDevice>();

			for (; index < start + 499 && index < devices.size(); index++){

				recursiveDevices.add(devices.get(index));
				log("name = " + devices.get(index).getModelNum());
				
			}
			insertBlock(recursiveDevices);
			result = insertDevices(devices,index);
		}else{
			return false;
		}
		return result;
	}



	private boolean insertBlock(ArrayList<AndroidDevice> devices) {
		String sql = null;
		try {

			Statement statement  = dbhelper.createConnection();
			checkTable(statement, DBSettings.TABLE_NAME);
			sql = DbStatementBuilder.buildAddDevicesCommand(devices, DBSettings.COLUMN_NAMES);
			log(sql);
			return (dbhelper.executeInsert(statement, sql) > 0);

		} catch (IOException e) {
			e.printStackTrace();
			log(sql);
			return false;
		}

	}

	private boolean checkTable(Statement statement, String tableName) {
		boolean result = DbCreator.createAndroidDeviceTable(statement, DBSettings.COLUMN_NAMES, DBSettings.TABLE_NAME);
		log("Table Created? " + result);
		return result;
	}

	private void log(String string) {
		if (Settings.DEBUG){
			String TAG = this.getClass().getSimpleName();
			System.out.println(TAG + " : " + string);
		}
	}


}
