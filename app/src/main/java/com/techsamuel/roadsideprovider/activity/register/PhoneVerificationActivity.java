package com.techsamuel.roadsideprovider.activity.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.tools.Tools;

import static com.techsamuel.roadsideprovider.Config.PHONE_VERIFICATION_CODE;

public class PhoneVerificationActivity extends AppCompatActivity {

    private ImageView backBtn;
    private EditText inputCodeEditText;
    private Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        Tools.hideSystemUI(this);
        init();
    }

    private void init(){
       backBtn=(ImageView) findViewById(R.id.backBtn);
       inputCodeEditText=findViewById(R.id.inputCode);
       verifyBtn=findViewById(R.id.verifyBtn);
       backBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onBackPressed();
           }
       });
       verifyBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent();
               intent.putExtra("inputCode",inputCodeEditText.getText().toString());
               setResult(PHONE_VERIFICATION_CODE,intent);
               finish();
           }
       });

    }
}