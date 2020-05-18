package com.vuducminh.viza.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.adapters.ListCardAdapter;
import com.vuducminh.viza.dialogs.ErrorDialog;
import com.vuducminh.viza.dialogs.LoadingDialog;
import com.vuducminh.viza.dialogs.SuccessDialog;
import com.vuducminh.viza.models.CardObject;
import com.vuducminh.viza.models.CardRequest;
import com.vuducminh.viza.models.History;
import com.vuducminh.viza.models.MoneyRequest;
import com.vuducminh.viza.models.OtherRequest;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.models.order.OrderCard;
import com.vuducminh.viza.models.order.OrderDeposit;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;
import com.vuducminh.viza.utils.GridSpacingItemDecoration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class BuyPhoneCardActivity extends BaseActivity implements View.OnClickListener {

    private Gson mGson;
    private Retrofit mRetrofit;
    private IRetrofitAPI mRetrofitAPI;
    private SharedPreferences sharedPreferences;
    private User user;

    private ImageView backButton;
    private RecyclerView listPhoneCardView;
    private ListCardAdapter adapter;
    private ArrayList<CardObject> listPhoneCard;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> listPrice;
    private ArrayList<String> listInfo;
//    private TextView textAmount;
//    private ImageView minusButton;
//    private ImageView plusButton;
    private TextView textThanhToan;
    private EditText editMk2;
    private Button continueButton;
    private TextView viewCard;
    private int curPos = 0;
    private int amount = 1;
    private LoadingDialog loadingDialog;

    private Call<CardRequest> getCardInfoAPI;
    private Call<OtherRequest> buyCardAPI;
    private Call<MoneyRequest> getRealMoneyAPI;

    private String timecreated;
    private int paymoney;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_buy_phone_card;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mGson = MyApplication.getGson();
        mRetrofit = MyApplication.getRetrofit();
        mRetrofitAPI = mRetrofit.create(IRetrofitAPI.class);
        sharedPreferences = MyApplication.getSharedPreferences();
        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);

        loadingDialog = new LoadingDialog(this);

        backButton = (ImageView) findViewById(R.id.back_btn);
        listPhoneCardView = (RecyclerView) findViewById(R.id.list_phone_card);
        spinner = (Spinner) findViewById(R.id.spinner);
