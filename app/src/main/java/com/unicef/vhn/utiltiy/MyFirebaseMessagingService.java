package com.unicef.vhn.utiltiy;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MainActivity;
import com.unicef.vhn.activity.PushNotifylistActivity;
import com.unicef.vhn.activity.SosAlertListActivity;
import com.unicef.vhn.constant.AppConstants;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //    Realm realm;
    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        String message = remoteMessage.getData().get("message");
//        //imageUri will contain URL of the image to be displayed with Notification
//        String imageUri = remoteMessage.getData().get("image");
//        //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
//        //If the key AnotherActivity has  value as False then when the user taps on notification, in the app MainActivity will be opened.
//        String TrueOrFlase = remoteMessage.getData().get("AnotherActivity");
//
//        //To get a Bitmap image from the URL received
//        bitmap = getBitmapfromUrl(imageUri);
//
//        sendNotification(message, bitmap, TrueOrFlase);


        sendNotification(remoteMessage.getNotification().getBody());


    }

    private void sendNotification(String message) {
        /*Log.e("FirebaseMessaging", "recived push notofication message >>>>>>>>>" + message);

        Intent resultIntent = new Intent(this, PushNotifylistActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
               stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri u = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        builder.setSmallIcon(R.drawable.ic_launcher);



        builder.setContentTitle("VHN");
//            builder.setLargeIcon(R.drawable.mother);
        builder.setContentText(message);
        builder.setAutoCancel(true);
        builder.setSound(u);
//        builder.setContentIntent(pendingIntent);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());*/
        // Creates an explicit intent for an Activity
//        Intent resultIntent = new Intent(this, PushNotifylistActivity.class);
        Intent resultIntent = new Intent(this, MainActivity.class);
        AppConstants.OPENFRAGMENT ="00";
//        Intent resultIntent = new Intent(this, SosAlertListActivity.class);
        Bundle bundle =new Bundle();
        bundle.putString("fcm",message);
        resultIntent.putExtras(bundle);
        // Creating a artifical activity stack for the notification activity
        android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PushNotifylistActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        // Pending intent to the notification manager
        PendingIntent resultPending = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Building the notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mother) // notification icon
                .setContentTitle("VHN") // main title of the notification
                .setContentText(message) // notification text
                .setContentIntent(resultPending); // notification intent
        NotificationManager mNotificationManager =    (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(10, mBuilder.build());
    }


}
