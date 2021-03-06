package com.vuducminh.viza.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.adapters.ListCardAdapter;
import com.vuducminh.viza.dialogs.LoadingDialog;
import com.vuducminh.viza.dialogs.SuccessDialog;
import com.vuducminh.viza.models.CardObject;
import com.vuducminh.viza.models.CardRequest;
import com.vuducminh.viza.models.History;
import com.vuducminh.viza.models.OtherRequest;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.models.order.OrderDeposit;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;
import com.vuducminh.viza.utils.GridSpacingItemDecoration;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DepositCardActivity extends BaseActivity implements View.OnClickListener {

    private MyApplication app;
    private Gson mGson;
    private Retrofit mRetrofit;
    private IRetrofitAPI mRetrofitAPI;
    private SharedPreferences sharedPreferences;
    private User user;

    private RecyclerView listCardView;
    private ListCardAdapter adapter;
    private ArrayList<CardObject> listCard;
    private ImageView backButton;
    //private EditText editTypeCard;
    private EditText editSerie;
    private EditText editMaThe;
    private Button continueButton;
    private int curPos = 0;
    private LoadingDialog loadingDialog;

    private Call<CardRequest> getCardDPTAPI;
    private String timecreated;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_deposit_card;
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
        listCardView = (RecyclerView) findViewById(R.id.list_card);
        //editTypeCard = (EditText) findViewById(R.id.edit_type_card);
        editSerie = (EditText) findViewById(R.id.edit_serie);
        editMaThe = (EditText) findViewById(R.id.edit_ma_the);
        continueButton = (Button) findViewById(R.id.button_continue);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        listCard = new ArrayList<>();
        getListCard();

        adapter = new ListCardAdapter(this, listCard, new ListCardAdapter.PositionClickListener() {
            @Override
            public void itemClicked(int position) {
                curPos = position;
            }
        });
        adapter.setHasStableIds(true);
        adapter.setDPT(true);

        listCardView.setLayoutManager(new GridLayoutManager(this, 3));
        listCardView.addItemDecoration(new GridSpacingItemDecoration(3, Constant.convertDpIntoPixels(20, this), true));
        listCardView.setAdapter(adapter);

        Constant.increaseHitArea(backButton);
        backButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
    }

    private void getListCard() {
        getCardDPTAPI = mRetrofitAPI.getCardDPT();
        getCardDPTAPI.enqueue(new Callback<CardRequest>() {
            @Override
            public void onResponse(Call<CardRequest> call, Response<CardRequest> response) {
                int errorCode = response.body().getErrorCode();
                String msg = response.body().getMsg();

                if (errorCode == 1) {
                    listCard.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DepositCardActivity.this, msg, Toast.LENGTH_SHORT).show();

                    if (errorCode == -2) {
                        sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
                        sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
                        Constant.restartApp(DepositCardActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<CardRequest> call, Throwable t) {
                Toast.makeText(DepositCardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void upToOrder() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        OrderDeposit orderDeposit = new OrderDeposit();
        orderDeposit.setPrice(100000);
        orderDeposit.setIssuingHousCard(listCard.get(curPos).getBankName());
        orderDeposit.setSeriCard(editSerie.getText().toString());
        orderDeposit.setCodeCard(editMaThe.getText().toString());
        orderDeposit.setDateCreate(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        orderDeposit.setDateFormat(date);

        FirebaseDatabase.getInstance().getReference(Constant.CUSTOMER)
                .child(user.getMobile())
                .child(Constant.ORDER)
                .child(Constant.ORDER_DEPOSIT)
                .child(orderDeposit.getDateCreate())
                .setValue(orderDeposit)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SuccessDialog dialog = new SuccessDialog(DepositCardActivity.this, "Nạp tiền thành công");
                        dialog.show();
                        addMoney();
                        editSerie.setText("");
                        editMaThe.setText("");
                    }
                });
    }

    public void upToHistory() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        timecreated = String.valueOf(Calendar.getInstance().getTimeInMillis());
        History newHistory = new History();
        newHistory.setTypeOrder(Constant.TYPE_DEPOSTI);
        newHistory.setPrice(10000);
        newHistory.setDateCreated(timecreated);
        newHistory.setDateFormat(date);

        FirebaseDatabase.getInstance().getReference(Constant.CUSTOMER)
                .child(user.getMobile())
                .child(Constant.HISTORY)
                .child(newHistory.getDateCreated())
                .setValue(newHistory);
    }

    public void addMoney() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constant.CUSTOMER)
                .child(user.getMobile()).child(Constant.WALLET);
        userRef.child(Constant.MONEY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer money = 0;
                        if (dataSnapshot.exists()) {
                            money = dataSnapshot.getValue(Integer.class);
                            money += 100000;

                        }
                        userRef.child(Constant.MONEY).setValue(money);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(DepositCardActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean checkEditText() {
        if(editMaThe.getText().toString().isEmpty()) {
            Toast.makeText(DepositCardActivity.this,"Vui lòng nhập mã thẻ",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(editSerie.getText().toString().isEmpty()) {
            Toast.makeText(DepositCardActivity.this,"Vui lòng nhập số seri",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_btn:
                    finish();
                    break;
                case R.id.button_continue:
                    if (sharedPreferences.getBoolean(Constant.IS_LOGIN, false) && checkEditText()) {
                        upToHistory();
                        upToOrder();

                    } else {
                        Toast.makeText(DepositCardActivity.this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
    }
}
