package com.techsamuel.roadsideprovider.activity.register;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.MainActivity;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.CommonRequests;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStepFiveActivity extends AppCompatActivity {
    TextView storeName;
    TextView storeDetails;
    ImageView storeLogo;
    Button saveAndGo;
    int PICK_IMAGE_STORE_LOGO=6;
    MultipartBody.Part storeLogoFile;
    String provider_id;
    BeautifulProgressDialog beautifulProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.hideSystemUI(this);
        AppSharedPreferences.init(this);
        provider_id=AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,"");
        if(provider_id.equals("")){
            RegisterStepFiveActivity.this.finish();
        }
        setContentView(R.layout.activity_register_step_five);
        init();
        initBeautifulProgressDialog();

    }

    private void initBeautifulProgressDialog(){
        beautifulProgressDialog = new BeautifulProgressDialog(this, BeautifulProgressDialog.withLottie, null);
        beautifulProgressDialog.setLottieLocation("service.json");
        beautifulProgressDialog.setLottieLoop(true);
    }

    private void init(){
        storeName=findViewById(R.id.store_name);
        storeDetails=findViewById(R.id.store_details);
        storeLogo=findViewById(R.id.store_logo);
        saveAndGo=findViewById(R.id.saveBtn);


        storeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery(PICK_IMAGE_STORE_LOGO);
            }
        });
        saveAndGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetails();
            }
        });
    }

    private void saveDetails() {
        beautifulProgressDialog.show();
        MultipartBody.Part store_name = MultipartBody.Part.createFormData("store_name", storeName.getText().toString());
        MultipartBody.Part store_details = MultipartBody.Part.createFormData("store_details", storeDetails.getText().toString());
        MultipartBody.Part providerId = MultipartBody.Part.createFormData("provider_id", provider_id);
        MultipartBody.Part providerPhone = MultipartBody.Part.createFormData("provider_phone", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        MultipartBody.Part device_type = MultipartBody.Part.createFormData("device_type", Config.DEVICE_TYPE);
        MultipartBody.Part lang_code = MultipartBody.Part.createFormData("lang_code", Config.LANG_CODE);
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<DataSavedModel> call=apiInterface.registerProviderStep3(device_type,lang_code,providerId,providerPhone,store_name,store_details,storeLogoFile);
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                beautifulProgressDialog.dismiss();
                if(response.body().getStatus()== Config.API_SUCCESS){
                    CommonRequests.goToStep(RegisterStepFiveActivity.this,FirebaseAuth.getInstance().getCurrentUser());
                }else{
                    Tools.showToast(RegisterStepFiveActivity.this,response.body().getMessage().toString());
                }
            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                beautifulProgressDialog.dismiss();
                Log.d("UserRegisterActivity",t.getMessage().toString());
                Tools.showToast(RegisterStepFiveActivity.this,"Connecton to server failed");
            }
        });

    }


    private void pickImageFromGallery(int requestCode){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_STORE_LOGO && resultCode==RESULT_OK && data!=null){
            Uri imageUri=data.getData();
            storeLogo.setImageURI(imageUri);
            File file= null;
            try {
                file = Tools.getFile(RegisterStepFiveActivity.this,imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            storeLogoFile = MultipartBody.Part.createFormData("store_logo", file.getName(), requestBody);
        }
    }
}