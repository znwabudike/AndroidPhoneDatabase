package com.github.znwabudike.androidphonedatabase.http;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.github.znwabudike.androidphonedatabase.parser.HtmlParser;
import com.github.znwabudike.androidphonedatabase.settings.Settings;
import com.github.znwabudike.androidphonedatabase.struct.AndroidDevice;


public class HttpHelper {
	HttpClient client;
	private static final String uri = Settings.URI;
	public HttpHelper(){

	}
	public HttpResponse getPhonesPage(){
		client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);
		HttpResponse response = null;
		
		try{
			log("Sending request");
			response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			log("Status Code = " + statusCode);
			
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return response;
	}
	
	public ArrayList<AndroidDevice> parseResponse(HttpResponse response) throws IllegalStateException, IOException{
		ArrayList<AndroidDevice> deviceMap = null;
		InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
		BufferedReader reader = new BufferedReader(isr);
		HtmlParser p = new HtmlParser();
		deviceMap = p.parseResponse(reader);
		log("size =" + deviceMap.size());
		reader.close();
		isr.close();
		log("list parsed - Finish");
		return deviceMap;
	}
	
	private void log(String string) {
		if (Settings.DEBUG){
//		if (true){
			String TAG = this.getClass().getSimpleName();
			System.out.println(TAG + " : " + string);
		}
	}
}
