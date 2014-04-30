package com.test.di;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import java.net.URLEncoder;
import com.lib.DNILib.UpdateCheckBroadcastReceiver;
import com.lib.DNILib.Manager;
import com.lib.DNILib.CheckUpdateAndDownloadService;
import com.lib.DNILib.DownloadBroadcastReceiver;
public class SampleMainActivity extends Activity
{
  String link="your_app_update_check_link";
   UpdateCheckBroadcastReceiver ucbr=null;
   DownloadBroadcastReceiver dbr=null;
   Manager manager=null;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try{
        link=link+"?val_key="+URLEncoder.encode("your_key","UTF-8");
        }
        catch(Exception e){}
        manager=new Manager(this);
        manager.setUpdateCheckURL(link);
        ucbr=new UpdateCheckBroadcastReceiver(manager);
        dbr= new DownloadBroadcastReceiver(manager);

    }
    @Override
    public void onStart()
    {   super.onStart();
        registerReceiver(ucbr,new IntentFilter(CheckUpdateAndDownloadService.UPDATE_FOUND));
        registerReceiver(dbr,new IntentFilter(CheckUpdateAndDownloadService.DOWNLOAD_COMPLETE));
        manager.checkUpdates();
    }
    @Override
     public void onDestroy()
    {   super.onDestroy();
        unregisterReceiver(ucbr);
        unregisterReceiver(dbr);

    }
}
