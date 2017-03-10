package com.example.velik_000.sampleapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by velik_000 on 03/05/2017.
 */

public class DisplayDataActivity extends Activity {

    private Button searchByIdButton;
    private EditText enterIdEditText;
    private ResponseBroadcastReceiver responseBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_display_data);

        Log.d("LIFE", "DISPLAY Activity onCreate()");

        searchByIdButton = (Button) findViewById(R.id.get_from_api_button);
        searchByIdButton.setOnClickListener(new SearchByIdButtonHandler());

        enterIdEditText = (EditText) findViewById(R.id.id_edit_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LIFE", "DISPLAY Activity onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LIFE", "DISPLAY Activity onResume()");
        // Register the local ResponseBroadcastReceiver
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        responseBroadcastReceiver = new ResponseBroadcastReceiver();
        localBroadcastManager.registerReceiver(responseBroadcastReceiver, new IntentFilter(HttpRequestIntentService.ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LIFE", "DISPLAY Activity onPause()");
        // Unregister the local ResponseBroadcastReceiver
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(responseBroadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LIFE", "DISPLAY Activity onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LIFE", "DISPLAY Activity onDestroy()");
    }

    private class ResponseBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String json = intent.getStringExtra("json_string");
            try {
                JSONObject response = new JSONObject(json);
                displayData(response);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("HTTP_SERVICE", "BAD JSON");
            }
        }

        private void displayData(JSONObject response) throws JSONException {
            int id = response.getInt("IdVehicle");
            ((TextView) findViewById(R.id.retrieved_id_text)).setText(id + "");

            String plate = response.getString("Plate");
            ((TextView) findViewById(R.id.retrieved_plate_number_text)).setText(plate);

            String description = (String) response.get("DescriptionShort");
            ((TextView) findViewById(R.id.retrieved_decription_text)).setText(description);

            double longitudeX = response.getDouble("LongitudeX");
            ((TextView) findViewById(R.id.retrieved_longitudex_text)).setText(longitudeX + "");

            double latitudeY = response.getDouble("LatitudeY");
            ((TextView) findViewById(R.id.retrieved_latitudey_text)).setText(latitudeY + "");

            double bearing = response.getDouble("Bearing");
            ((TextView) findViewById(R.id.retrieved_bearing_text)).setText(bearing + "");

            String speed = response.getString("Speed");
            ((TextView) findViewById(R.id.retrieved_speed_text)).setText(speed);

            String vehicleState = response.getString("VehicleState");
            ((TextView) findViewById(R.id.retrieved_vehicle_state_text)).setText(vehicleState);

            int vehicleStateColor = response.getInt("VehicleStateColor");
            ((TextView) findViewById(R.id.retrieved_vehicle_state_color_text)).setText(vehicleStateColor + "");

            String driver = response.getString("Driver");
            ((TextView) findViewById(R.id.retrieved_driver_text)).setText(driver);

            String driverStart = response.getString("DriverStart");
            ((TextView) findViewById(R.id.retrieved_driver_start_text)).setText(driverStart);

            String taximeter = response.getString("Taximeter");
            ((TextView) findViewById(R.id.retrieved_taximeter_text)).setText(taximeter);

            String lastLocation = response.getString("LastLocation");
            ((TextView) findViewById(R.id.retrieved_last_location_text)).setText(lastLocation);

            String stanica = response.getString("Stanica");
            ((TextView) findViewById(R.id.retrieved_stanica_text)).setText(stanica);

            String region = response.getString("Region");
            ((TextView) findViewById(R.id.retrieved_region_text)).setText(region);

            String doRegion = response.getString("DoRegion");
            ((TextView) findViewById(R.id.retrieved_do_region_text)).setText(doRegion);

            String adresa1 = response.getString("Adresa1");
            ((TextView) findViewById(R.id.retrieved_adresa_text)).setText(adresa1);
        }
    }

    //TODO Save data on orientation change(?)
    private class SearchByIdButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isConnected(getApplicationContext())) {
                promptInternetConnection(DisplayDataActivity.this);
                return;
            }
            String url = "http://62.162.54.10:1100/GetVehicleStatesByIdVehicle/?pIdVehicle=";
            String id = enterIdEditText.getText().toString();
            if (id.trim().equals("")) {
                Toast.makeText(getApplicationContext(), "No ID entered!", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(DisplayDataActivity.this, HttpRequestIntentService.class);
            intent.putExtra("url", url + id);
            startService(intent);
        }


        // Check if device is connected to internet
        private boolean isConnected(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return false;
            }
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }

        // Prompt the user to connect to the internet
        private void promptInternetConnection(Context context) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Enable internet connectivity");
            builder.setMessage("Internet connection is required to perfor this action. Connect via:");
            builder.setPositiveButton("WiFi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }
            });
            // TODO Check for different versions of Android
            builder.setNeutralButton("Mobile Data", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(
                            "com.android.settings",
                            "com.android.settings.Settings$DataUsageSummaryActivity"));
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();
        }
    }
}
