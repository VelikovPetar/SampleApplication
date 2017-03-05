package com.example.velik_000.sampleapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

    private final int PHONE_SMS_PERMISSION_REQUEST_CODE = 1;
    private Button notifyRecordsButton;
    private Button getDataButton;
    private ListView recordsListView;
    private SimpleCursorAdapter sca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Log.d("LIFE", "MAIN Activity onCreate()");

        notifyRecordsButton = (Button) findViewById(R.id.notify_records_button);
        getDataButton = (Button) findViewById(R.id.get_data_button);
        notifyRecordsButton.setOnClickListener(new NotifyRecordsButtonHandler());
        getDataButton.setOnClickListener(new GetDataButtonHandler());

        // Display of all records stored in the base using CursorLoader
        recordsListView = (ListView) findViewById(R.id.records_list_view);
        sca = new SimpleCursorAdapter(this,
                R.layout.records_list_item, null,
                new String[]{RecordsTable.COLUMN_ID, RecordsTable.COLUMN_INFO, RecordsTable.COLUMN_SAVED},
                new int[]{R.id.row_id, R.id.row_info, R.id.row_saved }, 0);
        recordsListView.setAdapter(sca);
        getSupportLoaderManager().initLoader(0, null, this);

        // Ask for permissions at runtime for Android 6.0+
        int phonePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int smsPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if(phonePermissionCheck != PackageManager.PERMISSION_GRANTED || smsPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS},PHONE_SMS_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LIFE", "MAIN Activity onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LIFE", "MAIN Activity onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LIFE", "MAIN Activity onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LIFE", "MAIN Activity onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LIFE", "MAIN Activity onDestroy()");
    }

    // Don't destroy the activity on back-press
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this, RecordsContentProvider.CONTENT_URI, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        sca.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        sca.swapCursor(null);
    }

    private class NotifyRecordsButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent startServiceIntent = new Intent(getApplicationContext(), RecordsWorkerIntentService.class);
            startServiceIntent.setAction("notify");
            startService(startServiceIntent);
        }
    }

    private class GetDataButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), DisplayDataActivity.class);
            startActivity(intent);
        }
    }

}
