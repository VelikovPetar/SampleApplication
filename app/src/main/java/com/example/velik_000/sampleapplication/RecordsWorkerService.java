package com.example.velik_000.sampleapplication;

import android.app.Service;
import android.content.Intent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by velik_000 on 02/17/2017.
 */

public class RecordsWorkerService extends Service {

    private String TAG = "IncBCST";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started!");
        String type = intent.getStringExtra("type");
        if(type != null) {
            if(type.equals("write")) {
                String info = intent.getStringExtra("info");
                String saved = intent.getStringExtra("saved");
                ContentValues cv = new ContentValues();
                cv.put(RecordsTable.COLUMN_INFO, info);
                cv.put(RecordsTable.COLUMN_SAVED, saved);
                ContentResolver cr = getContentResolver();
                try {
                    cr.insert(RecordsContentProvider.CONTENT_URI, cv);
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, e.getMessage());
                }
            } else if(type.equals("read")) {
                // TODO
            } else {
                // TODO
            }
        }
        stopSelf();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed!");
    }
}
