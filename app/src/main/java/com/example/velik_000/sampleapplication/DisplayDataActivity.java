package com.example.velik_000.sampleapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by velik_000 on 03/05/2017.
 */

public class DisplayDataActivity extends Activity {

    private Button searchByIdButton;
    private EditText enterIdEditText;

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LIFE", "DISPLAY Activity onPause()");
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

    //TODO Save data on orientation change(?)
    //TODO Check for internet connection and display error
    private class SearchByIdButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String url = "<ENTER URL HERE>";
            final TextView textView = (TextView) findViewById(R.id.id_edit_text);
            String id = textView.getText().toString();
            Log.d("VEHICLE API", id);
            if(id.trim().equals("")) {
                Toast.makeText(getApplicationContext(), "No ID entered!", Toast.LENGTH_LONG).show();
                return;
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + id, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if(response.has("ResponseCode")) {
                            //TODO Fix not existing IDs
                            Toast.makeText(getApplicationContext(), (String) response.get("ResponseDescription"), Toast.LENGTH_LONG).show();
                            textView.setText("");
                        } else {
                            //TODO Use resource string with placeholders
                            int id = response.getInt("IdVehicle");
                            ((TextView)findViewById(R.id.retrieved_id_text)).setText(id + "");

                            String plate = response.getString("Plate");
                            ((TextView)findViewById(R.id.retrieved_plate_number_text)).setText(plate);

                            String description = (String) response.get("DescriptionShort");
                            ((TextView)findViewById(R.id.retrieved_decription_text)).setText(description);

                            double longitudeX = response.getDouble("LongitudeX");
                            ((TextView)findViewById(R.id.retrieved_longitudex_text)).setText(longitudeX + "");

                            double latitudeY = response.getDouble("LatitudeY");
                            ((TextView)findViewById(R.id.retrieved_latitudey_text)).setText(latitudeY + "");

                            double bearing = response.getDouble("Bearing");
                            ((TextView)findViewById(R.id.retrieved_bearing_text)).setText(bearing + "");

                            String speed = response.getString("Speed");
                            ((TextView)findViewById(R.id.retrieved_speed_text)).setText(speed);

                            String vehicleState = response.getString("VehicleState");
                            ((TextView)findViewById(R.id.retrieved_vehicle_state_text)).setText(vehicleState);

                            int vehicleStateColor = response.getInt("VehicleStateColor");
                            ((TextView)findViewById(R.id.retrieved_vehicle_state_color_text)).setText(vehicleStateColor + "");

                            String driver = response.getString("Driver");
                            ((TextView)findViewById(R.id.retrieved_driver_text)).setText(driver);

                            String driverStart = response.getString("DriverStart");
                            ((TextView)findViewById(R.id.retrieved_driver_start_text)).setText(driverStart);

                            String taximeter = response.getString("Taximeter");
                            ((TextView)findViewById(R.id.retrieved_taximeter_text)).setText(taximeter);

                            String lastLocation = response.getString("LastLocation");
                            ((TextView)findViewById(R.id.retrieved_last_location_text)).setText(lastLocation);

                            String stanica = response.getString("Stanica");
                            ((TextView)findViewById(R.id.retrieved_stanica_text)).setText(stanica);

                            String region = response.getString("Region");
                            ((TextView)findViewById(R.id.retrieved_region_text)).setText(region);

                            String doRegion = response.getString("DoRegion");
                            ((TextView)findViewById(R.id.retrieved_do_region_text)).setText(doRegion);

                            String adresa1 = response.getString("Adresa1");
                            ((TextView)findViewById(R.id.retrieved_adresa_text)).setText(adresa1);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("VEHICLE API", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error during GET", Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }
}
