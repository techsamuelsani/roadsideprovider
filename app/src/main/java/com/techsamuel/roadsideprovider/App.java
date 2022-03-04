package com.techsamuel.roadsideprovider;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.auth.FirebaseAuth;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.CommonRequests;

import java.io.IOException;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppSharedPreferences.init(this);
        task.execute();
    }
    AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String advertId = null;
            try{
                advertId = idInfo.getId();
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            return advertId;
        }

        @Override
        protected void onPostExecute(String advertId) {
            AppSharedPreferences.write(Config.SHARED_PREF_DEVICE_ID,advertId);

        }

    };
}
