package com.example.velik_000.sampleapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallAndSMSBroadcastReceiver extends BroadcastReceiver {

    private Context context;
    private final String ACTION_PHONE = "android.intent.action.PHONE_STATE";
    private final String ACTION_SMS = "android.provider.Telephony.SMS_RECEIVED";
    final SmsManager sms = SmsManager.getDefault();
    private String TAG = "IncBCST";

    public CallAndSMSBroadcastReceiver() {

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "BROADCAST RECEIVED");
        this.context = context;
        final Context _context = context;
        final String action = intent.getAction();
        if(action.equals(ACTION_PHONE)) {  // Ako e primen broadcast od PHONE
            String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if(phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                // Staruvaj IntentService koj preku ContentResolver -> ContentProvider zapishuva vo baza
                // -----------------------------------------------------------------------------
                // Intent startServiceIntent = new Intent(_context, RecordsWorkerService.class);
                Intent startServiceIntent = new Intent(_context, RecordsWorkerIntentService.class);
                startServiceIntent.putExtra("type", "write");
                startServiceIntent.putExtra("info", "Incoming call from: " + intent.getStringExtra("incoming_number"));
                startServiceIntent.putExtra("saved", "not saved");
                context.startService(startServiceIntent);
                Log.d(TAG, "Incoming call from " + intent.getStringExtra("incoming_number"));
            }
        } else if(action.equals(ACTION_SMS)) {  // Ako e primen broadcast od SMS
            Log.d(TAG, "INSIDE MSG PART");
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Log.d(TAG, "BUNDLE NOT NULL");
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    Log.d(TAG, "PDUS NOT NULL");
                    String phoneNumber = "";
                    for (int i = 0; i < pdus.length; ++i) {
                        Log.d(TAG, "IN FOR LOOP");
                        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        phoneNumber = message.getDisplayOriginatingAddress();  // Zemi go isprakjachot
                        Log.d(TAG, "Incoming message from " + phoneNumber);
                    }
                    final String incomingNumber = phoneNumber;
                    // Staruvaj IntentService koj preku ContentResolver -> ContentProvider zapishuva vo baza
                    // -----------------------------------------------------------------------------
                    // Intent startServiceIntent = new Intent(_context, RecordsWorkerService.class);
                    Intent startServiceIntent = new Intent(_context, RecordsWorkerIntentService.class);
                    startServiceIntent.putExtra("type", "write");
                    startServiceIntent.putExtra("info", "Incoming SMS from: " + incomingNumber);
                    startServiceIntent.putExtra("saved", "not saved");
                    context.startService(startServiceIntent);
                    // -----------------------------------------------------------------------------
                }
            }
        }
        Log.d(TAG, "BROADCAST FINISHED");
    }
}
