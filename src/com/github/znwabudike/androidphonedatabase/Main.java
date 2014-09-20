package com.github.znwabudike.androidphonedatabase;
import java.applet.Applet;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;

import com.github.znwabudike.androidphonedatabase.http.HttpHelper;


public class Main extends Applet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(){
		downloadPhonesFile();
	}

	private void downloadPhonesFile() {
		HttpHelper httpH = new HttpHelper();
		HttpResponse response = httpH.getPhonesPage();
		try {
			HashMap<Character,HashMap<String,HashMap<String, String>>> deviceMap = httpH.parseResponse(response);
			
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
