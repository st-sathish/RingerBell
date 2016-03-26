package com.ringerbell;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ringerbell.dao.ContactDAO;
import com.ringerbell.daoImpl.ContactDAOImpl;
import com.ringerbell.db.DatabaseHelper;

import java.lang.reflect.Method;

public class PhoneCallReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneCallReceiver";

    private TelephonyManager telephonyManager;

    private int currentMode = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (intent.getAction().equals("android.intent.action.PHONE_STATE"))  {
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING))  {
                    // Get incoming number
                    String incomingNumber =  intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    System.out.println(incomingNumber);
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                    } else {
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    }*/
                    ContactDAO contactDAO = new ContactDAOImpl(context);
                    if ((incomingNumber != null)) {
                        Contact contact = contactDAO.getContactByMobileNumber(incomingNumber.trim());
                        if(null != contact) {
                            System.out.println(contact.getPhoneNumber());
                        }
                        currentMode = audioManager.getRingerMode();
                        if(null != contact && (contact.getBlocked() == 1)) {
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                            Log.d("HANG UP", incomingNumber);
                        } else {
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                        //audioManager.setStreamVolume(AudioManager.RINGER_MODE_SILENT, audioManager.getStreamMaxVolume(AudioManager.RINGER_MODE_SILENT), 0);
                        //audioManager.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.ADJUST_MUTE);
                        //rejectCall();
                    }
                } else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    audioManager.setRingerMode(currentMode);
                } else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    audioManager.setRingerMode(currentMode);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void rejectCall(){
        try {
            // Get the getITelephony() method
            Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method method = classTelephony.getDeclaredMethod("getITelephony");
            // Disable access check
            method.setAccessible(true);
            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = method.invoke(telephonyManager);
            // Get the endCall method from ITelephony
            Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
