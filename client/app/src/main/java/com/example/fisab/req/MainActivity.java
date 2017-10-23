package com.example.fisab.req;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "BIMBIM";

    ProgressDialog pd;

    private void setButtonListener(final Button btn, final int typeButton){
        final int START = 0;
        final int PLAYLIST = 1;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (typeButton){
                    case START:
                        mus.start();
                        //threadMusic.start();
                        //new yourDataTask().execute();
                        Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                    case PLAYLIST:
                        new yourDataTask().execute();
                }
            }
        });
    }

    public Button start;
    public Button get_playlist;

    public static TextView log;

    public Music mus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        start = (Button) findViewById(R.id.start);
        get_playlist = (Button) findViewById(R.id.getPlaylist);
        log = (TextView) findViewById(R.id.log);

        mus = new Music("https://psv4.userapi.com/c813337/u275500652/audios/63897b49a211.mp3?extra=73zAblJLFeu--XVp80HL0W2fd0RwrBfmdHqefvPMMp5SbJbHUozTF-wn6DvGxYjdIP7yAdWAJ91Lo50PWpvt0dn0Z6xGTWCEQeXpI4MRF1My5lC_BvT1HDoXCs2Vz7GfaWXUSLYa9R0");

        setButtonListener(start, 0);
        setButtonListener(get_playlist, 1);
    }

    ///
    protected class yourDataTask extends AsyncTask<Void, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(Void... params)
        {
            Log.e(TAG, "try...");
            //String str = "https://explorer.zensystem.io/insight-api-zen/blocks?limit=2&blockDate=2017-10-09";
            String str = "http://192.168.1.65:5000/getMusic";
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                return new JSONObject(stringBuffer.toString());
            }
            catch(Exception ex)
            {
                Log.e(TAG, "yourDataTask", ex);
                return null;
            }
            finally
            {
                if(bufferedReader != null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(JSONObject response)
        {
            if(response != null)
            {
                try {
                    log.setText(response.toString());
                    Log.e(TAG, "Success: " + response.getString("blocks"));
                } catch (JSONException ex) {
                    Log.e(TAG, "Failure", ex);
                }
            }
        }
    }
}

