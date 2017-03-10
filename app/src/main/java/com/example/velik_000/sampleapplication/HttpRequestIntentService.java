package com.example.velik_000.sampleapplication;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by velik_000 on 03/10/2017.
 */

public class HttpRequestIntentService extends IntentService {

    public static final String ACTION = "received_jsonobject";
    private String TAG = "HTTP_SERVICE";

    public HttpRequestIntentService() {
        super("HttpRequestIntentService");
    }

    public HttpRequestIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String stringUrl = intent.getStringExtra("url");
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder response = new StringBuilder();

        try {
            // Connect to server
            url = new URL(stringUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Read data
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            // Send JSON string to DisplayDataActivity
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(ACTION);
            broadcastIntent.putExtra("json_string", response.toString());
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException");
        }

    }
}
