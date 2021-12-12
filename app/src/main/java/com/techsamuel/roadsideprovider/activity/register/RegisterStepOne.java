package com.techsamuel.roadsideprovider.activity.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.tools.CommonRequests;
import com.techsamuel.roadsideprovider.tools.Tools;


import java.util.concurrent.TimeUnit;

import static com.techsamuel.roadsideprovider.Config.PHONE_VERIFICATION_CODE;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStepOne extends AppCompatActivity {

    private Button continueBtn;
    private CountryCodePicker countryCodePicker;
    private EditText phoneNumber;
    private String fullPhoneNumber;
    private PhoneAuthOptions phoneAuthOptions;
    FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private String verificationId;
    private String verificationCode;
    private String inputVerificationCode;
    BeautifulProgressDialog beautifulProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_one);
        Tools.hideSystemUI(this);
        init();
        checkAuth();
        initBeautifulProgressDialog();
    }

    private void initBeautifulProgressDialog(){
        beautifulProgressDialog = new BeautifulProgressDialog(this, BeautifulProgressDialog.withLottie, null);
        beautifulProgressDialog.setLottieLocation("service.json");
        beautifulProgressDialog.setLottieLoop(true);
    }

    private void checkAuth() {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            CommonRequests.goToStep(RegisterStepOne.this,firebaseUser);
        }
  }

    private void init(){
        continueBtn=(Button)findViewById(R.id.continueBtn);
        countryCodePicker=(CountryCodePicker)findViewById(R.id.countryCodePicker);
        phoneNumber=(EditText)findViewById(R.id.phoneNumber);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        firebaseAuth=FirebaseAuth.getInstance();


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countryCode=countryCodePicker.getSelectedCountryCodeWithPlus();
                String number=phoneNumber.getText().toString();
                fullPhoneNumber=countryCode+number;
                if(Tools.validatePhoneNumber(fullPhoneNumber)){
                    sendVerificationCode(fullPhoneNumber);
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.please_enter_correct_phone_number),Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    private void sendVerificationCode(String fullPhoneNumber) {
       progressBar.setVisibility(View.VISIBLE);
        phoneAuthOptions =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(fullPhoneNumber)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s,PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Tools.showToast(RegisterStepOne.this,"Code Sent");
            progressBar.setVisibility(View.GONE);
            verificationId=s;
            Intent intent=new Intent(RegisterStepOne.this,PhoneVerificationActivity.class);
            startActivityForResult(intent,PHONE_VERIFICATION_CODE);



        }
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            verificationCode=phoneAuthCredential.getSmsCode();


        }

        @Override
        public void onVerificationFailed( FirebaseException e) {
            Tools.showToast(RegisterStepOne.this,e.getMessage());

        }


    };

    private void verifyCode(String verificationCode) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            CommonRequests.goToStep(RegisterStepOne.this,firebaseAuth.getCurrentUser());
                            Tools.showToast(RegisterStepOne.this,"Phone Auth success");
                            progressBar.setVisibility(View.VISIBLE);
                        } else {
                            Tools.showToast(RegisterStepOne.this,getString(R.string.incorrect_verificatio_code));

                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PHONE_VERIFICATION_CODE){
            String inputCode=data.getStringExtra("inputCode");
            verifyCode(inputCode);

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}