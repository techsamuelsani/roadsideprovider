package com.techsamuel.roadsideprovider.activity.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.MainActivity;
import com.techsamuel.roadsideprovider.adapter.ToolboxUploadinPhotosGridAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.tools.SpacingItemDecoration;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStepFourActivity extends AppCompatActivity {
    ArrayList<String>services;
    ImageView uploadToolbox;
    int PICK_IMAGE_MULTIPLE = 1;
    int PICK_IMAGE_NIDFRONT=2;
    int PICK_IMAGE_NIDBACK=3;
    int PICK_IMAGE_DRIVINGLICENSE=4;
    int PICK_IMAGE_OWNERPHOTO=5;
    String imageEncoded;
    List<String> imagesEncodedList;
    RecyclerView recyclerView;
    ToolboxUploadinPhotosGridAdapter toolboxUploadinPhotosGridAdapter;
    TextView toolboxText;
    ImageView nidFront;
    ImageView nidBack;
    ImageView drivingLicense;
    ImageView ownerPhoto;
    Button nextButton;


    MultipartBody.Part nidFrontFile;
    MultipartBody.Part nidBackFile;
    MultipartBody.Part drivingLicenseFile;
    MultipartBody.Part ownerPhotoFile;
    List <MultipartBody.Part> toolboxFile=new ArrayList<MultipartBody.Part>();

    String providerId;
    String providerPhone;
    ApiInterface apiInterface;
    BeautifulProgressDialog beautifulProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_four);
        apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        providerPhone=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        Tools.hideSystemUI(this);
        getIdByPhone(providerPhone);
        initBeautifulProgressDialog();


    }

    private void initBeautifulProgressDialog(){
        beautifulProgressDialog = new BeautifulProgressDialog(this, BeautifulProgressDialog.withLottie, null);
        beautifulProgressDialog.setLottieLocation("service.json");
        beautifulProgressDialog.setLottieLoop(true);
    }


    private void getIdByPhone(String providerPhone){
        Call<DataSavedModel> call=apiInterface.getIdByPhone(providerPhone);
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                //Tools.showToast(RegisterStepFourActivity.this,response.body().getMessage().toString());
                providerId=response.body().getId().toString();
                getData();
            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                Tools.showToast(RegisterStepFourActivity.this,"Retrofit Connecton Failed");
            }
        });
    }

    private void getServicesById(){
        Call<DataSavedModel> call=apiInterface.getServicesById(providerId);
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                //Tools.showToast(RegisterStepFourActivity.this,response.body().getMessage().toString());
                //services=response.body().getMessage().get();
                for(int i=0;i<response.body().getMessage().size();i++){
                    services.add(response.body().getMessage().get(i));
                   // Tools.showToast(getApplicationContext(),response.body().getMessage().toString());

                }
                init();


            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                Tools.showToast(RegisterStepFourActivity.this,"Retrofit Connecton Failed");
            }
        });
    }

        private void getData(){
            services = new ArrayList<>();
            Intent intent = getIntent();

            if(intent.hasExtra("details")){
                services = intent.getStringArrayListExtra("services");
                for (String service : services) {
                    Log.d("Services", service);
                }
                init();
            }else{
                getServicesById();

            }
        }

    private void init() {
            uploadToolbox = findViewById(R.id.uploadToolbox);
            nidFront = findViewById(R.id.nid_Front);
            nidBack = findViewById(R.id.nidBack);
            drivingLicense = findViewById(R.id.driverLicense);
            ownerPhoto = findViewById(R.id.ownerPhoto);
            recyclerView = findViewById(R.id.recyclerView);
            toolboxText = findViewById(R.id.toolboxText);
            nextButton = findViewById(R.id.nextBtn);
            toolboxText.setText("Upload" + " " + services.size() + " " + "Toolbox Photos");
            uploadToolbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_MULTIPLE);
                }
            });

            nidFront.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickImageFromGallery(PICK_IMAGE_NIDFRONT);
                }
            });

            nidBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickImageFromGallery(PICK_IMAGE_NIDBACK);
                }
            });

            drivingLicense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickImageFromGallery(PICK_IMAGE_DRIVINGLICENSE);
                }
            });

            ownerPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICK_IMAGE_OWNERPHOTO);
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadAllDocuments();
                }
            });

    }

    private void uploadAllDocuments(){
        beautifulProgressDialog.show();
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<DataSavedModel> call=apiInterface.registerProviderStep2(providerId,providerPhone,
                nidFrontFile,nidBackFile,toolboxFile,drivingLicenseFile,ownerPhotoFile
        );
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                beautifulProgressDialog.dismiss();
                if(response.body().getStatus()== Config.API_SUCCESS){
                    Intent intent=new Intent(RegisterStepFourActivity.this, RegisterStepFiveActivity.class);
                    startActivity(intent);
                }
                Tools.showToast(RegisterStepFourActivity.this,response.body().getMessage().toString());

            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                Tools.showToast(RegisterStepFourActivity.this,"Retrofit Connecton Failed");
                beautifulProgressDialog.dismiss();
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
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                    filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> imageUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            imageUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                        }
                        if(imageUri.size()>services.size()){
                            Tools.showToast(RegisterStepFourActivity.this,"You have selected more photos than your toolbox");
                        }else if(imageUri.size()<services.size()){
                            Tools.showToast(RegisterStepFourActivity.this,"You have selected less photos than your toolbox");
                        }else{
                            recyclerView.setVisibility(View.VISIBLE);
                            uploadToolbox.setVisibility(View.GONE);
                            Tools.showToast(RegisterStepFourActivity.this,"You have selected Exact photos match with your toolbox");
                            toolboxUploadinPhotosGridAdapter=new ToolboxUploadinPhotosGridAdapter(RegisterStepFourActivity.this,imageUri);
                            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                            recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(RegisterStepFourActivity.this, 2), true));
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(toolboxUploadinPhotosGridAdapter);
                            //toolboxFile=new MultipartBody.Part[services.size()];
                            for(int i=0;i<services.size();i++){
                                File file=Tools.getFile(RegisterStepFourActivity.this,imageUri.get(i));
                                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                                MultipartBody.Part part=MultipartBody.Part.createFormData("toolbox_photos[]", file.getName(), requestBody);
                                toolboxFile.add(part);

                            }

                        }

                    }
                }
            }

           else if(requestCode==PICK_IMAGE_NIDFRONT && resultCode==RESULT_OK && data!=null){
                Uri imageUri=data.getData();
                nidFront.setImageURI(imageUri);
                File file=Tools.getFile(RegisterStepFourActivity.this,imageUri);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                nidFrontFile = MultipartBody.Part.createFormData("nid_front", file.getName(), requestBody);
            }

           else if(requestCode==PICK_IMAGE_NIDBACK && resultCode==RESULT_OK && data!=null){
                Uri imageUri=data.getData();
                nidBack.setImageURI(imageUri);
                File file=Tools.getFile(RegisterStepFourActivity.this,imageUri);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                nidBackFile = MultipartBody.Part.createFormData("nid_back", file.getName(), requestBody);
            }

            else if(requestCode==PICK_IMAGE_DRIVINGLICENSE && resultCode==RESULT_OK && data!=null){
                Uri imageUri=data.getData();

                File file=Tools.getFile(RegisterStepFourActivity.this,imageUri);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                drivingLicenseFile = MultipartBody.Part.createFormData("driving_license", file.getName(), requestBody);
                drivingLicense.setImageURI(imageUri);

            }


            else if(requestCode==PICK_IMAGE_OWNERPHOTO && resultCode==RESULT_OK && data!=null){
                  Bitmap image = (Bitmap) data.getExtras().get("data");
                  ownerPhoto.setImageBitmap(image);
//                  Uri uri=Tools.getImageUriFromBitmap(RegisterStepFourActivity.this,image);
//                  File file=Tools.getFile(RegisterStepFourActivity.this,uri);
                File file=Tools.getFileFromBitmap(RegisterStepFourActivity.this,image);
                  RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                  ownerPhotoFile = MultipartBody.Part.createFormData("owner_photo", file.getName(), requestBody);
            }else{
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}