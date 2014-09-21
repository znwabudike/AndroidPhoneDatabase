package com.github.znwabudike.androidphonedatabase.db;

import java.util.ArrayList;

import com.github.znwabudike.androidphonedatabase.settings.DBSettings;
import com.github.znwabudike.androidphonedatabase.settings.Settings;
import com.github.znwabudike.androidphonedatabase.struct.AndroidDevice;

public class DbStatementBuilder {

	public static String buildCommonNameQuery(String modelNum) {
		String qry = "SELECT " +
				"*" +
				" FROM " + 
				DBSettings.TABLE_NAME +
				" WHERE " +
				DBSettings.KEY_MODEL +
				" = " +
				modelNum;		
		return qry;
	}

	public static String buildAddDeviceCommand(AndroidDevice device, String[] colnames){

		String stmt = "";
		stmt +=	"INSERT INTO " ;
		stmt +=	DBSettings.TABLE_NAME;
		stmt += " " ;
		stmt += "(";
		stmt += buildColNames(colnames);
		stmt += "VALUES ";
		stmt += buildValues(device);
		stmt += ";";
		log(stmt);
		return stmt;
	}

	public static String buildAddDevicesCommand(ArrayList<AndroidDevice> devices, String[] colnames){

		String stmt = "BEGIN; \n";
		for (AndroidDevice device : devices){
			stmt +=	"INSERT INTO " ;
			stmt +=	DBSettings.TABLE_NAME;
			stmt += " " ;
			stmt += "(";
			stmt += buildColNames(colnames);
			stmt += "VALUES ";
			stmt += buildValues(device);
			stmt += ";\n";
		}
		stmt += "COMMIT; ";
		log(stmt);
		return stmt;
	}

	private static String buildValues(AndroidDevice device) {

		String piece = "";
		piece += "('";
		piece += device.getManufacturer() + "', '";
		piece += device.getCommonName()+ "', '";
		piece += device.getModelNum();
		piece += "')";

		return piece;
	}

	private static String buildColNames(String[] colnames) {
		int i = 1;
		String piece = "";
		for (; i < colnames.length - 1; i++ ){
			piece += "" + colnames[i] + ", ";
		}
		piece += colnames[i] + ") ";
		return piece;
	}

	public static String buildCreateTableCommand(String colnames[], String tablename){
		String stmt = 
				"create table if not exists " + 
						tablename + 
						" (_id integer primary key autoincrement, ";
		int i = 1;
		for (; i < colnames.length - 1; i++){
			stmt += colnames[i] + " text not null,";
		}
		stmt += colnames[i] + " text);";
		log(stmt);
		return stmt;
	}

	private static void log(String string) {
		if (true){
			String TAG = DbStatementBuilder.class.getSimpleName();
			System.out.println(TAG + " : " + string);
		}
	}

}
