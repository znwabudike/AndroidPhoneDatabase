package com.github.znwabudike.androidphonedatabase.db;

import java.util.ArrayList;

import com.github.znwabudike.androidphonedatabase.struct.AndroidDevice;

public class StatementBuilder {

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
		return stmt;
	}
	
	public static String buildAddDevicesCommand(ArrayList<AndroidDevice> devices, String[] colnames){

		String stmt = "";
		stmt +=	"INSERT INTO " ;
		stmt +=	DBSettings.TABLE_NAME;
		stmt += " " ;
		stmt += "(";
		stmt += buildColNames(colnames);
//		stmt += "VALUES ";
		stmt += buildValues(devices, colnames);
		stmt += ";";
		return stmt;
	}

	private static String buildValues(AndroidDevice device) {

		String piece = " ";
		piece += "('";
		piece += device.getManufacturer() + "', '";
		piece += device.getCommonName()+ "', '";
		piece += device.getModelNum();
		piece += "')";

		return piece;
	}

	private static String buildValues(ArrayList<AndroidDevice> devices, String[] colnames) {
		
		String piece = "";
		piece += "SELECT ";
		
			AndroidDevice device = devices.get(0);
			String commonName = device.getCommonName();
			commonName = (((commonName.charAt(commonName.length()-1) +"").compareTo(" ")==0) 
					? commonName.substring(0,commonName.lastIndexOf(" ")) : commonName) ;
			piece += "'" + device.getManufacturer() +"' ";
			piece += "AS ";
			piece += colnames[1] + ", ";
			piece += "'" + commonName +"' ";
			piece += "AS ";
			piece += colnames[2] + ", ";
			piece += "'" +device.getModelNum() +"' ";
			piece += "AS ";
			piece += colnames[3] + " ";
		
		int i = 1;
		for(; i < devices.size()-2; i ++){
			commonName = devices.get(i).getCommonName();
			
			commonName = (((commonName.charAt(commonName.length()-1) +"").compareTo(" ")==0) 
					? commonName.substring(0,commonName.lastIndexOf(" ")) : commonName) ;
			piece += " UNION SELECT ";
			piece += " '" + devices.get(i).getManufacturer() + "', ";
			piece += " '" + devices.get(i).getCommonName()+ "', ";
			piece += " '" + devices.get(i).getModelNum() + "'" ;
			
		}
		piece += " UNION SELECT ";
		piece += " '" + devices.get(++i).getManufacturer() + "', '";
		piece += " '" + devices.get(i).getCommonName() + "', '";
		piece += " '" + devices.get(i).getModelNum() + "'";
		piece += ";";
		
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
		System.out.println(stmt);
		return stmt;
	}

	private void log(String string) {
		String TAG = this.getClass().getSimpleName();
		System.out.println(TAG + " : " + string);
	}

}
