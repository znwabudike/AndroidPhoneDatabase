package com.github.znwabudike.androidphonedatabase.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SizeUtil {
	
	public static int getSize(Object o){

	try {
		  // Get a ByteArrayOutputStream to catch the output of the
		  //   ObjectOutputStream without going to disk
		  ByteArrayOutputStream baos = new ByteArrayOutputStream();

		  // Get an ObjectOutputStream so we can dump the entire
		  //   object at one shot
		  ObjectOutputStream oos = new ObjectOutputStream(baos);

		  // Write the object
		  oos.writeObject(o);

		  // Close the stream
		  oos.close();

		  // Query the ByteArrayOutputStream for its size
		  return baos.size();
		} catch (IOException e) {
		  // Something went wrong.  Print the stack trace.
		  e.printStackTrace();

		  // Return -1 so the caller knows we failed
		  return -1;
		}
	}
}
