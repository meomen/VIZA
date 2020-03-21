package com.vuducminh.viza.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
;
import com.vuducminh.viza.R;
import com.vuducminh.viza.utils.Constant;



public class DepositActivity extends BaseActivity implements View.OnClickListener {
    private ImageView backButton;
    private RelativeLayout buttonThe;
    private RelativeLayout buttonEbanking;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_deposit;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        backButton = (ImageView) findViewById(R.id.back_btn);
        buttonThe = (RelativeLayout) findViewById(R.id.button_the);
        buttonEbanking = (RelativeLayout) findViewById(R.id.button_ebanking);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Constant.increaseHitArea(backButton);

        backButton.setOnClickListener(this);
        buttonThe.setOnClickListener(this);
        buttonEbanking.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.button_the:
                startActivity(DepositCardActivity.class);
                break;
            case R.id.button_ebanking:
                startActivity(DepositEbankingActivity.class);
                break;
        }
    }
}
