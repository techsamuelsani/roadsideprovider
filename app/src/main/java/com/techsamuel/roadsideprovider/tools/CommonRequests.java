package com.techsamuel.roadsideprovider.tools;


import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.MainActivity;
import com.techsamuel.roadsideprovider.activity.register.RegisterStepFiveActivity;
import com.techsamuel.roadsideprovider.activity.register.RegisterStepFourActivity;
import com.techsamuel.roadsideprovider.activity.register.RegisterStepThreeActivity;
import com.techsamuel.roadsideprovider.activity.register.RegisterStepTwo;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonRequests {


    public static void goToStep(Context context, FirebaseUser firebaseUser){

        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<DataSavedModel> call=apiInterface.checkRegistrationStep(
                firebaseUser.getPhoneNumber()
        );
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                if(response.body().getStatus()== Config.API_FAILED){
                    String allMessage="";
                    for(String message:response.body().getMessage()){
                        allMessage+="\n"+message;
                    }
                    Tools.showToast(context,allMessage);

                }else{
                    AppSharedPreferences.init(context);
                    AppSharedPreferences.write(Config.SHARED_PREF_PROVIDER_ID,response.body().getId().toString());
                    String step=response.body().getMessage().get(0);
                    //Tools.showToast(context,step);
                    if(step.equals("STEP1")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Want to continue with "+firebaseUser.getPhoneNumber()+" this phone number?");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(context, RegisterStepTwo.class);
                                intent.putExtra("phoneNumber",firebaseUser.getPhoneNumber());
                                intent.putExtra("firebaseId",firebaseUser.getUid());
                                if(intent!=null){
                                    context.startActivity(intent);
                                }

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();

                    }else if(step.equals("STEP5")){
                       getProviderByPhone(context);
                       //Tools.showToast(context,"Going to STEP5");

                    }else{
                        Intent intent=null;
                        if(step.equals("STEP2")){
                            intent=new Intent(context, RegisterStepThreeActivity.class);

                        }else if(step.equals("STEP3")){
                            intent=new Intent(context, RegisterStepFourActivity.class);

                        }else if(step.equals("STEP4")){
                            intent=new Intent(context, RegisterStepFiveActivity.class);
                        }

                        if(intent!=null){
                            intent.putExtra("phoneNumber",firebaseUser.getPhoneNumber());
                            intent.putExtra("firebaseId",firebaseUser.getUid());
                            context.startActivity(intent);
                        }


//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setTitle("Want to reload your previous data?");
//                        builder.setMessage("Our system detected that you have unfinished registration, to start from saved registration click Ok " +
//                                "or if you want to start from afresh click Cancel");
//                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                            }
//                        });
//                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//                        builder.show();


                    }

                }
            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                Log.d("ApiError",t.getMessage().toString());
                Tools.showToast(context,t.getMessage());

            }
        });

    }


    public static void getIdByPhone(Context context,String providerPhone){
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<DataSavedModel> call=apiInterface.getIdByPhone(providerPhone);
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                //Tools.showToast(RegisterStepFourActivity.this,response.body().getMessage().toString());
                AppSharedPreferences.init(context);
                if(response.body().getStatus()==Config.API_SUCCESS){
                    if(response.body().getId()!=null){
                        AppSharedPreferences.write(Config.SHARED_PREF_PROVIDER_ID,response.body().getId().toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                Tools.showToast(context,"Retrofit Connecton Failed");
            }
        });
    }

    public static void updateLocationToServer(Context context, LatLng location){
        AppSharedPreferences.init(context);
        String latitude= String.valueOf(location.getLatitude());
        String longitude= String.valueOf(location.getLongitude());
        String fcm=AppSharedPreferences.read(Config.SHARED_PREF_KEY_FCM,"");
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        String providerId=AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,"");
        Call<DataSavedModel> call=apiInterface.updateLocationToServer(providerId,latitude,longitude);
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                Tools.showToast(context,response.body().getMessage().toString());
            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                    Log.d("LocationAndFcmToServer",t.getMessage().toString());
            }
        });

    }

    public static void updateFcmToServer(Context context){
        AppSharedPreferences.init(context);
        String fcm=AppSharedPreferences.read(Config.SHARED_PREF_KEY_FCM,"");
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        String providerId=AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,"");
        Call<DataSavedModel> call=apiInterface.updateFcmToServer(providerId,fcm);
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                Tools.showToast(context,response.body().getMessage().toString());
            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                Log.d("FcmToServer",t.getMessage().toString());
            }
        });

    }

    public static void getProviderByPhone(Context context){
        AppSharedPreferences.init(context);
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<ProviderModel> call=apiInterface.getProviderByPhone(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        call.enqueue(new Callback<ProviderModel>() {
            @Override
            public void onResponse(Call<ProviderModel> call, Response<ProviderModel> response) {
                if(response.body().getStatus()==Config.API_SUCCESS){
                    AppSharedPreferences.writeProviderModel(Config.SHARED_PREF_PROVIDER_MODEL,response.body());
                    AppSharedPreferences.write(Config.SHARED_PREF_PROVIDER_ID,response.body().getId());
                    Intent intent=new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<ProviderModel> call, Throwable t) {
                Log.d("ProviderModel",t.getMessage().toString());
            }
        });

    }


}
