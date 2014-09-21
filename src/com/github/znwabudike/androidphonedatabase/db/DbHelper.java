package com.github.znwabudike.androidphonedatabase.db;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.github.znwabudike.androidphonedatabase.settings.DBSettings;
import com.github.znwabudike.androidphonedatabase.settings.Settings;
import com.github.znwabudike.androidphonedatabase.struct.AndroidDevice;

public class DbHelper {

	Connection connection = null;

	/*
	 * Create a connection to the databse and return a statement if 
	 * successful, else return null.  Timeout = 30s
	 */
	public Statement createConnection() throws IOException{
//		String dbpath = DBSettings.PATH_TO_DB;
//		String fullpath = DBSettings.PATH_TO_DB + DBSettings.DB_NAME;

		String uri = (new File( DBSettings.DB_NAME ).getAbsolutePath());
		
		log(uri);
//		uri = (DbHelper.class.getProtectionDomain().getCodeSource().getLocation().toString() + "res/raw/db/" + DBSettings.DB_NAME).split("file:/")[1];
	
		if (checkFileExists(uri)){
			// create a database connection
			Statement statement = null;
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:\\" + uri);
				statement = connection.createStatement();
				// set timeout to 30 sec.
				statement.setQueryTimeout(30); 

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return statement;

		}else return null; // return null if file does not exist


	}

	public ResultSetMetaData queryCommonName(String modelNum) 
			throws IOException, SQLException{
		String qry = DbStatementBuilder.buildCommonNameQuery(modelNum);

		Statement statement = createConnection();
		ResultSet rs = statement.executeQuery(qry);
		ResultSetMetaData rsmd = rs.getMetaData();

		//there should only be one device in the list
		ArrayList<AndroidDevice> devices = decodeRSMD(rs, rsmd.getColumnCount(), DBSettings.COLUMN_NAMES);

		return rsmd;

	}

	public int executeInsert(Statement statement, String sql){
		try {
			int result = statement.executeUpdate(sql);
			closeConnection();
			return result; 
		} catch (SQLException e) {
			e.printStackTrace();
			closeConnection();
			return -1;
		}

	}

	/*
	 * Return a list of devices that match the query
	 */
	private ArrayList<AndroidDevice> decodeRSMD(ResultSet rs, int columnCount,
			String[] colnames) {

		if (rs != null){
			ArrayList<AndroidDevice> devices = new ArrayList<AndroidDevice>();
			try {
				if(rs.next() && !rs.isAfterLast()){

					AndroidDevice device = new AndroidDevice();
					device.setCommonName(rs.getString(DBSettings.KEY_COMMON_NAME));
					device.setManufacturer(rs.getString(DBSettings.KEY_BRAND_NAME));
					device.setModelNum(rs.getString(DBSettings.KEY_MODEL));
					devices.add(device);

				}else return devices;

			} catch (SQLException e) {
				e.printStackTrace();	
			}

		}else return null;

		return null;
	}

	/*
	 * If the file does not exist, create it or return false
	 */
	private boolean checkFileExists(String uri){
		log("Checking if file exists at: " + uri.toString());
		File f = new File(uri);
		boolean created = false;
		if (!f.exists()){
			try {
				if (f.createNewFile()){
					log("File created");
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
			if (!created) {System.out.println("File not created"); return false;}
		}else{
			log("File Exists!");
		}
		return true;
	}


	public boolean createNewDatabase(String tablename) {
//		String sql = DbStatementBuilder.buildCreateTableCommand(DBSettings.COLUMN_NAMES, tablename);
		
		try {
			return DbCreator.createAndroidDeviceTable(createConnection(), DBSettings.COLUMN_NAMES, tablename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return false;
	}

	/*
	 * Close the connection
	 */
	public boolean closeConnection(){
		try
		{
			if(connection != null){
				connection.close();
				return true;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return false; //connection null
	}
	
	private void log(String string) {
		if (Settings.DEBUG){
			String TAG = this.getClass().getSimpleName();
			System.out.println(TAG + " : " + string);
		}
	}

}
