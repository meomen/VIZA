package com.vuducminh.viza.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.utils.Constant;



public class DepositActivity extends BaseActivity implements View.OnClickListener {
    private MyApplication app;
    private Gson mGson;
    private SharedPreferences sharedPreferences;
    private User user;
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
        app = (MyApplication) getApplication();
        mGson = app.getGson();
        sharedPreferences = app.getSharedPreferences();
        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);
        Constant.increaseHitArea(backButton);

        backButton.setOnClickListener(this);
        buttonThe.setOnClickListener(this);
        buttonEbanking.setOnClickListener(this);
    }
    public void checkConnectBank() {

        FirebaseDatabase.getInstance().getReference(Constant.CUSTOMER)
                .child(user.getMobile()).child(Constant.WALLET)
                .child(Constant.CONNECT_BANK)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            Intent intent = new Intent(DepositActivity.this, ConnectBankActivity.class);
                            intent.putExtra("connected",false);
                            startActivity(intent);
                        }
                        else {
                            startActivity(DepositEbankingActivity.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
                if (sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
                  checkConnectBank();

                } else {
                    Toast.makeText(DepositActivity.this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
