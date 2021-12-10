package com.techsamuel.roadsideprovider.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;

public class WebViewActivity extends AppCompatActivity {

    Toolbar toolbar;
    String providerId;
    String url;
    String title;
    String content;
    WebView webView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppSharedPreferences.init(this);
        providerId=AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,"");
        setContentView(R.layout.activity_webview);
        init();
        initToolbar();

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //new PaymentTask().execute("");
    }
    private void init(){
        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        title=intent.getStringExtra("title");

        webView=findViewById(R.id.webview);
        progressBar=findViewById(R.id.progressBar);
        webView.setWebViewClient(new PaymentWebviewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        if(intent.hasExtra("content")){
            content=intent.getStringExtra("content");
            webView.loadData(content,"text/html", "UTF-8");
        }else{
            webView.loadUrl(url);
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        WebViewActivity.this.finish();
    }

    private class PaymentWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }
    }
}