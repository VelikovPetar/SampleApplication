package com.example.velik_000.sampleapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

    private final int PHONE_SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
