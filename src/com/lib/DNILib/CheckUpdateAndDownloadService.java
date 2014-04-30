package com.lib.DNILib;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;


public class CheckUpdateAndDownloadService extends Service{
     private DownloadServiceHandler dloadservhandler;
     private Looper dloadservlooper;
     public static int CHECK_FOR_UPDATE=1000;
     public static int DOWNLOAD_UPDATE=1001;
     public static final String UPDATE_FOUND="UPDATE FOUND";
     public static final String DOWNLOAD_COMPLETE="DOWNLOAD_COMPLETE";

    class DownloadServiceHandler extends Handler{
       
    public DownloadServiceHandler(Looper looper)
        {
         super(looper); 
         }
    @Override
    public void handleMessage(Message msg){
        Bundle task_bundle=msg.getData();
        int task_code=task_bundle.getInt(Manager.TASK_CODE);
        if(task_code==CHECK_FOR_UPDATE)
        {
            String url=task_bundle.getString(Manager.UPDATE_CHECK_URL);
            int version_code=task_bundle.getInt(Manager.VERSION_CODE);
            String s1="",s2="";
            String[] data=null;
            try{
            URL update_check_url=new URL(url);
            HttpURLConnection htconn=(HttpURLConnection)update_check_url.openConnection();
            BufferedReader br=new BufferedReader(new InputStreamReader(htconn.getInputStream()));
            while(true)
            {
                s1=br.readLine();
                if(s1==null)break;
                s2+=s1;
            }
            data=s2.split("</br>");
            if(Integer.parseInt(data[0])>version_code)
            {
             Bundle broadcast_bundle =new Bundle();
             broadcast_bundle.putString(Manager.DOWNLOAD_URL, data[1]);
             Intent send_broadcast=new Intent();
             send_broadcast.setAction(UPDATE_FOUND);
             send_broadcast.putExtra(Manager.BROADCAST_BUNDLE, broadcast_bundle);
             getApplication().getApplicationContext().sendBroadcast(send_broadcast);
            }

            }
            catch(Exception exception)
            {
                Log.v("Exception", ""+exception.getMessage());
            }
            
        }
        else if(task_code==DOWNLOAD_UPDATE)
        { String download_url=task_bundle.getString(Manager.APK_URL);
          String package_name=task_bundle.getString(Manager.PACKAGE_NAME);
          Log.v("pname", package_name);
           File apk_file=null;
          try{
              apk_file=new File(createFile(package_name));
              FileOutputStream fos=new FileOutputStream(apk_file);
              URL apk_url=new URL(download_url);
              HttpURLConnection htc=(HttpURLConnection)apk_url.openConnection();
              htc.setRequestMethod("GET");
              htc.setDoOutput(true);
              htc.connect();
              InputStream istream=htc.getInputStream();
              byte buffer[]=new byte[2048];
              int file_pointer=0;
              while((file_pointer=istream.read(buffer))!=-1)
                  fos.write(buffer, 0, file_pointer);
                  fos.close();
                  istream.close();
             }
          catch(Exception exception)
          {
              Log.v("Exception", ""+exception.getMessage());
          }
            Bundle broadcast_bundle=new Bundle();
            broadcast_bundle.putString(Manager.FILE_PATH,apk_file.getAbsolutePath());
            Intent broadcast_intent=new Intent();
            broadcast_intent.setAction(DOWNLOAD_COMPLETE);
            broadcast_intent.putExtra(Manager.BROADCAST_BUNDLE, broadcast_bundle);
            getApplication().getApplicationContext().sendBroadcast(broadcast_intent);
            
            
        }

         stopSelf(msg.arg1);
    }
    public String createFile(String packagename)
        {
         File f1=new File(Environment.getExternalStorageDirectory(),packagename+File.separator+"APKs");
         if(!f1.exists()&&(!f1.mkdirs()))
         {
             Log.v("File creation error","Cannot create file"+f1.getAbsolutePath());
             return "";
         }
         Date date=new Date();
         return (f1.getAbsolutePath()+File.separator+packagename+"_"+date.getTime()/1000+".apk");
        }
    }
    @Override
    public void onCreate()
    {
        HandlerThread handler_thread=new HandlerThread("Task_Thread",Process.THREAD_PRIORITY_BACKGROUND);
        handler_thread.start();
        dloadservlooper=handler_thread.getLooper();
        dloadservhandler=new DownloadServiceHandler(dloadservlooper);

    }
    @Override
    public int onStartCommand(Intent its,int flag,int startid)
    {
        Message message=dloadservhandler.obtainMessage();
        message.arg1=startid;
        message.setData(its.getBundleExtra(Manager.TASK_BUNDLE));
        dloadservhandler.sendMessage(message);
        return START_STICKY;
    }
    public IBinder onBind(Intent in)
    {
        return null;
    }
}
