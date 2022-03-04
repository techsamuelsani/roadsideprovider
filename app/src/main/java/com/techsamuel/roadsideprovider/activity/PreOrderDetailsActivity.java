package com.techsamuel.roadsideprovider.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.OrderModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreOrderDetailsActivity extends AppCompatActivity {

    BeautifulProgressDialog beautifulProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_order_details);
        beautifulProgressDialog = new BeautifulProgressDialog(this, BeautifulProgressDialog.withLottie, null);
        beautifulProgressDialog.setLottieLocation("service.json");
        beautifulProgressDialog.setLottieLoop(true);
        init();
    }

    private void init(){
        Intent intent=getIntent();
        String orderId=intent.getStringExtra("order_id");
        getOrderDetailsById(orderId);
    }


    private void getOrderDetailsById(String id) {
        beautifulProgressDialog.show();
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<OrderModel> call=apiInterface.getOrderDetailsById(Config.DEVICE_TYPE,Config.LANG_CODE,id);
        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                Log.d("CurrentOrdersActivity",response.body().getMessage().toString());
                beautifulProgressDialog.dismiss();
                if(response.body().getStatus()== Config.API_SUCCESS){
                    AppSharedPreferences.writeOrderModel(Config.SHARED_PREF_ORDER_MODEL,response.body());
                    Intent intent=new Intent(PreOrderDetailsActivity.this,OrderDetailsActivity.class);
                   // intent.putExtra(Config.APP_PAGE,Config.PAGE_CURRENT_ORDERS);
                    startActivity(intent);
                    PreOrderDetailsActivity.this.finish();
                }
            }
            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.d("PreOrderDetailsActivity",t.getMessage().toString());
                beautifulProgressDialog.dismiss();

            }
        });

    }
}