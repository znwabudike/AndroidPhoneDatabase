package com.github.znwabudike.androidphonedatabase;
import java.applet.Applet;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.httpclient.HttpParser;
import org.apache.http.HttpResponse;

import com.github.znwabudike.androidphonedatabase.db.DbHandler;
import com.github.znwabudike.androidphonedatabase.http.HttpHelper;
import com.github.znwabudike.androidphonedatabase.parser.HtmlParser;
import com.github.znwabudike.androidphonedatabase.settings.Settings;
import com.github.znwabudike.androidphonedatabase.struct.AndroidDevice;

/*
 * Copyright 2014 Zachary Nwabudike
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */


public class Main extends Applet{
	String commonName = "SPH710";

	private HtmlParser p;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(){
		p = new HtmlParser();
		downloadPhonesFile();
		
	}

	private void downloadPhonesFile() {
		
		long time = System.currentTimeMillis();
		ArrayList<AndroidDevice> deviceMap = null;
		
		HttpHelper httpH = new HttpHelper();
		HttpResponse response = httpH.getResponse(Settings.URI);
		String text = p.parseFirstResponseForPDF(response);
		deviceMap = p.parseTextForDeviceMap(text);
		DbHandler dbh = new DbHandler();
		boolean inserted = dbh.insertDevices(deviceMap, 0);
		if (inserted){
			log("Success");
		}else log("Failed");
//		try {
//			
////			deviceMap = p.parseResponse(response);
//		} catch (IllegalStateException e) {e.printStackTrace();
//		} catch (IOException e) {e.printStackTrace();}
//		finally{
			log("Finished");
			log("time taken = " + (System.currentTimeMillis() - time));
			log("number of devices = " + deviceMap.size());
			System.exit(0);
//		}

	}

	private void log(String string) {
		String TAG = this.getClass().getSimpleName();
		System.out.println(TAG + " : " + string);
	}
}
