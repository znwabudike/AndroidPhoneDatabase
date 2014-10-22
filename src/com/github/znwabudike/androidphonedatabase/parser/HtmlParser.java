package com.github.znwabudike.androidphonedatabase.parser;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;

import com.github.znwabudike.androidphonedatabase.db.DbHandler;
import com.github.znwabudike.androidphonedatabase.db.DbHelper;
import com.github.znwabudike.androidphonedatabase.settings.DBSettings;
import com.github.znwabudike.androidphonedatabase.settings.Settings;
import com.github.znwabudike.androidphonedatabase.struct.AndroidDevice;
import com.github.znwabudike.androidphonedatabase.utils.Cleaner;


public class HtmlParser {

	DbHandler dbHandler = new DbHandler();

	public ArrayList<AndroidDevice> parseResponse(HttpResponse response) throws IOException {
		InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
		BufferedReader reader = new BufferedReader(isr);

		DbHelper dbHelper = new DbHelper();
		dbHelper.createNewDatabase(DBSettings.TABLE_NAME);

		String body = parseToString(reader);
		log("Body = " + body);
		ArrayList<AndroidDevice> devices = parseForDevices(body);

		log("number of devices parsed: " + devices.size());

		if (!dbHandler.insertDevices(devices, 0)){
			log("Something Wrong With Insert!");
		} else {
			log("insert successful!");
		}
		reader.close();
		isr.close();
		return devices;

	}

	public String parseFirstResponseForPDF(HttpResponse response){

		try {
			InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
			BufferedReader reader = new BufferedReader(isr);
			String URI = getPDFLink(reader);
			Map<String, Object> map = parsePDFFromWebpage(URI);
			log("text = " + map.get("text").toString());
			return map.get("text").toString();

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	//	public ArrayList<AndroidDevice> parseResponse(HttpResponse response) throws IllegalStateException, IOException{
	//
	//
	//		ArrayList<AndroidDevice> deviceMap = null;
	//
	//		p = new HtmlParser();
	//		deviceMap = p.parseResponse(response);
	//		log("size =" + deviceMap.size());
	//		log("list parsed - Finish");
	//		return deviceMap;
	//	}

	public Map<String, Object> parsePDFFromWebpage(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream input = null;
			if (entity != null) {
				try{
					input = entity.getContent();
					BodyContentHandler handler = new BodyContentHandler(10*1024*1024); 
					Metadata metadata = new Metadata();
					AutoDetectParser parser = new AutoDetectParser();
					ParseContext parseContext = new ParseContext();
					parser.parse(input, handler, metadata, parseContext);
					map.put("text", handler.toString().replaceAll("\r", " "));
					map.put("title", metadata.get(TikaCoreProperties.TITLE));
					map.put("pageCount", metadata.get("xmpTPg:NPages"));
					map.put("status_code", response.getStatusLine().getStatusCode() + "");
				} catch (Exception e) {                     
					e.printStackTrace();
				}finally{
					if(input != null){
						try {
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}catch (Exception exception) {
			exception.printStackTrace();
		}
		return map;
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

	public ArrayList<AndroidDevice> parseTextForDeviceMap(String text) {
		ArrayList<AndroidDevice> devices = new ArrayList<AndroidDevice>();
		InputStream is = new ByteArrayInputStream(text.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = "";
		String manufacturer = null;
		try {
			while((line = br.readLine()) != null){
				line = line.replace("\\n", "");
				if (line.compareTo("") != 0){
					if(!line.contains("(") ){
						manufacturer = line;
					}else{
						int ndx = line.indexOf("(");
						String common_name = line.substring(0,ndx);
						// -1 for \n char
						String model_number = line.substring(ndx,line.length());
						if (!line.contains(")")){
							line = br.readLine();
							model_number += line;
							
						}
						
						String s = " | ";
						log(manufacturer + s + model_number + s + common_name);
						AndroidDevice device = new AndroidDevice(manufacturer, model_number, common_name, null);
						devices.add(device);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return devices;
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

	public String getPDFLink(BufferedReader reader) throws IOException {
		String line;
		while ( (line = reader.readLine() ) != null){
			if (line.contains(Settings.SEARCH_FOR)){
				log("http:" + parseLineForPDFLink(line));
				return "http:" + parseLineForPDFLink(line);
			}
		}
		return null;
	}

	private String parseLineForPDFLink(String line) {
		int beginIndex = line.indexOf(Settings.SEARCH_FOR);
		int offset = 9;
		String endToken = "\">(PDF)";
		int offsetEnd = line.indexOf(endToken);
		int endIndex = offsetEnd;
		String link = line.substring(beginIndex + offset, endIndex);
		return link;
	}

	private void log(String string) {

		if (Settings.DEBUG){
			String TAG = this.getClass().getSimpleName();
			System.out.println(TAG + " : " + string);
		}
	}


}


