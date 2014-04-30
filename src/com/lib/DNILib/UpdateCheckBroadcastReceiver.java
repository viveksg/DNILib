/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lib.DNILib;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class UpdateCheckBroadcastReceiver extends BroadcastReceiver{
      public static final String UPDATE_FOUND="UPDATE FOUND";
      public static final String DOWNLOAD_COMPLETE="DOWNLOAD_COMPLETE";
      public static final int CHECK_UPDATE_TASK=1;
      public static final int DOWNLOAD_TASK=2;
      public static final int INSTALL_TASK=3;
      public int present_task=0;
      public Context app_context;
      String apk_file_path="",apk_url="";
      Manager manager=null;

      public UpdateCheckBroadcastReceiver(Manager mgr)
      {
       manager=mgr;
      }
      public interface StartDownload{
          public void startDownload(String apk_url);
         
      }
      public class DInterface implements DialogInterface.OnClickListener
      {
       public void onClick(DialogInterface dface,int button_value)
         {
           if (button_value!=-1)
           {
               dface.dismiss();
               return;
           }
              manager.startDownload(apk_url);
           
           
         }
      }

    @Override
     public void onReceive(Context context,Intent intent)
    { Bundle bundle=null;
        String action=intent.getAction();
        bundle=intent.getBundleExtra(Manager.BROADCAST_BUNDLE);
        DInterface diface=new DInterface();
        if(action.equals(UPDATE_FOUND))
        { 
          apk_url=bundle.getString(Manager.DOWNLOAD_URL);
          Log.v("apk_url", ""+apk_url);
          AlertDialog update_alert=new AlertDialog.Builder(context).create();
          update_alert.setTitle("New update");
          update_alert.setMessage("New update available\nWould you like to download it now?");
          update_alert.setButton(-1, "Yes",diface);
          update_alert.setButton(-2, "No",diface);
          update_alert.show();
        }
      
    }


}
