package com.example.fisab.req;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by fisab on 23.10.2017.
 */
//String url = "https://psv4.userapi.com/c813337/u275500652/audios/63897b49a211.mp3?extra=73zAblJLFeu--XVp80HL0W2fd0RwrBfmdHqefvPMMp5SbJbHUozTF-wn6DvGxYjdIP7yAdWAJ91Lo50PWpvt0dn0Z6xGTWCEQeXpI4MRF1My5lC_BvT1HDoXCs2Vz7GfaWXUSLYa9R0";

public class Music extends Thread{
    private String TAG = "BIMBIM";

    private String url;

    Music(String new_url){
        this.url = new_url;
    }

    @Override
    public void run(){
        try {
            URL url = new URL(this.url);
            URLConnection conn = url.openConnection();

            conn.addRequestProperty("Accept-Charset", "UTF-8");
            try{
                File SDCardpath = Environment.getExternalStorageDirectory();
                File myDataPath = new File(SDCardpath.getAbsolutePath()
                        + "/Alarms/file.mp3");
                Log.e(TAG, myDataPath.toString());

                OutputStream outstream = new FileOutputStream(myDataPath);

                InputStream is;
                is = conn.getInputStream();

                Log.e(TAG, "Downloading...");

                byte[] buffer = new byte[4096];
                int len;
                while ((len = is.read(buffer)) > 0) {
                    outstream.write(buffer, 0, len);
                }
                outstream.close();



            }
            catch (Exception e){
                Log.e(TAG, e.toString());
            }

        } catch (Exception ex) {
            Log.e("App", "yourDataTask", ex);
        }
    }

}
