package com.techsamuel.roadsideprovider.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.register.RegisterStepOne;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.SettingsModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.hideSystemUI(this);
        AppSharedPreferences.init(this);
        setContentView(R.layout.activity_splash);
        getAllSettings();

    }

    private void getAllSettings(){

        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<SettingsModel> call=apiInterface.getAllSettings(Config.DEVICE_TYPE,Config.LANG_CODE);
        call.enqueue(new Callback<SettingsModel>() {
            @Override
            public void onResponse(Call<SettingsModel> call, Response<SettingsModel> response) {
                //Tools.showToast(MainActivity.this,response.body().getMessage().toString());
                Log.d("SplashActivity",response.body().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    AppSharedPreferences.writeSettingsModel(Config.SHARED_PREF_SETTINGS_MODEL,response.body());
                    goToLoginScreen();
                }else{
                    Tools.showToast(SplashActivity.this,"There is an error in API");
                    Log.d("MainActivity","Failed to update device information");
                }
            }

            @Override
            public void onFailure(Call<SettingsModel> call, Throwable t) {
                Tools.showToast(SplashActivity.this,"No internet connection/server problem");
                Log.d("MainActivity",t.getMessage().toString());

            }
        });


    }

    private void goToLoginScreen(){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent userLogin = new Intent(SplashActivity.this, RegisterStepOne.class);
                startActivity(userLogin);
            }
        }, Config.SPLASH_SCREEN_TIMEOUT);
    }
}