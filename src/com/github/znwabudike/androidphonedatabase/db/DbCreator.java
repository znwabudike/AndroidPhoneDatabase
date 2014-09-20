package com.github.znwabudike.androidphonedatabase.db;

import java.sql.SQLException;
import java.sql.Statement;

public class DbCreator {
	
	public static boolean createAndroidDeviceTable(Statement statement, String[] colnames, String tablename){
		String stmt = StatementBuilder.buildCreateTableCommand(colnames, tablename);
		try {
			return statement.execute(stmt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (e.getMessage().contains("memory")){
				System.out.println("file not found?");
			}
		}
		return false;
	}
	
	
}
