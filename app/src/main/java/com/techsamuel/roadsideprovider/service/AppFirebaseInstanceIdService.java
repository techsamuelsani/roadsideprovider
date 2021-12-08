package com.techsamuel.roadsideprovider.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;

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
        Log.d("FirebaseFCM",remoteMessage.getNotification().getBody().toString());
    }
}
