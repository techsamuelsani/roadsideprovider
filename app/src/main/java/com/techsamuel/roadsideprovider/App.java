package com.techsamuel.roadsideprovider;

import android.app.Application;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.CommonRequests;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppSharedPreferences.init(this);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
           // CommonRequests.getIdByPhone(this,FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        }

    }
}
