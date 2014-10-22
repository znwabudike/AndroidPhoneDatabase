AndroidPhoneDatabase
==============================

Written by Zachary Nwabudike
------------------------------

UPDATE 10/21/14 - It looks like Google changed their list from HTML to a .pdf file.

The code is semi-broken. It parses the PDF and generates an ArrayList<AndroidDevice> then attempts to insert it into the db.  
Only the last transaction remains (last ~219 devices of ~7190)

Working to get it resolved within the next few days. 

-ZN

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
