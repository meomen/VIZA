package com.vuducminh.viza.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.vuducminh.viza.R;
import com.vuducminh.viza.utils.Constant;


public class TransDetailActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void onClick(View v) {

    }

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
//    private ImageView backBtn;
//    private TextView titleText;
//    private WebView webView;
//    private TextView textTime;
//    private String content = "";
//    private String time = "";
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_trans_detail;
//    }
//
//    @Override
//    protected void initVariables(Bundle savedInstanceState) {
//        backBtn = (ImageView) findViewById(R.id.back_btn);
//        titleText = (TextView) findViewById(R.id.title_text);
//        webView = (WebView) findViewById(R.id.web_view);
//        textTime = (TextView) findViewById(R.id.text_time);
//    }
//
//    @Override
//    protected void initData(Bundle savedInstanceState) {
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            content = extras.getString("content");
//            time = extras.getString("time");
//        }
//
//        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
//        textTime.setText(time);
//
//        Constant.increaseHitArea(backBtn);
//        backBtn.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        finish();
//    }
}
