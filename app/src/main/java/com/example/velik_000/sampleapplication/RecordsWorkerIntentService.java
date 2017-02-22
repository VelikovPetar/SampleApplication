package com.example.velik_000.sampleapplication;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
    private final int NOTIFICATION_ID = 1234;

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
        String action = intent.getAction();
        if(action != null) {
            switch (action) {
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
                    Cursor savedRecords = cr.query(RecordsContentProvider.CONTENT_URI, new String[]{RecordsTable.COLUMN_INFO, RecordsTable.COLUMN_SAVED},
                            RecordsTable.COLUMN_SAVED + "=?", new String[]{"saved"}, null);
                    Cursor notSavedRecords = cr.query(RecordsContentProvider.CONTENT_URI, new String[]{RecordsTable.COLUMN_INFO, RecordsTable.COLUMN_SAVED},
                            RecordsTable.COLUMN_SAVED + "=?", new String[]{"not saved"}, null);
                    if (notSavedRecords != null && savedRecords != null) {
                        int savedRecordsCount = savedRecords.getCount();
                        int notSavedrecordsCount = notSavedRecords.getCount();
                        Log.d(TAG, "Saved records: " + savedRecords.getCount() + "\nNot saved records: " + notSavedRecords.getCount());

                        // Send notification
                        sendNotification(this, savedRecordsCount, notSavedrecordsCount);

                        notSavedRecords.close();
                        savedRecords.close();
                    } else {
                        if(notSavedRecords != null) {
                            notSavedRecords.close();
                        }
                        if(savedRecords != null) {
                            savedRecords.close();
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

    private void sendNotification(Context context, int savedRecordsCount, int notSavedRecordsCount) {
        Intent onViewButtonClickIntent = new Intent(context, MainActivity.class);
//        onViewButtonClickIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, onViewButtonClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder
                = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Records")
                .setContentText("Saved records: " + savedRecordsCount + "\nNot saved records: " + notSavedRecordsCount)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
