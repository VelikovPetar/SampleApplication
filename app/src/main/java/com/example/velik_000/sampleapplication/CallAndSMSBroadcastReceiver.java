package com.example.velik_000.sampleapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
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
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "BROADCAST RECEIVED");
        this.context = context;
        String action = intent.getAction();
        if(action.equals(ACTION_PHONE)) {  // Ako e primen broadcast od PHONE
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // Proveri ja sostojbata na povikot
            tm.listen(new PhoneStateListener(){
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    if(state == TelephonyManager.CALL_STATE_RINGING) {
                        // TODO Zapishi vo baza
                        Log.d(TAG, "Incoming call from " + incomingNumber);
                    } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                        Log.d(TAG, "OFF_HOOK");
                    } else if(state == TelephonyManager.CALL_STATE_IDLE) {
                        Log.d(TAG, "IDLE");
                    }
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);
        } else if(action.equals(ACTION_SMS)) {  // Ako e primen broadcast od SMS
            Log.d(TAG, "INSIDE MSG PART");
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Log.d(TAG, "BUNDLE NOT NULL");
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    Log.d(TAG, "PDUS NOT NULL");
                    for (int i = 0; i < pdus.length; ++i) {
                        Log.d(TAG, "IN FOR LOOP");
                        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String phoneNumber = message.getDisplayOriginatingAddress();
                        Log.d(TAG, "Incoming message from " + phoneNumber);
                    }
                    // TODO Zapishi vo baza
                }
            }
        }
    }

}
