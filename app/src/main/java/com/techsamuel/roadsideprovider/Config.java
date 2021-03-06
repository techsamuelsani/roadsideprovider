package com.techsamuel.roadsideprovider;

import com.techsamuel.roadsideprovider.model.ServiceModel;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public static final String BASE_URL="https://admin.towietr.com/public/";
    public static final String API_BASE_URL=BASE_URL+"api/";
    public static final String IMAGE_URL=BASE_URL+"public/uploads/provider/";
    public static final String API_USERNAME="roadsideapp";
    public static final String API_PASSWORD="RoadSide1643*#";
    public static final String USER_TYPE ="provider" ;
    public static final String SHARED_PREF_SETTINGS_MODEL = "settings_model";
    public static final String SHARED_PREF_DEVICE_ID = "device_id";
    public static final String SHARED_PREF_ORDER_MODEL = "order_model";
    public static final long SPLASH_SCREEN_TIMEOUT = 0;
    public static final String ORDER_TYPE_NONE = "none";
    public static String PAYMENT_TYPE_CASH="cash";
    public static String PAYMENT_TYPE_CARD="card";
    public static  int ORDER_ID = 0;
    public static String ORDER_TYPE_DELIVERY="delivery";//Delivery means Dropoff
    public static String ORDER_TYPE_PICKUP="pickup";
    public static int PHONE_VERIFICATION_CODE=101;
    public static int API_SUCCESS=200;
    public static int API_FAILED=500;
    //All Configuration will go here
    public static long LOCATION_BUTTON_INVISIBLE_TIME=3000; //1 sec =1000
    public static String DEVICE_TYPE="android";
    public static String LANG_CODE="en";




    //All Shared Preferences Key
    public static String SHARED_PREF_KEY_FCM="fcmtoken";
    public static String SHARED_PREF_PROVIDER_ID="provider_id";
    public static String SHARED_PREF_PROVIDER_MODEL="provider_model";


    //Writable Variable Declaratio
    public static List<ServiceModel.Datum> ServiceModelData=new ArrayList<ServiceModel.Datum>();

    public static Double balance=0.0;
}
