package com.techsamuel.roadsideprovider.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.MessageActivity;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppFirebaseInstanceIdService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
        AppSharedPreferences.init(this);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        AppSharedPreferences.write(Config.SHARED_PREF_KEY_FCM,s);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("FirebaseFcm","Received");

        String title=remoteMessage.getData().get("title");
        String message=remoteMessage.getData().get("message");
        String type=remoteMessage.getData().get("type");
        String id=remoteMessage.getData().get("id");
        String icon=remoteMessage.getData().get("icon");
        String cover=remoteMessage.getData().get("cover");

        showPushNotification(title,message,type,id,icon,cover);

    }

    private void showPushNotification(
            String title,
            String message,
            String type,
            String id,
            String icon,
            String cover
            ) {

        int NOTIFICATION_ID = Tools.generateRandomInt(1,100000);
        if(type.equals("order")){
            NOTIFICATION_ID = Integer.parseInt(id);
        }

        Bitmap coverImage = getBitmapfromUrl(cover);
        Bitmap iconImage=getBitmapfromUrl(icon);



        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "roadside-provider";
        CharSequence name = "Roadside Provider";
        String Description = "This is Roadside Provider App Notification Channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_attractions_24)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);


        if (coverImage!=null){
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(coverImage));

        }
        if (iconImage!=null){
            builder.setLargeIcon(iconImage);
        }
        Intent resultIntent=null;
        if(type.equals("order")){
            //resultIntent = new Intent(this, PreOrderDetailsActivity.class);
            resultIntent.putExtra("order_id",id);
        }else if(type.equals("admin")){
            resultIntent = new Intent(this, MessageActivity.class);
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
