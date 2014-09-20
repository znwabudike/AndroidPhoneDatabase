package com.github.znwabudike.androidphonedatabase.parser;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.github.znwabudike.androidphonedatabase.db.DBSettings;
import com.github.znwabudike.androidphonedatabase.db.DbHandler;
import com.github.znwabudike.androidphonedatabase.db.DbHelper;
import com.github.znwabudike.androidphonedatabase.struct.AndroidDevice;


public class HtmlParser {
	String line = "";
	String strong = "<br><strong>";
	String sstrong = "</strong>";
	String ul = "<ul>";
	String lt = "<";
	String gt = ">";
	String split;
	String brand = null;
	String model;
	String br = "<br>";
	String sul = "</ul>";
	String li = "<li>";
	String sli = "</li>";
	String[] nums = {"0","1","2","3","4","5","6","7","8","9","0"};
	ArrayList<String> brands = new ArrayList<String>();
	ArrayList<Character> alpha = new ArrayList<Character>();
	Character first = null;
	boolean isNewChar = false;
	HashMap<Character,HashMap<String,HashMap<String, String>>> deviceMap =
			new HashMap<Character,HashMap<String,HashMap<String, String>>>();

	HashMap<String,HashMap<String, String>> brandMap = 
			new HashMap<String,HashMap<String, String>>();

	HashMap<String, String> modelMap = 
			new HashMap<String, String>();

	private boolean startParse = false;
	private boolean isFinished = false;
	private BufferedReader reader;
	DbHandler dbHandler = new DbHandler();
	private ArrayList<AndroidDevice> devices = new ArrayList<AndroidDevice>();

	public HashMap<Character,HashMap<String,HashMap<String, String>>> parseResponse(BufferedReader reader) throws IOException {
		this.reader = reader;
		int pos = 0;

		DbHelper dbHelper = new DbHelper();
		dbHelper.createNewDatabase(DBSettings.TABLE_NAME);

		while ((line = reader.readLine()) != null){
			if (line.contains("<a name=\"num\"></a><br>")){
				//				log("Line read: " + line);
				startParse  = true;
			}if (startParse){

				if(! parseForBrand() ){
					break;
				}else{
					parseForModels();
				}

				if (isNewChar){
					//					log("Putting brandMap into :" + Character.toString(first));
					deviceMap.put(first, brandMap);
				}else{
					//					log("not new char");
				}
			}
		}
		if (!dbHandler.insertDevices(devices)){
			log("Something Wrong With Insert!");
			return null;
		}
		return deviceMap;
	}

	private void parseForModels() throws IOException {
		//		log("Parsing for models");
		if(line.contains(ul)){
			line = reader.readLine();
		}
		String leftover;
		modelMap = new HashMap<String, String>();
		//					log("New Model Map");
		//		log("Line read: " + line); 
		if (line.contains(li)){
			String[] splits = line.split(li);
			for (String s : splits){
				split = s.split(sli)[0];

				if (split.contains("(")){

					// may get rid of this
					String modelKey = split.split("\\(")[1];
					String modelNum = modelKey.replace("(", "");
					//					modelNum = modelNum.split("/")[0];

					//put it into the DB
					modelNum = modelKey.replace(")", "");
					modelNum = modelNum.split("/")[0];
					String modelNum2 = modelNum.split("/")[0];
					//TODO do something with model number 2.. make another column?

					String modelValue = split.split("\\(")[0];
					AndroidDevice device = new AndroidDevice(brand, modelNum, modelValue, null);
					devices.add(device);


					//					if (!dbHandler.insertDevice(device)){
					//						log("Something Wrong With Insert!");
					//						return;
					//					}

					//									log("Model = " + split);
				}else{

					//				log("No modelValue: "+ split);
					//									modelMap.put(split,null);
				}
			}
			if (split.contains(sul)){
				leftover = split.split(sul)[1];
				log("Leftover = " + leftover);
			}
		}

		if(!brandMap.containsKey(brand)){
			log("Putting " + brand + " into brandMap");
			brandMap.put(brand, modelMap);
		}

	}

	private boolean parseForBrand() throws IOException {
		//		log("Parsing for brand");
		//				log("Line read: " + line); npo :)
		if (line.contains(ul)){
			line = reader.readLine();
		}
		String temp;
		if(line.contains(strong)){//<strong>3Q</strong>
			String[] splits = line.split(strong);
			//			log("Splits size = "+ splits.length);

			line = splits[1];//3Q</strong>

			split = line.split(sstrong)[0];//3Q

			//			log("BRAND NAME: " + split);
			if (split.contains("</script>")){
				log("/script found!!");
				return false;
			}
			//				split = split.split(lt)[0];
			brand = split;
			first = brand.charAt(0);

			if (! alpha.contains(first)){
				//				log("NEW CHAR = " + first);
				alpha.add(first);
				isNewChar = true;
			}
			else{ isNewChar = false;}

			brands.add(brand);
			//			log("Brand = " + brand.toUpperCase());
			brandMap = new HashMap<String,HashMap<String, String>>();

			//			log("Line read: " + line);

		}else {
		}
		return true;

	}

	private void log(String string) {
		String TAG = this.getClass().getSimpleName();
		System.out.println(TAG + " : " + string);
	}
}
