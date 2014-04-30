package com.lib.DNILib;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.io.File;
import java.net.URLEncoder;


public class Manager implements UpdateCheckBroadcastReceiver.StartDownload,DownloadBroadcastReceiver.InstallAPK {
    Context context=null;
    int version_code=0;
    String version_name="";
    String package_name="";
    String update_check_url="";
    String download_directory="";
    public static final String VERSION_CODE="version_code";
    public static final String TASK_BUNDLE="task_bundle";
    public static final String UPDATE_CHECK_URL="update_check_url";
    public static final String APK_URL="apk_url";
    public static final String TASK_CODE="task_code";
    public static final String PACKAGE_NAME="package_name";
    public static final String BROADCAST_BUNDLE="broadcast_bundle";
    public static final String DOWNLOAD_URL="download_url";
    public static final String FILE_PATH="FILE_PATH";
    public Manager (Context cnt)
    {
        context=cnt;
        PackageInfo pinfo=null;
        try{
         pinfo=cnt.getPackageManager().getPackageInfo(cnt.getPackageName(), 0);
        }
        catch(Exception exception )
        {
            Log.v("Exception: ", ""+exception.getMessage());
        }
        version_code=pinfo.versionCode;
        version_name=pinfo.versionName;
        package_name=pinfo.packageName;
    }
    public void checkUpdates()
    {
       Bundle task_bundle=new Bundle();
       task_bundle.putInt(TASK_CODE, CheckUpdateAndDownloadService.CHECK_FOR_UPDATE);
       task_bundle.putString(UPDATE_CHECK_URL,update_check_url);
       task_bundle.putInt(VERSION_CODE, version_code);
       Intent check_update=new Intent(context,CheckUpdateAndDownloadService.class);
       check_update.putExtra(TASK_BUNDLE, task_bundle);
       context.startService(check_update);
    }

    public void startDownload(String apk_url)
    {
       Bundle task_bundle=new Bundle();
       task_bundle.putInt(TASK_CODE, CheckUpdateAndDownloadService.DOWNLOAD_UPDATE);
       task_bundle.putInt(VERSION_CODE, version_code);
       task_bundle.putString(PACKAGE_NAME, package_name);
       task_bundle.putString(APK_URL, apk_url);
       //task_bundle.putString("download_directory", download_directory);
       Intent download_update=new Intent(context,CheckUpdateAndDownloadService.class);
       download_update.putExtra(TASK_BUNDLE, task_bundle);
       context.startService(download_update);
    }
   public void installAPK(String apk_file_path)
    {
      Intent installapk =new Intent(Intent.ACTION_VIEW);
      installapk.setDataAndType(Uri.fromFile(new File(apk_file_path)),"application/vnd.android.package-archive");
      context.startActivity(installapk);
    }
   public void setUpdateCheckURL(String url)
    {  try{
        url=url+"&version="+URLEncoder.encode(""+version_code,"UTF-8");
        }
        catch(Exception e){}
       update_check_url=url;
    }

}
