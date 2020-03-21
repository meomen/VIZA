package com.vuducminh.viza.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.dialogs.LoadingDialog;
import com.vuducminh.viza.dialogs.SuccessDialog;
import com.vuducminh.viza.models.OtherRequest;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class WithdrawBankActivity extends BaseActivity implements View.OnClickListener {
    private MyApplication app;
    private Gson mGson;
    private Retrofit mRetrofit;
    private IRetrofitAPI mRetrofitAPI;
    private SharedPreferences sharedPreferences;
    private User user;

    private ImageView backButton;
    private EditText editMoneyAmount;
    private EditText editBank;
    private EditText editChiNhanh;
    private EditText editSoTk;
    private ImageView btnShowPaySave;
    private EditText editFullName;
    private EditText editPass;
    private Button continueButton;
    private LoadingDialog loadingDialog;

    private Call<OtherRequest> postAccountBankAPI;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_withdraw_bank;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        app = (MyApplication) getApplication();
        mGson = app.getGson();
        mRetrofit = app.getRetrofit();
        mRetrofitAPI = mRetrofit.create(IRetrofitAPI.class);
        sharedPreferences = app.getSharedPreferences();
        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);

        loadingDialog = new LoadingDialog(this);

        backButton = (ImageView) findViewById(R.id.back_btn);
        editMoneyAmount = (EditText) findViewById(R.id.edit_money_amount);
        editBank = (EditText) findViewById(R.id.edit_bank);
        editChiNhanh = (EditText) findViewById(R.id.edit_chi_nhanh);
        editSoTk = (EditText) findViewById(R.id.edit_so_tk);
        btnShowPaySave = (ImageView) findViewById(R.id.btn_show_pay_save);
        editFullName = (EditText) findViewById(R.id.edit_fullname);
        editPass = (EditText) findViewById(R.id.edit_pass);
        continueButton = (Button) findViewById(R.id.button_continue);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Constant.increaseHitArea(backButton);
        Constant.increaseHitArea(btnShowPaySave);
        backButton.setOnClickListener(this);
        editBank.setOnClickListener(this);
        btnShowPaySave.setOnClickListener(this);
        continueButton.setOnClickListener(this);
    }

    private void postAccountBank() {
        String token = "";
        if (user != null) {
            token = user.getToken();
        }

        HashMap<String, Object> body = new HashMap<>();
        body.put("sotienrut", editMoneyAmount.getText().toString());
        body.put("bank", editBank.getText().toString());
        body.put("chinhanh", editChiNhanh.getText().toString());
        body.put("soTK", editSoTk.getText().toString());
        body.put("fullname", editFullName.getText().toString());
        body.put("mk2", editPass.getText().toString());

        loadingDialog.show();
        postAccountBankAPI = mRetrofitAPI.postAccountBank(token, body);
        postAccountBankAPI.enqueue(new Callback<OtherRequest>() {
            @Override
            public void onResponse(Call<OtherRequest> call, Response<OtherRequest> response) {
                int errorCode = response.body().getErrorCode();
                String msg = response.body().getMsg();
                loadingDialog.dismiss();

                if (errorCode == 1) {
                    SuccessDialog dialog = new SuccessDialog(WithdrawBankActivity.this, msg);
                    dialog.show();
                } else {
                    Toast.makeText(WithdrawBankActivity.this, msg, Toast.LENGTH_SHORT).show();

                    if (errorCode == -2) {
                        sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
                        sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
                        Constant.restartApp(WithdrawBankActivity.this);
                    }
                }

                editMoneyAmount.setText("");
                editChiNhanh.setText("");
                editSoTk.setText("");
                editFullName.setText("");
                editPass.setText("");

                Intent i = new Intent(Constant.UPDATE_INFO);
                sendBroadcast(i);
            }

            @Override
            public void onFailure(Call<OtherRequest> call, Throwable t) {
                Toast.makeText(WithdrawBankActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.edit_bank:
                Intent i1 = new Intent(this, PaySaveActivity.class);
                i1.putExtra("type", 0);
                i1.putExtra("channel", "wdr");
                startActivityForResult(i1, 1);
                break;
            case R.id.btn_show_pay_save:
                if (sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
                    Intent i = new Intent(this, PaySaveActivity.class);
                    i.putExtra("type", 1);
                    startActivityForResult(i, 0);
                } else {
                    Toast.makeText(WithdrawBankActivity.this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_continue:
                if (sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
                    postAccountBank();
                } else {
                    Toast.makeText(WithdrawBankActivity.this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String paySave = data.getStringExtra("pay_save");
                String fullName = data.getStringExtra("full_name");
                String bankName = data.getStringExtra("bank_name");
                String branch = data.getStringExtra("branch");
                editSoTk.setText(paySave);
                editFullName.setText(fullName);
                editBank.setText(bankName);
                editChiNhanh.setText(branch);
            }
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String bank = data.getStringExtra("bank");
                editBank.setText(bank);
            }
        }
    }
}
