package com.techsamuel.roadsideprovider.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.techsamuel.roadsideprovider.model.OrderModel;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.SettingsModel;

public class AppSharedPreferences {
    private static SharedPreferences mSharedPref;

    private AppSharedPreferences()
    {

    }

    public static void init(Context context)
    {
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public static void clear(){
        mSharedPref.edit().clear().commit();
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static boolean read(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public static Integer read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).commit();
    }
    public static ProviderModel readProviderModel(String key,String defValue){
        Gson gson = new Gson();
        String json = mSharedPref.getString(key, defValue);
        ProviderModel providerModel = gson.fromJson(json, ProviderModel.class);
        return providerModel;

    }

    public static void writeProviderModel(String key,ProviderModel providerModel){
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(providerModel);
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public static SettingsModel readSettingsModel(String key, String defValue){
        Gson gson = new Gson();
        String json = mSharedPref.getString(key, defValue);
        SettingsModel settingsModel = gson.fromJson(json, SettingsModel.class);
        return settingsModel;
    }

    public static void writeSettingsModel(String key,SettingsModel settingsModel){
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(settingsModel);
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public static OrderModel readOrderModel(String key, String defValue){
        Gson gson = new Gson();
        String json = mSharedPref.getString(key, defValue);
        OrderModel orderModel = gson.fromJson(json, OrderModel.class);
        return orderModel;
    }

    public static void writeOrderModel(String key,OrderModel orderModel){
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(orderModel);
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }



}
