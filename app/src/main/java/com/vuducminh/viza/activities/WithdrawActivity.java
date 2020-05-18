package com.vuducminh.viza.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vuducminh.viza.R;
import com.vuducminh.viza.utils.Constant;



public class WithdrawActivity extends BaseActivity implements View.OnClickListener {
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
//    private ImageView backButton;
//    private RelativeLayout buttonWithdrawATM;
//    private RelativeLayout buttonWithdrawCMT;
//    private RelativeLayout buttonWithdrawBank;
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_withdraw;
//    }
//
//    @Override
//    protected void initVariables(Bundle savedInstanceState) {
//        backButton = (ImageView) findViewById(R.id.back_btn);
//        buttonWithdrawATM = (RelativeLayout) findViewById(R.id.button_withdraw_atm);
//        buttonWithdrawCMT = (RelativeLayout) findViewById(R.id.button_withdraw_cmt);
//        buttonWithdrawBank = (RelativeLayout) findViewById(R.id.button_withdraw_bank);
//    }
//
//    @Override
//    protected void initData(Bundle savedInstanceState) {
//        Constant.increaseHitArea(backButton);
//
//        backButton.setOnClickListener(this);
//        buttonWithdrawATM.setOnClickListener(this);
//        buttonWithdrawCMT.setOnClickListener(this);
//        buttonWithdrawBank.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.back_btn:
//                finish();
//                break;
//            case R.id.button_withdraw_atm:
//                startActivity(WithdrawATMActivity.class);
//                break;
//            case R.id.button_withdraw_cmt:
//                startActivity(WithdrawCMTActivity.class);
//                break;
//            case R.id.button_withdraw_bank:
//                startActivity(WithdrawBankActivity.class);
//                break;
//        }
//    }
}
