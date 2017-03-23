package com.app.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.app.listeners.SmsReceivedListner;

public class IncomingSms extends BroadcastReceiver {
    private SmsReceivedListner smsReceived = null;

    private static String PROVIDER_NAME = "MktApp";

    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    try {
                        if (senderNum.contains(PROVIDER_NAME)) {
                            message = message.replaceAll("\\D+", "");
                            smsReceived.onSmsReceived(message);
                            //SellerVerificationActivity Sms = new SellerVerificationActivity();
                            //Sms.recivedSms(message,context );
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnSmsReceivedListener(Context context) {
        this.smsReceived = (SmsReceivedListner) context;
    }
}
