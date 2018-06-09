package com.unicef.vhn.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.unicef.vhn.activity.SplashScreenActivity;

/**
 * Created by suthishan on 5/19/2018.
 */

public class GpsLocationReceiver extends BroadcastReceiver {
//    private GpsStatusDetector mGpsStatusDetector;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {

            /*Toast.makeText(context, "in android.location.PROVIDERS_CHANGED",
                    Toast.LENGTH_SHORT).show();
            Intent pushIntent = new Intent(context, SplashScreenActivity.class);
            pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(pushIntent);*/

            Intent pushIntent = new Intent(context, SplashScreenActivity.class);
            context.startService(pushIntent);
//            mGpsStatusDetector = new GpsStatusDetector((Activity) context.getApplicationContext());
//            mGpsStatusDetector.checkGpsStatus();
        }
    }
  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGpsStatusDetector.checkOnActivityResult(requestCode, resultCode);
    }

    @Override
    public void onGpsSettingStatus(boolean enabled) {
        Log.d("TAG", "onGpsSettingStatus: " + enabled);
        if(!enabled){
            mGpsStatusDetector.checkGpsStatus();
        }
    }

    @Override
    public void onGpsAlertCanceledByUser() {
        Log.d("TAG", "onGpsAlertCanceledByUser");
    }*/
}
