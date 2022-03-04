package com.techsamuel.roadsideprovider.activity.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.AdminUser;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.YearPickerDialog;
import com.techsamuel.roadsideprovider.tools.Tools;
import com.techsamuel.roadsideprovider.tools.YearPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStepTwo extends AppCompatActivity {

    private EditText vmake;
    private EditText vmodel;
    private EditText plateNumber;
    private EditText vyear;
    private EditText exp_date;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private EditText nationalId;
    private Button nextBtn;
    String phoneNumber;
    String firebaseId;
    Calendar calendar;
    BeautifulProgressDialog beautifulProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_two);
        Tools.hideSystemUI(this);
        init();
        initBeautifulProgressDialog();

    }
    private void initBeautifulProgressDialog(){
        beautifulProgressDialog = new BeautifulProgressDialog(this, BeautifulProgressDialog.withLottie, null);
        beautifulProgressDialog.setLottieLocation("service.json");
        beautifulProgressDialog.setLottieLoop(true);
    }
    private void init(){
        Intent intent=getIntent();
        phoneNumber=intent.getStringExtra("phoneNumber");
        firebaseId=intent.getStringExtra("firebaseId");
        vmake=findViewById(R.id.vmake);
        vmodel=findViewById(R.id.vmodel);
        plateNumber=findViewById(R.id.plateNumber);
        vyear=findViewById(R.id.year);
        exp_date=findViewById(R.id.exp_date);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        confirm_password=findViewById(R.id.confirm_password);
        nationalId=findViewById(R.id.national_id);
        nextBtn=findViewById(R.id.nextBtn);

        vyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YearPickerDialog newFragment = new YearPickerDialog(vyear);
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });
        exp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDateOrYear(false,exp_date);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNext(vmake.getText().toString(),vmodel.getText().toString(),plateNumber.getText().toString(),vyear.getText().toString(),
                        exp_date.getText().toString(),name.getText().toString(),email.getText().toString(),password.getText().toString(),
                        confirm_password.getText().toString(),nationalId.getText().toString(), firebaseId,phoneNumber
                );
            }
        });



    }

    private void selectDateOrYear(boolean isOnlyYear,EditText editText){
        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String dateFormat="";
                if(isOnlyYear){
                    dateFormat="YYYY";
                }else{
                    dateFormat="MM/dd/yyyy";
                }
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat(dateFormat, Locale.US);
                editText.setText(simpleDateFormat.format(calendar.getTime()));
            }

        };

        new DatePickerDialog(RegisterStepTwo.this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();



    }


    private void goToNext(String vmake,String vmodel,String plate_no, String vyear,String exp_date,String name,String email,String password,String confirmPassword,String national_id,
                          String firebase_id,String phone){
        beautifulProgressDialog.show();
        ApiInterface apiInterface=ApiServiceGenerator.createService(ApiInterface.class);
        Call<DataSavedModel> call=apiInterface.registerProviderStep1(
              vmake,vmodel,plate_no,vyear,exp_date,name,
              email,password,confirmPassword,national_id,firebase_id,phone
        );
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                beautifulProgressDialog.dismiss();
                if(response.body().getStatus()==Config.API_FAILED){
                    String allMessage="";
                    for(String message:response.body().getMessage()){
                        allMessage+="\n"+message;
                    }
                    Tools.showToast(RegisterStepTwo.this,allMessage);

                }else{
                    AppSharedPreferences.init(RegisterStepTwo.this);
                    AppSharedPreferences.write(Config.SHARED_PREF_PROVIDER_ID,response.body().getId().toString());

                    Intent intent=new Intent(RegisterStepTwo.this, RegisterStepThreeActivity.class);
                    intent.putExtra("phoneNumber", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                    intent.putExtra("firebaseId",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if(intent!=null){
                        startActivity(intent);
                    }


                }
            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                beautifulProgressDialog.dismiss();
                Log.d("Api Error",t.getMessage().toString());

            }
        });

    }





    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}