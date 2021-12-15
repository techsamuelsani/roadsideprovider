package com.techsamuel.roadsideprovider.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.adapter.MessageAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.listener.MessageItemClickListener;
import com.techsamuel.roadsideprovider.model.MessageModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    String userId;
    Toolbar toolbar;
    RecyclerView recyclerMessage;
    MessageAdapter messageAdapter;
    BeautifulProgressDialog beautifulProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initToolbar();
        init();
    }

    private void initToolbar() {
        AppSharedPreferences.init(this);
        userId=AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,"");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("All Messages");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //new PaymentTask().execute("");
        beautifulProgressDialog = new BeautifulProgressDialog(this, BeautifulProgressDialog.withLottie, null);
        beautifulProgressDialog.setLottieLocation("service.json");
        beautifulProgressDialog.setLottieLoop(true);
    }
    private void init(){
        recyclerMessage=findViewById(R.id.recyler_message);
        getMessageByTypeAndId();

    }

    public void getMessageByTypeAndId(){
        Log.d("MainActivity","Called");
        beautifulProgressDialog.show();
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<MessageModel> call=apiInterface.getMessageByTypeAndId(Config.DEVICE_TYPE,Config.LANG_CODE,Config.USER_TYPE,userId);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                beautifulProgressDialog.dismiss();
                Log.d("MainActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    messageAdapter=new MessageAdapter(MessageActivity.this, response.body(),  new MessageItemClickListener() {
                        @Override
                        public void onItemClick(MessageModel.Datum item) {
                            Tools.showToast(MessageActivity.this,item.getMessageDetails());
                        }
                    });
                    recyclerMessage.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                    recyclerMessage.setAdapter(messageAdapter);
                    Log.d("MainActivity",response.body().getMessage().toString());
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());

            }
        });

    }

    @Override
    public void onBackPressed() {
        MessageActivity.this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}