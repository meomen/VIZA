package com.vuducminh.viza.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.utils.Constant;




public class DepositEbankingActivity extends BaseActivity implements View.OnClickListener {
    private Gson mGson;
    private SharedPreferences sharedPreferences;
    private User user;

    private ImageView backButton;
    private WebView webView;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_deposit_ebanking;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mGson = MyApplication.getGson();
        sharedPreferences = MyApplication.getSharedPreferences();
        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);

        backButton = (ImageView) findViewById(R.id.back_btn);
        webView = (WebView) findViewById(R.id.web_view);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        if (user != null) {
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            webView.setWebChromeClient(new WebChromeClient());
            webView.loadUrl(Constant.EBANKING_URL + user.getToken());
        } else {
            Toast.makeText(this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
        }

        Constant.increaseHitArea(backButton);
        backButton.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        Constant.restartApp(this);
    }
}
