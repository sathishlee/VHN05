package com.unicef.vhn.utiltiy;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MainActivity;
import com.unicef.vhn.constant.Apiconstants;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.google.android.gms.wearable.DataMap.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

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

        private void sendNotification(String message){
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.putExtra("AnotherActivity", TrueOrFalse);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

            Uri u = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher));
            builder.setSmallIcon(R.drawable.ic_launcher);

//            builder.setStyle(new NotificationCompat.BigPictureStyle()
//                    .bigPicture(image))/*Notification with Image*/
//                    .setAutoCancel(true);

            builder.setContentTitle("VHN");
//            builder.setLargeIcon(R.drawable.mother);
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setSound(u);
            builder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }

//    public Bitmap getBitmapfromUrl(String imageUrl) {
//        try {
//            URL url = new URL(imageUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap bitmap = BitmapFactory.decodeStream(input);
//            return bitmap;
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//
//        }
//    }
}
