package com.vuducminh.viza.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.dialogs.LoadingDialog;
import com.vuducminh.viza.dialogs.SuccessDialog;
import com.vuducminh.viza.models.CardObject;
import com.vuducminh.viza.models.CardRequest;
import com.vuducminh.viza.models.OtherRequest;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class TraGopActivity extends BaseActivity implements View.OnClickListener {
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
//    private MyApplication app;
//    private Gson mGson;
//    private Retrofit mRetrofit;
//    private IRetrofitAPI mRetrofitAPI;
//    private SharedPreferences sharedPreferences;
//    private User user;
//
//    private ImageView backButton;
//    private EditText editMoneyAmount;
//    private Spinner spinnerHd;
//    private ArrayAdapter<String> adapter;
//    private ArrayList<String> listHdName;
//    private ArrayList<CardObject> listHd;
//    private EditText editMaHd;
//    private EditText editFullname;
//    private EditText editPhone;
//    private EditText editAddress;
//    private EditText editPass;
//    private EditText editDes;
//    private ImageView btnShowPaySave;
//    private Button continueButton;
//    private LoadingDialog loadingDialog;
//
//    private Handler handler;
//    private long delay = 1000;
//    private long last_text_edit = 0;
//
//    private Call<CardRequest> getCardInfoAPI;
//    private Call<OtherRequest> postSaleHDAPI;
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_tra_gop;
//    }
//
//    @Override
//    protected void initVariables(Bundle savedInstanceState) {
//        app = (MyApplication) getApplication();
//        mGson = app.getGson();
//        mRetrofit = app.getRetrofit();
//        mRetrofitAPI = mRetrofit.create(IRetrofitAPI.class);
//        sharedPreferences = app.getSharedPreferences();
//        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);
//
//        loadingDialog = new LoadingDialog(this);
//
//        backButton = (ImageView) findViewById(R.id.back_btn);
//        editMoneyAmount = (EditText) findViewById(R.id.edit_money_amount);
//        spinnerHd = (Spinner) findViewById(R.id.spinner_hd);
//        editMaHd = (EditText) findViewById(R.id.edit_ma_hd);
//        editFullname = (EditText) findViewById(R.id.edit_fullname);
//        editPhone = (EditText) findViewById(R.id.edit_phone);
//        editAddress = (EditText) findViewById(R.id.edit_address);
//        editPass = (EditText) findViewById(R.id.edit_pass);
//        editDes = (EditText) findViewById(R.id.edit_des);
//        btnShowPaySave = (ImageView) findViewById(R.id.btn_show_pay_save);
//        continueButton = (Button) findViewById(R.id.button_continue);
//    }
//
//    @Override
//    protected void initData(Bundle savedInstanceState) {
//        handler = new Handler();
//
//        listHdName = new ArrayList<>();
//        getListHd();
//
//        adapter = new ArrayAdapter<>(this, R.layout.item_spinner, listHdName);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerHd.setAdapter(adapter);
//        spinnerHd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (editMoneyAmount.getText().toString().equals("")) {
//                    editDes.setText("");
//                } else {
//                    editDes.setText("Thanh toán " + listHdName.get(position) + " với số tiền " + editMoneyAmount.getText().toString() + " VNĐ");
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        editMoneyAmount.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (editMoneyAmount.getText().toString().equals("")) {
//                    editDes.setText("");
//                }
//                handler.removeCallbacks(input_finish_checker);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    last_text_edit = System.currentTimeMillis();
//                    handler.postDelayed(input_finish_checker, delay);
//                }
//            }
//        });
//
//        Constant.increaseHitArea(backButton);
//        Constant.increaseHitArea(btnShowPaySave);
//        backButton.setOnClickListener(this);
//        btnShowPaySave.setOnClickListener(this);
//        continueButton.setOnClickListener(this);
//    }
//
//    private Runnable input_finish_checker = new Runnable() {
//        public void run() {
//            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
//                if (editMoneyAmount.getText().toString().equals("")) {
//                    editDes.setText("");
//                } else {
//                    editDes.setText("Thanh toán " + listHdName.get(spinnerHd.getSelectedItemPosition()) + " với số tiền " + editMoneyAmount.getText().toString() + " VNĐ");
//                }
//            }
//        }
//    };
//
//    private void getListHd() {
//        HashMap<String, Object> body = new HashMap<>();
//        body.put("channel", "sale");
//
//        getCardInfoAPI = mRetrofitAPI.getCardInfo(body);
//        getCardInfoAPI.enqueue(new Callback<CardRequest>() {
//            @Override
//            public void onResponse(Call<CardRequest> call, Response<CardRequest> response) {
//                int errorCode = response.body().getErrorCode();
//                String msg = response.body().getMsg();
//
//                if (errorCode == 1) {
//                    listHd = response.body().getData();
//
//                    for (int i = 0; i < listHd.size(); i++) {
//                        listHdName.add(listHd.get(i).getBankName());
//                    }
//                    adapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(TraGopActivity.this, msg, Toast.LENGTH_SHORT).show();
//
//                    if (errorCode == -2) {
//                        sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
//                        sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
//                        Constant.restartApp(TraGopActivity.this);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CardRequest> call, Throwable t) {
//                Toast.makeText(TraGopActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void postSaleHD() {
//        String token = "";
//        String fullname = "";
//        String phoneNumber = "";
//        if (user != null) {
//            token = user.getToken();
//            fullname = user.getFullname();
//            phoneNumber = user.getMobile();
//        }
//
//        HashMap<String, Object> body = new HashMap<>();
//        body.put("partner", listHdName.get(spinnerHd.getSelectedItemPosition()));
//        body.put("mk2", editPass.getText().toString());
//        body.put("fullname", fullname);
//        body.put("maKH_HD", editMaHd.getText().toString());
//        body.put("payAmount", editMoneyAmount.getText().toString());
//        body.put("ghichu", editDes.getText().toString());
//        body.put("sodienthoai", phoneNumber);
//
//        loadingDialog.show();
//        postSaleHDAPI = mRetrofitAPI.postSaleHD(token, body);
//        postSaleHDAPI.enqueue(new Callback<OtherRequest>() {
//            @Override
//            public void onResponse(Call<OtherRequest> call, Response<OtherRequest> response) {
//                int errorCode = response.body().getErrorCode();
//                String msg = response.body().getMsg();
//                loadingDialog.dismiss();
//
//                if (errorCode == 1) {
//                    SuccessDialog dialog = new SuccessDialog(TraGopActivity.this, msg);
//                    dialog.show();
//                } else {
//                    Toast.makeText(TraGopActivity.this, msg, Toast.LENGTH_SHORT).show();
//
//                    if (errorCode == -2) {
//                        sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
//                        sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
//                        Constant.restartApp(TraGopActivity.this);
//                    }
//                }
//
//                editMoneyAmount.setText("");
//                editMaHd.setText("");
//                editPass.setText("");
//                editDes.setText("");
//
//                Intent i = new Intent(Constant.UPDATE_INFO);
//                sendBroadcast(i);
//            }
//
//            @Override
//            public void onFailure(Call<OtherRequest> call, Throwable t) {
//                Toast.makeText(TraGopActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                loadingDialog.dismiss();
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.back_btn:
//                finish();
//                break;
//            case R.id.btn_show_pay_save:
//                if (sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
//                    Intent i = new Intent(this, PaySaveActivity.class);
//                    i.putExtra("type", 3);
//                    startActivityForResult(i, 0);
//                } else {
//                    Toast.makeText(TraGopActivity.this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.button_continue:
//                if (sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
//                    postSaleHD();
//                } else {
//                    Toast.makeText(TraGopActivity.this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                String paySave = data.getStringExtra("pay_save");
//                String bankName = data.getStringExtra("bank_name");
//                String fullName = data.getStringExtra("full_name");
//                String phone = data.getStringExtra("contact");
//                String address = data.getStringExtra("address");
//                editMaHd.setText(paySave);
//                editFullname.setText(fullName);
//                editPhone.setText(phone);
//                editAddress.setText(address);
//
//                for (int i = 0; i < listHdName.size(); i++) {
//                    if (listHdName.get(i).equals(bankName)) {
//                        spinnerHd.setSelection(i);
//
//                        if (!editMoneyAmount.getText().toString().equals("")) {
//                            editDes.setText("Thanh toán " + listHdName.get(i) + " với số tiền " + editMoneyAmount.getText().toString() + " VNĐ");
//                        }
//                        break;
//                    }
//                }
//            }
//        }
//    }
}