//        textAmount = (TextView) findViewById(R.id.text_amount);
//        minusButton = (ImageView) findViewById(R.id.button_minus);
//        plusButton = (ImageView) findViewById(R.id.button_plus);
        textThanhToan = (TextView) findViewById(R.id.text_thanh_toan);
        editMk2 = (EditText) findViewById(R.id.edit_mk2);
        continueButton = (Button) findViewById(R.id.continue_button);
        viewCard = (TextView) findViewById(R.id.view_card);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        listPrice = new ArrayList<>();
        listInfo = new ArrayList<>();
        listPhoneCard = new ArrayList<>();
        getListCard();

        adapter = new ListCardAdapter(this, listPhoneCard, new ListCardAdapter.PositionClickListener() {
            @Override
            public void itemClicked(int position) {
                curPos = position;

                listInfo.removeAll(listInfo);
                listPrice.removeAll(listPrice);
                listInfo.addAll(Arrays.asList(listPhoneCard.get(position).getInfo().split("\\|")));
                for (int i = 0; i < listInfo.size(); i++) {
                    listPrice.add(listInfo.get(i) + " VNĐ");
                }
                spinnerAdapter.notifyDataSetChanged();
                spinner.setSelection(0);

                getThanhToan(0);
            }
        });
        adapter.setHasStableIds(true);
        adapter.setDPT(false);

        listPhoneCardView.setLayoutManager(new GridLayoutManager(this, 3));
        listPhoneCardView.addItemDecoration(new GridSpacingItemDecoration(3, Constant.convertDpIntoPixels(20, this), true));
        listPhoneCardView.setAdapter(adapter);

        spinnerAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, listPrice);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getThanhToan(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        textAmount.setText(amount + "");

        Constant.increaseHitArea(backButton);
//        Constant.increaseHitArea(minusButton);
//        Constant.increaseHitArea(plusButton);
        Constant.increaseHitArea(viewCard);

        backButton.setOnClickListener(this);
//        minusButton.setOnClickListener(this);
//        plusButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        viewCard.setOnClickListener(this);
    }

    private void getListCard() {
        HashMap<String, Object> body = new HashMap<>();
        body.put("channel", "telco");

        getCardInfoAPI = mRetrofitAPI.getCardInfo(body);
        getCardInfoAPI.enqueue(new Callback<CardRequest>() {
            @Override
            public void onResponse(Call<CardRequest> call, Response<CardRequest> response) {
                int errorCode = response.body().getErrorCode();
                String msg = response.body().getMsg();

                if (errorCode == 1) {
                    listPhoneCard.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();

                    listInfo.addAll(Arrays.asList(listPhoneCard.get(0).getInfo().split("\\|")));
                    for (int i = 0; i < listInfo.size(); i++) {
                        listPrice.add(listInfo.get(i) + " VNĐ");
                    }
                    spinnerAdapter.notifyDataSetChanged();

                    getThanhToan(0);
                } else {
                    Toast.makeText(BuyPhoneCardActivity.this, msg, Toast.LENGTH_SHORT).show();

                    if (errorCode == -2) {
                        sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
                        sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
                        Constant.restartApp(BuyPhoneCardActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<CardRequest> call, Throwable t) {
                Toast.makeText(BuyPhoneCardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buyCard() {
        loadingDialog.show();
        upToHistory();
        updateMoney();
        upToOrder();
    }

    private void getThanhToan(int spinnerPos) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("product", listPhoneCard.get(curPos).getBankName());
        body.put("menhgia", listInfo.get(spinnerPos));
        body.put("soluong", amount);

        String token = "";
        if (user != null) {
            token = user.getToken();
        }

        loadingDialog.show();
        getRealMoneyAPI = mRetrofitAPI.getRealAmountBuyCard(token, body);
        getRealMoneyAPI.enqueue(new Callback<MoneyRequest>() {
            @Override
            public void onResponse(Call<MoneyRequest> call, Response<MoneyRequest> response) {
                paymoney = response.body().getData();
                textThanhToan.setText(paymoney + " VNĐ");

                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MoneyRequest> call, Throwable t) {
                Toast.makeText(BuyPhoneCardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    public void updateMoney() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constant.CUSTOMER)
                .child(user.getMobile()).child(Constant.WALLET);
        userRef.child(Constant.MONEY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer money = 0;
                        if (dataSnapshot.exists()) {
                            money = dataSnapshot.getValue(Integer.class);
                            if(money < paymoney) {
                                ErrorDialog errorDialog = new ErrorDialog(BuyPhoneCardActivity.this,
                                        "Ví của bạn không đủ tiền, xin vui lòng nạp thêm");
                                errorDialog.show();
                            }
                            else {
                                money -= paymoney;
                            }

                        }
                        userRef.child(Constant.MONEY).setValue(money);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(BuyPhoneCardActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void upToHistory() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        timecreated = String.valueOf(Calendar.getInstance().getTimeInMillis());
        History newHistory = new History();
        newHistory.setTypeOrder(Constant.TYPE_BUY_CARD_PHONE);
        newHistory.setPrice(spinner.getSelectedItemPosition());
        newHistory.setDateCreated(timecreated);
        newHistory.setDateFormat(date);

        FirebaseDatabase.getInstance().getReference(Constant.CUSTOMER)
                .child(user.getMobile())
                .child(Constant.HISTORY)
                .child(newHistory.getDateCreated())
                .setValue(newHistory);
    }
    public void upToOrder() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        OrderCard orderDeposit = new OrderCard();
        orderDeposit.setPrice(spinner.getSelectedItemPosition());
        orderDeposit.setIssuingHousCard(listPhoneCard.get(curPos).getBankName());
        orderDeposit.setSeriCard("517048427861481");
        orderDeposit.setCodeCard("10005930442464");
        orderDeposit.setDateCreate(timecreated);
        orderDeposit.setDateFormat(date);

        FirebaseDatabase.getInstance().getReference(Constant.CUSTOMER)
                .child(user.getMobile())
                .child(Constant.ORDER)
                .child(Constant.ORDER_CARD_PHONE)
                .child(orderDeposit.getDateCreate())
                .setValue(orderDeposit)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingDialog.dismiss();
                        SuccessDialog dialog = new SuccessDialog(BuyPhoneCardActivity.this, "Nạp tiền thành công");
                        dialog.show();
                    }
                });
    }

    public boolean checkPassword() {
        String mk2 = editMk2.getText().toString();
        if(mk2.isEmpty()){
            Toast.makeText(BuyPhoneCardActivity.this,"Vui lòng xác nhận mua bằng mật khẩu",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!mk2.equals(user.getPassword())) {
            Toast.makeText(BuyPhoneCardActivity.this,"Mật khẩu không chính xác",Toast.LENGTH_SHORT).show();
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
//            case R.id.button_minus:
//                if (amount > 1) {
//                    amount--;
//                    textAmount.setText(amount + "");
//                }
//                break;
//            case R.id.button_plus:
//                if (amount < 99) {
//                    amount++;
//                    textAmount.setText(amount + "");
//                }
//                break;
            case R.id.continue_button:
                if (sharedPreferences.getBoolean(Constant.IS_LOGIN, false) && checkPassword()) {
                    buyCard();
                } else {
                    Toast.makeText(BuyPhoneCardActivity.this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.view_card:
                if (sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
                    Intent i = new Intent(this, ListCardActivity.class);
                    i.putExtra("channel", "telco");
                    startActivity(i);
                } else {
                    Toast.makeText(BuyPhoneCardActivity.this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
