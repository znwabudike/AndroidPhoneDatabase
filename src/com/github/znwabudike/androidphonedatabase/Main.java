package com.github.znwabudike.androidphonedatabase;
import java.applet.Applet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;

import com.github.znwabudike.androidphonedatabase.http.HttpHelper;
import com.github.znwabudike.androidphonedatabase.struct.AndroidDevice;
import com.github.znwabudike.androidphonedatabase.utils.SizeUtil;


public class Main extends Applet{
	String commonName = "SPH710";

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
		long time = System.currentTimeMillis();
		ArrayList<AndroidDevice> deviceMap = null;
		try {
			deviceMap = httpH.parseResponse(response);
		} catch (IllegalStateException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();}
		finally{
			log("Finished");
			log("time taken = " + (System.currentTimeMillis() - time));
			log("number of devices = " + deviceMap.size());
			System.exit(0);
		}

	}

	private void log(String string) {
		String TAG = this.getClass().getSimpleName();
		System.out.println(TAG + " : " + string);
	}
}
