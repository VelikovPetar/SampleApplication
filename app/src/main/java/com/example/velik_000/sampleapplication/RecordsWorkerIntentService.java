package com.example.velik_000.sampleapplication;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by velik_000 on 02/17/2017.
 */

public class RecordsWorkerIntentService extends IntentService {

    private String TAG = "IncBCST";

    public RecordsWorkerIntentService() {
        super("RecordsWorkerIntentService");
        setIntentRedelivery(true);
    }

    public RecordsWorkerIntentService(String name) {
        super(name);
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created!");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service started!");
        String type = intent.getStringExtra("type");
        if(type != null) {
            switch (type) {
                case "write": {
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
                    break;
                }
                case "notify": {
                    ContentResolver cr = getContentResolver();
                    Cursor notSavedRecods = cr.query(RecordsContentProvider.CONTENT_URI, new String[]{RecordsTable.COLUMN_INFO, RecordsTable.COLUMN_SAVED}, RecordsTable.COLUMN_SAVED + "=?", new String[]{"not saved"}, null);
                    Cursor savedRecods = cr.query(RecordsContentProvider.CONTENT_URI, new String[]{RecordsTable.COLUMN_INFO, RecordsTable.COLUMN_SAVED}, RecordsTable.COLUMN_SAVED + "=?", new String[]{"saved"}, null);
                    if (notSavedRecods != null && savedRecods != null) {
                        Log.d(TAG, "Saved records: " + savedRecods.getCount() + "\nNot saved records: " + notSavedRecods.getCount());
                        NotificationCompat.Builder notificationBuilder =
                                new NotificationCompat.Builder(getApplicationContext())
                                        .setSmallIcon(R.drawable.notification_icon)
                                        .setContentTitle("Records")
                                        .setContentText("Saved records: " + savedRecods.getCount() + "\nNot saved records: " + notSavedRecods.getCount());
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(123, notificationBuilder.build());
                        notSavedRecods.close();
                        savedRecods.close();
                    } else {
                        if(notSavedRecods != null) {
                            notSavedRecods.close();
                        }
                        if(savedRecods != null) {
                            savedRecods.close();
                        }
                        Log.d(TAG, "ERROR");
                    }

                    break;
                }
                default:

                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed!");
    }
}
