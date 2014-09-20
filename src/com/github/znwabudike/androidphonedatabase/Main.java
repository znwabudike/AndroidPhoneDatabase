package com.github.znwabudike.androidphonedatabase;
import java.applet.Applet;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;

import com.github.znwabudike.androidphonedatabase.http.HttpHelper;
import com.github.znwabudike.androidphonedatabase.utils.SizeUtil;


public class Main extends Applet{
	String commonName = "SPH710";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(){
		long time = System.currentTimeMillis();
		downloadPhonesFile();
		log("time taken = " + (System.currentTimeMillis() - time));
	}

	private void downloadPhonesFile() {
		HttpHelper httpH = new HttpHelper();
		HttpResponse response = httpH.getPhonesPage();
		try {
			HashMap<Character,HashMap<String,HashMap<String, String>>> deviceMap = httpH.parseResponse(response);
			int size = SizeUtil.getSize(deviceMap);
			log("Size = " + size);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void log(String string) {
		String TAG = this.getClass().getSimpleName();
		System.out.println(TAG + " : " + string);
	}
}
