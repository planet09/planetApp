package com.example.annew.planet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "BroadcastReceiver Received");
        Intent move= new Intent( context,SendDataService.class);


        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            Object[] messages = (Object[])bundle.get("pdus");
            SmsMessage[] smsMessage = new SmsMessage[messages.length];


            for(int i = 0; i < messages.length; i++) {
                smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);

            }
            String number= smsMessage[0].getOriginatingAddress().toString();
            //01041319091
            //[신한]14:37 11038***1989 입금23,000원 잔액 220,000 손성은
            if(number.equals("01099090954")){

            String message = smsMessage[0].getMessageBody().toString();
            int target_num= message.indexOf("입금");
            String money =message.substring(target_num+3,(message.substring(target_num).indexOf("원")+target_num));
            String[] array = message.split("원\n");
            String name = array[array.length-1];

                Log.d(TAG, "SMS Message:" +money+name+"number"+number);
            move.putExtra("msg", message);
                move.putExtra("money", money);
                move.putExtra("name", name);
                context.startService(move);}
        }


    }

}
