package com.techsamuel.roadsideprovider.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText fullName;
    EditText email;
    EditText phoneNumber;
    EditText services;
    EditText location;
    ImageView profileImage;
    RelativeLayout lytProfilePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initToolbar();
        init();
    }

    private void init(){
        AppSharedPreferences.init(this);
        ProviderModel providerModel=AppSharedPreferences.readProviderModel(Config.SHARED_PREF_PROVIDER_MODEL,"");
        fullName=findViewById(R.id.fullname);
        email=findViewById(R.id.email);
        phoneNumber=findViewById(R.id.phoneNumber);
        services=findViewById(R.id.services);
        location=findViewById(R.id.location);
        profileImage=findViewById(R.id.profile_image);
        lytProfilePhoto=findViewById(R.id.lyt_profile_photo);

        fullName.setText(providerModel.getData().get(0).getName());
        email.setText(providerModel.getData().get(0).getEmail());
        phoneNumber.setText(providerModel.getData().get(0).getPhone());
        services.setText(providerModel.getMessage().toString());



        Glide.with(ProfileActivity.this)
                .load(Config.BASE_URL+providerModel.getData().get(0).getStorePhoto())
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        profileImage.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


        location.setText(providerModel.getData().get(0).getLatitude()+ " " +providerModel.getData().get(0).getLongitude());

        

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Provider Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}