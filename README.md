DNILib
======

This library allows to download and install (DNI) new version of android app which is using this library

DNILib allows  developers to add capability of automatic installing new version/updates in their android apps.
There are scenarios in which your app is not distributed through android market for example apps which are used by firms or a group for private use.Under such scenarios there should be some mechanism to update and install new version of app whenever it is available.

DOWNLOAD
--------------
[DNILib.jar](http://goo.gl/1jV6f3)


What you need?
==============
A server for hosting APK file of android app and a simple script .

How does it works?
==================
1. This library comes with a service which communicates with server 
   And get the Latest version code and the link at which the APK file is hosted.

2. Once the server_version_code is obtained the service matches it with locat_version_code.
   And if server_version_coder is higher then broadcast is sent to the activity that a new version
   Is available. 

3. On receiving the broadcast the reveiver alerts the user by alert dialog asking if
   He/She wants to update the app. 

4. If user gives positive response then the service starts downloading
   The apk file from the link obtained in step 1.

5. Once the file download is complete the service sends the broadcast.
   The receiver of broadcast notifies user that new version is downloaded
   And if he/she would like to install new version

6. On positive response the app is installed on the android device


Script Code
============
([See SampleScript.php](https://github.com/viveksg/DNILib/blob/master/SampleScript.php))
The backend script must print 
-------------------------------------
1. Version code.
2. Link to the APK fie


The sample script receives two parameters
------------------------------------------
1. version : the value of this parameter gives the version code of apk presently installed on android device
2. val_key : This is the security key which will allow to show the latest version and APK link only on receiving   authorized request.

Output
-------
The script will match the value of val_key obtained from request and the value val_key stored at server and if there is match then the script will show Version code and link to APK file.

both the values must be seperated by SINGLE `</br>` tag.

See the sample script in Php.


Specifications for android manifest file
========================================
Following permissions are required in android manifest file ([see SampleAndroidManifest.xml](https://github.com/viveksg/DNILib/blob/master/SampleAndroidManifest.xml))


     <uses-permission android:name="android.permission.INTERNET"/>
     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     <uses-permission android:name="android.permission.INSTALL_PACKAGES"/>
   
"CheckUpdateAndDownloadService" is also needed to be declared in the manifest file

      <service android:name="com.lib.DNILib.CheckUpdateAndDownloadService" android:process=":cuads" android:label="@string/app_name" android:enabled="true"/>




Library Usage from the Activity.
================================
([see SampleMainActivity.java](https://github.com/viveksg/DNILib/blob/master/SampleMainActivity.java))

Tasks in  onCreate Method  
---------------------------------------------
 1. Instantiate the Manager class .
 2. Call Manager.setUpdateCheckURL(String update_check_url) and provide the link to update check script hosted on server
    Note: Here the URL should contain only the val_key parameter with value. The version information is added automatically By Manager. 
 3. Instantiate UpdateCheckBroadcastReceiver class
 4. Instantiate DownloadBroadcastReceiver class

Tasks in onStart method
---------------------------------------------
1. Register UpdateCheckBroadcastReceiver.
2. Register DownloadBroadcastReceiver.
3. call Manager.checkUpdates() method to initiate update check process.

Tasks in onDestroy method
----------------------------------------------
1. Unregister UpdateCheckBroadcastReceiver.
2. Unregister DownloadBroadcastReceiver.
