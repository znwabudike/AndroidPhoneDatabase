package com.github.znwabudike.androidphonedatabase.parser;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.github.znwabudike.androidphonedatabase.db.DbHandler;
import com.github.znwabudike.androidphonedatabase.db.DbHelper;
import com.github.znwabudike.androidphonedatabase.settings.DBSettings;
import com.github.znwabudike.androidphonedatabase.settings.Settings;
import com.github.znwabudike.androidphonedatabase.struct.AndroidDevice;
import com.github.znwabudike.androidphonedatabase.utils.Cleaner;


public class HtmlParser {

	DbHandler dbHandler = new DbHandler();

	public ArrayList<AndroidDevice> parseResponse(BufferedReader reader) throws IOException {

		DbHelper dbHelper = new DbHelper();
		dbHelper.createNewDatabase(DBSettings.TABLE_NAME);

		String body = parseToString(reader);
		ArrayList<AndroidDevice> devices = parseForDevices(body);

		log("number of devices parsed: " + devices.size());

		if (!dbHandler.insertDevices(devices, 0)){
			log("Something Wrong With Insert!");
			return devices;
		} else {
			log("insert successful!");
			return devices;
		}

	}



	private String parseToString(BufferedReader reader) throws IOException {
		String string = "";
		String line;
		boolean isParsing = false;
		while ( (line = reader.readLine()) != null){
			if (line.contains(Settings.STRING_BEGIN)){
				log("Starting parser: " + line);
				isParsing = true;
			}else if (line.contains(Settings.STRING_TERMINATE)){
				string += line;
				log(Settings.STRING_TERMINATE + " Body clipped.");
				return string;
			}
			if(isParsing){
				string += line + "\n";
			}

		}
		return string;
	}

	private ArrayList<AndroidDevice> parseForDevices(String body) {

		ArrayList<AndroidDevice> devices = new ArrayList<AndroidDevice>();
		String[] brands_line = body.split("<strong>");
		int i = 1;
		int count = 0;
		log("number of brands = " + brands_line.length);
		for (;i < brands_line.length ; i ++){
			String temp = brands_line[i];
			//			log(i + " " +  temp);
			String brand = temp.split("</strong>")[0];
			String productsblob;
			log("brand = " + brand);
			log("temp = " + temp);
			productsblob = temp.split("</ul>")[0].split("<ul>")[1];
			log("productsblob = " + productsblob);
			String[] products = productsblob.split("</li>");

			for (String product : products){
				product = product.replace("<li>","");
				product = product.replace("\n","");
				if (product.compareTo("") != 0){

					String common_name = product.substring(0,product.indexOf("("));
					String model = product.substring(product.indexOf("("),product.length());

					AndroidDevice device = new AndroidDevice(
							Cleaner.cleanString(brand), 
							Cleaner.cleanString(model), 
							Cleaner.cleanString(common_name),
							null);


					//									if (Settings.DEBUG) device.printDevice();
					devices.add(device);

					//// If you're curious about how slow this can be with single adds
					//// then comment out the above line and uncomment the lines below
					////				if (!dbHandler.insertDevice(device)){
					////					log("Something Wrong With Insert!");
					////					return null;
					////			
					////				}
					//////////////////////////////////////////////////////////////
				}
			}

		}
		log("Size of devices returned= " + devices.size());
		return devices;
	}

	private void log(String string) {

		if (Settings.DEBUG){
			String TAG = this.getClass().getSimpleName();
			System.out.println(TAG + " : " + string);
		}
	}
}


