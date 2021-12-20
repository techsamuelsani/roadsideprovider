package com.techsamuel.roadsideprovider.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.adapter.TransactionAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.SettingsModel;
import com.techsamuel.roadsideprovider.model.TransactionModel;
import com.techsamuel.roadsideprovider.model.UserModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnRecharge;
    ProviderModel providerModel;
    String userId;
    ImageView userImage;
    TextView userBalance;
    TextView userName;
    TextView creditBalance;
    TextView debitBalance;
    TransactionAdapter transactionAdapter;
    RecyclerView recyclerPayment;
    SettingsModel settingsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppSharedPreferences.init(this);
        providerModel=AppSharedPreferences.readProviderModel(Config.SHARED_PREF_PROVIDER_MODEL,"");
        settingsModel=AppSharedPreferences.readSettingsModel(Config.SHARED_PREF_SETTINGS_MODEL,"");
        if(providerModel.equals("")||settingsModel.equals("")){
            WalletActivity.this.finish();
        }
        setContentView(R.layout.activity_wallet);
        initToolbar();
        init();
    }
    private void initToolbar() {
        userId=AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,"");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.wallet);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void init(){
        btnRecharge=findViewById(R.id.btnRecharge);
        userName=findViewById(R.id.userName);
        userImage=findViewById(R.id.userImage);
        userBalance=findViewById(R.id.userBalance);
        creditBalance=findViewById(R.id.creditBalance);
        debitBalance=findViewById(R.id.debitBalnce);
        userName.setText(providerModel.getData().get(0).getName());
        userBalance.setText(providerModel.getData().get(0).getWallet()+" "+settingsModel.getData().getAppCurrency());
        Glide.with(WalletActivity.this).load(Config.BASE_URL+providerModel.getData().get(0).getStorePhoto()).into(userImage);
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showRechargeDialog();
            }
        });
    }

//    private void showRechargeDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("(Minimum "+Config.MINIMUM_RECHARGE_AMOUNT+" Maximum "+Config.MAXIMUM_RECHARGE_AMOUNT
//                +")");
//        builder.setTitle("Enter the amount you want to recharge.");
//
//        final EditText input = new EditText(this);
//        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        builder.setView(input);
//
//// Set up the buttons
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String inputAmount=input.getText().toString();
//                if(inputAmount.isEmpty()){
//                    Tools.showToast(WalletActivity.this,"Please enter amount");
//
//                }
//                else if(Double.valueOf(inputAmount)< Config.MINIMUM_RECHARGE_AMOUNT){
//                    Tools.showToast(WalletActivity.this,"Please enter amount max than "+Config.MINIMUM_RECHARGE_AMOUNT);
//
//                }
//                else if(Double.valueOf(inputAmount)> Config.MAXIMUM_RECHARGE_AMOUNT){
//                    Tools.showToast(WalletActivity.this,"Please enter amount min than "+Config.MAXIMUM_RECHARGE_AMOUNT);
//
//                }else{
//                    Intent intent=new Intent(WalletActivity.this,PaymentWebview.class);
//                    intent.putExtra("url", Config.IZIPAY_PAYMENT_URL_SELF+Config.USER_TYPE+"/"+userId+"/"+Config.DEVICE_TYPE
//                    +"/"+Config.LANG_CODE+"/"+inputAmount);
//                    startActivity(intent);
//
//                }
//
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.show();
//    }

    @Override
    protected void onResume() {
        getAllTransaction();
        super.onResume();
    }

    private void getAllTransaction() {
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<TransactionModel> call=apiInterface.getAllTransactionById(Config.USER_TYPE,userId);
        call.enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {

                if(response.body().getStatus()==Config.API_SUCCESS){
                    userBalance.setText(response.body().getTotalBalance()+" "+settingsModel.getData().getAppCurrency());
                    creditBalance.setText(response.body().getTotalCredit()+" "+settingsModel.getData().getAppCurrency());
                    debitBalance.setText(response.body().getTotalDebit()+" "+settingsModel.getData().getAppCurrency());
                    recyclerPayment=findViewById(R.id.recycler_payment);
                    recyclerPayment.setLayoutManager(new LinearLayoutManager(WalletActivity.this));
                    transactionAdapter=new TransactionAdapter(WalletActivity.this,response.body());
                    recyclerPayment.setAdapter(transactionAdapter);
                    Config.balance=response.body().getTotalBalance();


                }else{
                    userBalance.setText(response.body().getTotalBalance()+" "+settingsModel.getData().getAppCurrency());
                    creditBalance.setText(response.body().getTotalCredit()+" "+settingsModel.getData().getAppCurrency());
                    debitBalance.setText(response.body().getTotalDebit()+" "+settingsModel.getData().getAppCurrency());
                }

            }

            @Override
            public void onFailure(Call<TransactionModel> call, Throwable t) {
                Tools.showToast(WalletActivity.this,"Connecton to server failed");
                Log.d("WalletActivity",t.getMessage().toString());
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        WalletActivity.this.finish();
    }
}