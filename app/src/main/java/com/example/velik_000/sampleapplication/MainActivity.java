package com.example.velik_000.sampleapplication;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private final int PHONE_SMS_PERMISSION_REQUEST_CODE = 1;
    private Button notifyRecordsButton;
    private Button displayRecordsButton;
    private ListView recordsListView;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("LIFE", "Activity onCreate()");

        notifyRecordsButton = (Button) findViewById(R.id.notify_records_button);
        notifyRecordsButton.setOnClickListener(new NotifyRecordsButtonHandler());

        displayRecordsButton = (Button) findViewById(R.id.display_records_button);
        displayRecordsButton.setOnClickListener(new DisplayRecordsButtonHandler());

        recordsListView = (ListView) findViewById(R.id.records_list_view);

        Object object = getLastNonConfigurationInstance();
        if(object != null) {
            Cursor cursor = (Cursor) object;
            recordsListView.setAdapter(new RecordsCursorAdapter(this, cursor));
        }
        // Ask for permissions at runtime for Android 6.0+
        int phonePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int smsPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if(phonePermissionCheck != PackageManager.PERMISSION_GRANTED || smsPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS},PHONE_SMS_PERMISSION_REQUEST_CODE);
        }

//        ContentValues cv = new ContentValues();
//        cv.put(RecordsTable.COLUMN_INFO, "info");
//        cv.put(RecordsTable.COLUMN_SAVED, "not saved");

//        ContentResolver cr = getContentResolver();
//        cr.insert(RecordsContentProvider.CONTENT_URI, cv);
//        cr.delete(RecordsContentProvider.CONTENT_URI, RecordsTable.COLUMN_SAVED + " LIKE ?", new String[]{"%%"});
//        Cursor cursor = cr.query(RecordsContentProvider.CONTENT_URI, new String[]{RecordsTable.COLUMN_INFO}, null, null, null);
//        if(cursor != null) {
//            Log.d("CURSOR TEST", cursor.getCount() + "");
//        } else {
//            Log.e("CURSOR TEST", "NULL");
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LIFE", "Activity onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LIFE", "Activity onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LIFE", "Activity onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LIFE", "Activity onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(cursor != null) {
//            cursor.close();
//        }
        Log.d("LIFE", "Activity onDestroy()");
    }

    // Don't destroy the activity on back-press
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @Override
    public Object onRetainNonConfigurationInstance() {
        Log.d("LIFE", "Activity onRetainNonConfigurationInstance()");
        return cursor;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == PHONE_SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Cannot read INC_CALLS", Toast.LENGTH_SHORT).show();
            }
            if(grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Cannot read INC_SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class NotifyRecordsButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent startServiceIntent = new Intent(getApplicationContext(), RecordsWorkerIntentService.class);
            startServiceIntent.setAction("notify");
            startService(startServiceIntent);
        }
    }

    private class DisplayRecordsButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ContentResolver cr = getContentResolver();
            cursor = cr.query(RecordsContentProvider.CONTENT_URI, new String[] {RecordsTable.COLUMN_ID, RecordsTable.COLUMN_INFO, RecordsTable.COLUMN_SAVED}, null, null, null);
            if(recordsListView.getAdapter() == null) {
                RecordsCursorAdapter rca = new RecordsCursorAdapter(getApplicationContext(), cursor);
                recordsListView.setAdapter(rca);
            } else {
                Cursor oldCursor = ((CursorAdapter) recordsListView.getAdapter()).swapCursor(cursor);
                if(oldCursor != null) {
                    oldCursor.close();
                }
            }
        }
    }

}
