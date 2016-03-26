package com.ringerbell;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    /** The logger */
    private static final String LOG_TAG = SMSReceiver.class.getName();

    final SmsManager sms = SmsManager.getDefault();

    public static final String SMS_BUNDLE = "pdus";

    SharedPreferences prefs;

    String keyMessage = null;

    public void onReceive(Context context, Intent intent) {
        Log.i("sm received", "hello");

        prefs = context.getSharedPreferences("RINGER_BELL", Context.MODE_PRIVATE);
        keyMessage = prefs.getString("KEY_MESSAGE", "");

        final Bundle bundle = intent.getExtras();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        EndCallListener callListener = new EndCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);

        try {
            if (bundle != null) {
                final Object[] sms = (Object[]) bundle.get(SMS_BUNDLE);
                if(null != sms) {
                    for (int i = 0; i < sms.length; i++) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                        String senderNumber = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();
                        if(message.equals(keyMessage)) {
                            Log.i("SmsReceiver", "senderNum: " + senderNumber + "; message: " + message);
                            changeAudioMode(context, senderNumber, message, audioManager);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
        }
    }

    /**
     * Change audio manager mode
     * @param context
     *      application context
     */
    private void changeAudioMode(Context context, String senderNumber, String message, AudioManager audioManager) {
        // Retrieves a map of extended data from the intent.
        switch (audioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                //audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                //raiseVolume(audioManager);
                audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                //audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),0);
                audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                break;
        }
    }

    private  void raiseVolume(AudioManager audioManager) {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        //if(maxVolume != currentVolume) {
            audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), 0);
        //}

        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.STREAM_RING);
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.STREAM_ALARM);
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.STREAM_SYSTEM);
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.STREAM_NOTIFICATION);
    }

    private class EndCallListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if(TelephonyManager.CALL_STATE_RINGING == state) {
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }
            if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
                //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
                Log.i(LOG_TAG, "OFFHOOK");
            }
            if(TelephonyManager.CALL_STATE_IDLE == state) {
                //when this state occurs, and your flag is set, restart your app
                Log.i(LOG_TAG, "IDLE");
            }
        }

    }
}
