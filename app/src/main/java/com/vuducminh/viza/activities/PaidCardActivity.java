package com.vuducminh.viza.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.adapters.ListPaidCardAdapter;
import com.vuducminh.viza.models.TransactionObject;
import com.vuducminh.viza.models.TransactionRequest;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class PaidCardActivity extends BaseActivity implements View.OnClickListener {
    private Gson mGson;
    private Retrofit mRetrofit;
    private IRetrofitAPI mRetrofitAPI;
    private SharedPreferences sharedPreferences;
    private User user;

    private ImageView backBtn;
    private ListView listPaidCardView;
    private ListPaidCardAdapter adapter;
    private ArrayList<TransactionObject> listPaidCard;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private String telco = "";

    private Call<TransactionRequest> getAllTransAPI;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_paid_card;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mGson = MyApplication.getGson();
        mRetrofit = MyApplication.getRetrofit();
        mRetrofitAPI = mRetrofit.create(IRetrofitAPI.class);
        sharedPreferences = MyApplication.getSharedPreferences();
        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);

        backBtn = (ImageView) findViewById(R.id.back_btn);
        listPaidCardView = (ListView) findViewById(R.id.list_paid_card);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            telco = extras.getString("telco");
        }

        listPaidCard = new ArrayList<>();
        getListPaidCard();

        adapter = new ListPaidCardAdapter(this, listPaidCard);
        listPaidCardView.setAdapter(adapter);

        Constant.increaseHitArea(backBtn);
        backBtn.setOnClickListener(this);
    }

    private void getListPaidCard() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date fromDate = calendar.getTime();

        HashMap<String, Object> body = new HashMap<>();
        body.put("value", telco);
        body.put("fieldName", "partnerCode");
        body.put("type", "");
        body.put("fromDate", dateFormat.format(fromDate));
        body.put("toDate", dateFormat.format(new Date()));

        getAllTransAPI = mRetrofitAPI.getAllTransaction(user.getToken(), body);
        getAllTransAPI.enqueue(new Callback<TransactionRequest>() {
            @Override
            public void onResponse(Call<TransactionRequest> call, Response<TransactionRequest> response) {
                int errorCode = response.body().getErrorCode();
                String msg = response.body().getMsg();

                if (errorCode == 1) {
                    listPaidCard.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PaidCardActivity.this, msg, Toast.LENGTH_SHORT).show();

                    if (errorCode == -2) {
                        sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
                        sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
                        Constant.restartApp(PaidCardActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionRequest> call, Throwable t) {
                Toast.makeText(PaidCardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
