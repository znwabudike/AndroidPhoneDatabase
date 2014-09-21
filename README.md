AndroidPhoneDatabase
==============================

Written by Zachary Nwabudike
------------------------------

This Java utility gets the current list of all devices the support Android and inserts them into an SQLite3 database. 

The applet first connects to "https://support.google.com/googleplay/answer/1727131?hl=en" 
then scrapes the HTML source for each Android device.

For each device in the list, an AndroidDevice is created which contains:
Model Number
Common Name
Brand Name (Manufacturer)

Each AndroidDevice is placed into an ArrayList.  
A new database is created every time the Applet runs in case there is any change to the list.
The AndroidDevices are inserted into the SQLite database recursively in chunks of 499 until all devices are inserted.

Future work:
-------------------------------
Implement the ability to lookup a device's common name by model number

-------------------------------
For any questions/comments

Contact: z.nwabudike@gmail.com 
