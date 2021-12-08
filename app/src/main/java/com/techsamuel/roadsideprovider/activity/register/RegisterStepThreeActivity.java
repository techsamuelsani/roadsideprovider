package com.techsamuel.roadsideprovider.activity.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.adapter.ServiceAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.ServiceModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.CommonRequests;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStepThreeActivity extends AppCompatActivity {

    ArrayList<String> servicesName;
    ArrayList<String> servicesId;
    Button nextBtn;
    String providerId;
    String providerPhone;
    ApiInterface apiInterface;
    RecyclerView recyclerService;
    ServiceAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_three);
        apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        providerPhone= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        Tools.hideSystemUI(this);
        init();
        getProviderId();
        fetchAllServices();

    }

    private void getProviderId(){
        providerId=AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,"");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Config.ServiceModelData.clear();
    }

    private void init(){
        recyclerService=findViewById(R.id.recycler_services);
        servicesName=new ArrayList<String>();
        servicesId=new ArrayList<>();
        nextBtn=findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProviderServices(Config.ServiceModelData);

            }
        });
    }

    private void updateProviderServices(List<ServiceModel.Datum> services){
        for(int i=0;i<services.size();i++){
            this.servicesName.add(services.get(i).getName());
            this.servicesId.add(services.get(i).getId());
        }

        Call<DataSavedModel> call=apiInterface.updateProviderServices(providerId,providerPhone,servicesName,servicesId);
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                if(response.body().getStatus()== Config.API_SUCCESS){
                    Tools.showToast(getApplicationContext(),response.body().getMessage().toString());
                    Intent intent=new Intent(RegisterStepThreeActivity.this,RegisterStepFourActivity.class);
                    intent.putExtra("details","1");
                    intent.putExtra("services",servicesName);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {

            }
        });


    }

        public void fetchAllServices(){
            Call<ServiceModel> call=apiInterface.getAllServices(Config.DEVICE_TYPE,Config.LANG_CODE);
            call.enqueue(new Callback<ServiceModel>() {
                @Override
                public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                    if(response.body().getStatus()== Config.API_SUCCESS){
                        serviceAdapter=new ServiceAdapter(RegisterStepThreeActivity.this,response.body());
                        recyclerService.setLayoutManager(new LinearLayoutManager(RegisterStepThreeActivity.this));
                        recyclerService.setAdapter(serviceAdapter);
                    }
                }

                @Override
                public void onFailure(Call<ServiceModel> call, Throwable t) {
                    Log.d("RegisterStep3",t.getMessage().toString());

                }
            });

        }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}