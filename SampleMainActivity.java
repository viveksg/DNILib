package com.test.di;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import java.net.URLEncoder;
import com.lib.DNILib.UpdateCheckBroadcastReceiver;
import com.lib.DNILib.Manager;
import com.lib.DNILib.CheckUpdateAndDownloadService;
import com.lib.DNILib.DownloadBroadcastReceiver;
public class MainActivity extends Activity
{
  String link="link to your app";
   UpdateCheckBroadcastReceiver ucbr=null;
   DownloadBroadcastReceiver dbr=null;
   Manager manager=null;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try{ //add val_key parameter with value to URL
        link=link+"?val_key="+URLEncoder.encode("your_key","UTF-8");

        }
        catch(Exception e){}
        //Instantiate Manager class
        manager=new Manager(this);
        // Set thhe update check link for manager objet
        manager.setUpdateCheckURL(link);
        //Instantiate UpdateCheckBroadcastReceiver class
        ucbr=new UpdateCheckBroadcastReceiver(manager);
         //Instantiate DownloadBroadcastReceiver class
        dbr= new DownloadBroadcastReceiver(manager);

    }
    @Override
    public void onStart()
    {   super.onStart();
        //Register Listeners
        registerReceiver(ucbr,new IntentFilter(CheckUpdateAndDownloadService.UPDATE_FOUND));
        registerReceiver(dbr,new IntentFilter(CheckUpdateAndDownloadService.DOWNLOAD_COMPLETE));
        // Call manager.Checkupdates() to initiate update check service
        manager.checkUpdates();
    }
    @Override
     public void onDestroy()
    {   super.onDestroy();
        //Unregister receivers
        unregisterReceiver(ucbr);
        unregisterReceiver(dbr);

    }
}
