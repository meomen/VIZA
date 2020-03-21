package com.vuducminh.viza.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vuducminh.viza.R;
import com.vuducminh.viza.utils.Constant;


public class ForgotPassActivity extends BaseActivity implements View.OnClickListener {
    private ImageView backButton;
    private Button buttonSend;
    private TextView quenMk1, quenMk2;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_forgot_pass;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        backButton = (ImageView) findViewById(R.id.back_btn);
        buttonSend = (Button) findViewById(R.id.button_send);
        quenMk1 = (TextView) findViewById(R.id.text_quen_mk_1);
        quenMk2 = (TextView) findViewById(R.id.text_quen_mk_2);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Spanned sp1 = Html.fromHtml(getResources().getText(R.string.cu_phap_2).toString());
        quenMk1.setText(sp1);

        Spanned sp2 = Html.fromHtml(getResources().getText(R.string.quen_mk_2).toString());
        quenMk2.setText(sp2);

        Constant.increaseHitArea(backButton);
        backButton.setOnClickListener(this);
        buttonSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.button_send:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "8298"));
                intent.putExtra("sms_body", "TCSR MK");
                startActivity(intent);
                break;
        }
    }
}
