package com.github.znwabudike.androidphonedatabase.http;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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

public class HttpHelper {
	HttpClient client;
	private HtmlParser p;
	private static final String uri = Settings.URI;
	public HttpHelper(){
		p = new HtmlParser();
	}
	public HttpResponse getResponse(String uri){
		client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);
		HttpResponse response = null;

		try{
			log("Sending request");
			response = client.execute(httpGet);
			log("Got Response");
			StatusLine statusLine = response.getStatusLine();
			log("Getting Status");
			int statusCode = statusLine.getStatusCode();
			log("Status Code = " + (statusCode == 200 ? "Ok" : ("Fail " + statusCode)));

		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}

		return response;
	}


	private void log(String string) {
		if (Settings.DEBUG){
			//		if (true){
			String TAG = this.getClass().getSimpleName();
			System.out.println(TAG + " : " + string);
		}
	}
}
