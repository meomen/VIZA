package com.vuducminh.viza.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.adapters.ListBankAdapter;
import com.vuducminh.viza.adapters.ListPaySaveAdapter;
import com.vuducminh.viza.models.CardObject;
import com.vuducminh.viza.models.CardRequest;
import com.vuducminh.viza.models.PaySaveObject;
import com.vuducminh.viza.models.PaySaveRequest;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;
import com.vuducminh.viza.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class PaySaveActivity extends BaseActivity implements View.OnClickListener {
    private Gson mGson;
    private Retrofit mRetrofit;
    private IRetrofitAPI mRetrofitAPI;
    private SharedPreferences sharedPreferences;
    private User user;

    private ImageView backButton;
    private TextView textTitle;
    private EditText editSearch;
    private LinearLayout searchLayout;
    private TextView textSearch;
    private String searchContent = "";
    private RecyclerView listPaySaveView;
    private ListPaySaveAdapter paySaveAdapter;
    private ArrayList<PaySaveObject> listPaySave;
    private ListBankAdapter bankAdapter;
    private ArrayList<CardObject> listBank;
    private int type;
    private String channel;

    private Call<PaySaveRequest> getPaySaveAPI;
    private Call<CardRequest> getCardInfoAPI;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_pay_save;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mGson = MyApplication.getGson();
        mRetrofit = MyApplication.getRetrofit();
        mRetrofitAPI = mRetrofit.create(IRetrofitAPI.class);
        sharedPreferences = MyApplication.getSharedPreferences();
        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);

        backButton = (ImageView) findViewById(R.id.back_btn);
        textTitle = (TextView) findViewById(R.id.title_text);
        editSearch = (EditText) findViewById(R.id.edit_search);
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        textSearch = (TextView) findViewById(R.id.text_search);
        listPaySaveView = (RecyclerView) findViewById(R.id.list_pay_save);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        listPaySave = new ArrayList<>();
        listBank = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getInt("type");
            channel = extras.getString("channel");
            switch (type) {
                case 0:
                    getListBank(channel);
                    textTitle.setText("Danh sách ngân hàng");
                    textSearch.setText("Tìm kiếm ngân hàng");
                    break;
                case 1:
                    getPaySave(type);
                    textTitle.setText("Danh sách thẻ ATM");
                    textSearch.setText("Tìm kiếm thẻ ATM");
                    break;
                case 2:
                    getPaySave(type);
                    textTitle.setText("Danh sách chứng minh thư");
                    textSearch.setText("Tìm kiếm chứng minh thư");
                    break;
                case 3:
                    getPaySave(type);
                    textTitle.setText("Danh sách mã hợp đồng");
                    textSearch.setText("Tìm kiếm mã hợp đồng");
                    break;
            }
        }

        paySaveAdapter = new ListPaySaveAdapter(this, listPaySave, new ListPaySaveAdapter.PositionClickListener() {
            @Override
            public void itemClicked(int position) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("pay_save", paySaveAdapter.getItem(position).getCardnumber());
                resultIntent.putExtra("full_name", paySaveAdapter.getItem(position).getFullname());
                resultIntent.putExtra("bank_name", paySaveAdapter.getItem(position).getBankname());
                resultIntent.putExtra("contact", paySaveAdapter.getItem(position).getContact());
                resultIntent.putExtra("date", paySaveAdapter.getItem(position).getDateIssue());
                resultIntent.putExtra("branch", paySaveAdapter.getItem(position).getBranch());
                resultIntent.putExtra("address", paySaveAdapter.getItem(position).getPlaceIssue());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        paySaveAdapter.setHasStableIds(true);

        bankAdapter = new ListBankAdapter(this, listBank, new ListBankAdapter.PositionClickListener() {
            @Override
            public void itemClicked(int position) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("bank", bankAdapter.getItem(position).getBankName());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        bankAdapter.setHasStableIds(true);

        listPaySaveView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listPaySaveView.addItemDecoration(new SimpleDividerItemDecoration(this));
        if (type == 0) {
            listPaySaveView.setAdapter(bankAdapter);
        } else {
            listPaySaveView.setAdapter(paySaveAdapter);
        }

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    searchContent = s.toString();
                } else {
                    searchLayout.setVisibility(View.VISIBLE);
                    editSearch.setVisibility(View.GONE);
                    searchContent = "";
                }

                if (type == 0) {
                    bankAdapter.getFilter().filter(s);
                } else {
                    paySaveAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Constant.increaseHitArea(backButton);
        backButton.setOnClickListener(this);
        searchLayout.setOnClickListener(this);
    }

    private void getPaySave(int type) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("type", type);

        getPaySaveAPI = mRetrofitAPI.getPaySave(user.getToken(), body);
        getPaySaveAPI.enqueue(new Callback<PaySaveRequest>() {
            @Override
            public void onResponse(Call<PaySaveRequest> call, Response<PaySaveRequest> response) {
                int errorCode = response.body().getErrorCode();
                String msg = response.body().getMsg();

                if (errorCode == 1) {
                    listPaySave.addAll(response.body().getData());
                    paySaveAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PaySaveActivity.this, msg, Toast.LENGTH_SHORT).show();

                    if (errorCode == -2) {
                        sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
                        sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
                        Constant.restartApp(PaySaveActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<PaySaveRequest> call, Throwable t) {
                Toast.makeText(PaySaveActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListBank(String channel) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("channel", channel);

        getCardInfoAPI = mRetrofitAPI.getCardInfo(body);
        getCardInfoAPI.enqueue(new Callback<CardRequest>() {
            @Override
            public void onResponse(Call<CardRequest> call, Response<CardRequest> response) {
                int errorCode = response.body().getErrorCode();
                String msg = response.body().getMsg();

                if (errorCode == 1) {
                    listBank.addAll(response.body().getData());
                    bankAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PaySaveActivity.this, msg, Toast.LENGTH_SHORT).show();

                    if (errorCode == -2) {
                        sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
                        sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
                        Constant.restartApp(PaySaveActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<CardRequest> call, Throwable t) {
                Toast.makeText(PaySaveActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.search_layout:
                searchLayout.setVisibility(View.GONE);
                editSearch.setVisibility(View.VISIBLE);
                editSearch.requestFocus();
                break;
        }
    }
}
