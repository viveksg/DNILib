package com.lib.DNILib;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class DownloadBroadcastReceiver  extends BroadcastReceiver{
      public static final String DOWNLOAD_COMPLETE="DOWNLOAD_COMPLETE";
      public static final int CHECK_UPDATE_TASK=1;
      public static final int DOWNLOAD_TASK=2;
      public static final int INSTALL_TASK=3;
      public int present_task=0;
      public Context app_context;
      String apk_file_path="",apk_url="";
      Manager manager=null;

      public DownloadBroadcastReceiver(Manager mgr)
      {
       manager=mgr;
      }
      public interface InstallAPK{
          public void installAPK(String apk_file_path);
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


                manager.installAPK(apk_file_path);

         }
      }

    @Override
     public void onReceive(Context context,Intent intent)
    { Bundle bundle=null;
        String action=intent.getAction();
        bundle=intent.getBundleExtra(Manager.BROADCAST_BUNDLE);
        DInterface diface=new DInterface();
         if(action.equals(DOWNLOAD_COMPLETE))
        { 
          apk_file_path=bundle.getString(Manager.FILE_PATH);
          app_context=context;
          AlertDialog install_alert=new AlertDialog.Builder(context).create();
          install_alert.setTitle("Install New Version");
          install_alert.setMessage("Would you like to install the new version of app?");
          install_alert.setButton(-1, "Yes",diface);
          install_alert.setButton(-2, "No",diface);
          install_alert.show();
        }
    }


}

