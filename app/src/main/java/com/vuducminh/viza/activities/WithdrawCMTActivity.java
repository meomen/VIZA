package com.vuducminh.viza.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class WithdrawCMTActivity extends BaseActivity implements View.OnClickListener {
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
//    private EditText editIdentity;
//    private ImageView btnShowPaySave;
//    private EditText editFullname;
//    private Spinner spinnerPlace;
//    private ArrayAdapter<String> adapter;
//    private ArrayList<String> listPlace;
//    private EditText editDate;
//    private ImageView pickDate;
//    private EditText editPhone;
//    private EditText editPass;
//    private Button continueButton;
//    private LoadingDialog loadingDialog;
//
//    private Call<OtherRequest> postCMTdrawAPI;
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_withdraw_cmt;
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
//        editFullname = (EditText) findViewById(R.id.edit_fullname);
//        editIdentity = (EditText) findViewById(R.id.edit_identity);
//        btnShowPaySave = (ImageView) findViewById(R.id.btn_show_pay_save);
//        spinnerPlace = (Spinner) findViewById(R.id.spinner_place);
//        editDate = (EditText) findViewById(R.id.edit_date);
//        pickDate = (ImageView) findViewById(R.id.pick_date);
//        editPhone = (EditText) findViewById(R.id.edit_phone);
//        editPass = (EditText) findViewById(R.id.edit_pass);
//        continueButton = (Button) findViewById(R.id.button_continue);
//    }
//
//    @Override
//    protected void initData(Bundle savedInstanceState) {
//        listPlace = new ArrayList<>(Arrays.asList("Hà Nội",
//                "TP HCM",
//                "Cần Thơ",
//                "Đà Nẵng",
//                "Hải Phòng",
//                "An Giang",
//                "Bà Rịa - Vũng Tàu",
//                "Bắc Giang",
//                "Bắc Kạn",
//                "Bạc Liêu",
//                "Bắc Ninh",
//                "Bến Tre",
//                "Bình Định",
//                "Bình Dương",
//                "Bình Phước",
//                "Bình Thuận",
//                "Cà Mau",
//                "Cao Bằng",
//                "Đắk Lắk",
//                "Đắk Nông",
//                "Điện Biên",
//                "Đồng Nai",
//                "Đồng Tháp",
//                "Gia Lai",
//                "Hà Giang",
//                "Hà Nam",
//                "Hà Tĩnh",
//                "Hải Dương",
//                "Hậu Giang",
//                "Hòa Bình",
//                "Hưng Yên",
//                "Khánh Hòa",
//                "Kiên Giang",
//                "Kon Tum",
//                "Lai Châu",
//                "Lâm Đồng",
//                "Lạng Sơn",
//                "Lào Cai",
//                "Long An",
//                "Nam Định",
//                "Nghệ An",
//                "Ninh Bình",
//                "Ninh Thuận",
//                "Phú Thọ",
//                "Quảng Bình",
//                "Quảng Nam",
//                "Quảng Ngãi",
//                "Quảng Ninh",
//                "Quảng Trị",
//                "Sóc Trăng",
//                "Sơn La",
//                "Tây Ninh",
//                "Thái Bình",
//                "Thái Nguyên",
//                "Thanh Hóa",
//                "Thừa Thiên Huế",
//                "Tiền Giang",
//                "Trà Vinh",
//                "Tuyên Quang",
//                "Vĩnh Long",
//                "Vĩnh Phúc",
//                "Yên Bái",
//                "Phú Yên"));
//
//        adapter = new ArrayAdapter<>(this, R.layout.item_spinner, listPlace);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerPlace.setAdapter(adapter);
//        spinnerPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        Constant.increaseHitArea(backButton);
//        Constant.increaseHitArea(btnShowPaySave);
//        backButton.setOnClickListener(this);
//        btnShowPaySave.setOnClickListener(this);
//        pickDate.setOnClickListener(this);
//        continueButton.setOnClickListener(this);
//    }
//
//    private void postCMTdraw() {
//        String token = "";
//        if (user != null) {
//            token = user.getToken();
//        }
//
//        HashMap<String, Object> body = new HashMap<>();
//        body.put("sotienrut", editMoneyAmount.getText().toString());
//        body.put("chinhanh", listPlace.get(spinnerPlace.getSelectedItemPosition()));
//        body.put("sodienthoai", editPhone.getText().toString());
//        body.put("noicap", listPlace.get(spinnerPlace.getSelectedItemPosition()));
//        body.put("ngaycap", editDate.getText().toString());
//        body.put("fullname", editFullname.getText().toString());
//        body.put("soCMT", editIdentity.getText().toString());
//        body.put("mk2", editPass.getText().toString());
//
//        loadingDialog.show();
//        postCMTdrawAPI = mRetrofitAPI.postCMTdraw(token, body);
//        postCMTdrawAPI.enqueue(new Callback<OtherRequest>() {
//            @Override
//            public void onResponse(Call<OtherRequest> call, Response<OtherRequest> response) {
//                int errorCode = response.body().getErrorCode();
//                String msg = response.body().getMsg();
//                loadingDialog.dismiss();
//
//                if (errorCode == 1) {
//                    SuccessDialog dialog = new SuccessDialog(WithdrawCMTActivity.this, msg);
//                    dialog.show();
//                } else {
//                    Toast.makeText(WithdrawCMTActivity.this, msg, Toast.LENGTH_SHORT).show();
//
//                    if (errorCode == -2) {
//                        sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
//                        sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
//                        Constant.restartApp(WithdrawCMTActivity.this);
//                    }
//                }
//
//                editMoneyAmount.setText("");
//                editIdentity.setText("");
//                editFullname.setText("");
//                editDate.setText("");
//                editPhone.setText("");
//                editPass.setText("");
//
//                Intent i = new Intent(Constant.UPDATE_INFO);
//                sendBroadcast(i);
//            }
//
//            @Override
//            public void onFailure(Call<OtherRequest> call, Throwable t) {
//                Toast.makeText(WithdrawCMTActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
//                    i.putExtra("type", 2);
//                    startActivityForResult(i, 0);
//                } else {
//                    Toast.makeText(WithdrawCMTActivity.this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.pick_date:
//                Calendar calendar = Calendar.getInstance();
//                DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
//                        String dateString = Constant.formatTime(dayOfMonth) + "/" + Constant.formatTime(month + 1) + "/" + year;
//                        editDate.setText(dateString);
//                    }
//                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
//                datePicker.show();
//                break;
//            case R.id.button_continue:
//                if (sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
//                    postCMTdraw();
//                } else {
//                    Toast.makeText(WithdrawCMTActivity.this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
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
//                String fullName = data.getStringExtra("full_name");
//                String contact = data.getStringExtra("contact");
//                String date = data.getStringExtra("date");
//                editIdentity.setText(paySave);
//                editFullname.setText(fullName);
//                editPhone.setText(contact);
//                editDate.setText(date);
//            }
//        }
//    }
}
