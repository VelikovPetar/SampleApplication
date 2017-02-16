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

    public CallAndSMSBroadcastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("IncMsg", "BROADCAST RECEIVED");
        this.context = context;
        String action = intent.getAction();
        if(action.equals(ACTION_PHONE)) {  // Ako e primen broadcast od PHONE
            CustomPhoneStateListener cpsl = new CustomPhoneStateListener();
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // Proveri ja sostojbata na povikot
            tm.listen(cpsl, PhoneStateListener.LISTEN_CALL_STATE);
        } else if(action.equals(ACTION_SMS)) {  // Ako e primen broadcast od SMS
            Log.d("IncMsg", "INSIDE MSG PART");
            Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    Log.d("IncMsg", "BUNDLE NOT NULL");
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus != null) {
                        Log.d("IncMsg", "PDUS NOT NULL");
                        for (int i = 0; i < pdus.length; ++i) {
                            Log.d("IncMsg", "IN FOR LOOP");
                            SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            String phoneNumber = message.getDisplayOriginatingAddress();
                            Log.d("IncMsg", "Incoming message from " + phoneNumber);
                        }
                    }
                }
            }catch (Exception e) {
                Log.e("IncSms", "Exception " + e);
            }
        }
    }

    private class CustomPhoneStateListener extends PhoneStateListener {

        private String TAG = "CustomPSL";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // Spravi se so sostojbata na povikot soodvetno
            if(state == TelephonyManager.CALL_STATE_RINGING) {
                Log.d(TAG, "Incoming call from " + incomingNumber);
            } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                Log.d(TAG, "OFF_HOOK");
            } else if(state == TelephonyManager.CALL_STATE_IDLE) {
                Log.d(TAG, "IDLE");
            }
        }
    }
}
