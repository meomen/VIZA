package com.vuducminh.viza.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.vuducminh.viza.dialogs.SuccessDialog;
import com.vuducminh.viza.models.History;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.models.BankConnect;
import com.vuducminh.viza.models.order.OrderDeposit;
import com.vuducminh.viza.utils.Constant;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DepositEbankingActivity extends BaseActivity implements View.OnClickListener {

    private MyApplication app;
    private Gson mGson;
    private SharedPreferences sharedPreferences;
    private User user;
    private ImageView backButton,img_logo_bank;
    private Button btn_bankother, btn_deposite,btn_disconnect;
    private TextView tv_number_account,tv_name_owner;
    private EditText edit_so_tien;

    private BankConnect bankConnect;
    private DatabaseReference userRef;
    private String timecreate;
    private int i;
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_deposit_ebanking;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        app = (MyApplication) getApplication();
        mGson = app.getGson();
        sharedPreferences = app.getSharedPreferences();
        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);

        backButton = (ImageView) findViewById(R.id.back_btn);
        img_logo_bank = (ImageView) findViewById(R.id.img_logo_bank);
        btn_bankother = (Button)findViewById(R.id.btn_bankother);
        btn_deposite = (Button)findViewById(R.id.btn_deposite);
        btn_disconnect = (Button)findViewById(R.id.btn_disconnect);
        tv_number_account = (TextView)findViewById(R.id.tv_number_account);
        tv_name_owner = (TextView)findViewById(R.id.tv_name_owner);
        edit_so_tien = (EditText) findViewById(R.id.edit_so_tien);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        backButton.setOnClickListener(this);
        btn_bankother.setOnClickListener(this);
        btn_deposite.setOnClickListener(this);
        btn_disconnect.setOnClickListener(this);


        userRef = FirebaseDatabase.getInstance().getReference(Constant.CUSTOMER)
                .child(user.getMobile());
        userRef.child(Constant.WALLET).child(Constant.CONNECT_BANK).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    bankConnect = dataSnapshot.getValue(BankConnect.class);
                    Picasso.get().load(bankConnect.getLogoBank()).into(img_logo_bank);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void checkPassword() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Xác nhận");
        alert.setMessage("Bạn cần nhập mật khẩu để xác nhận");

        final EditText input = new EditText(this);
        input.setHint("Mật khẩu");
        alert.setView(input);

        alert.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = input.getText().toString();
                if(value.equals(user.getPassword())) {
                    upToHistory();
                    addMoney();
                    upToOrder();
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(DepositEbankingActivity.this,"Mật khẩu không chính xác",Toast.LENGTH_SHORT).show();
                }

            }
        });

        alert.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.show();
    }
    public void upToHistory() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        timecreate = String.valueOf(Calendar.getInstance().getTimeInMillis());

        History newHistory = new History();
        newHistory.setTypeOrder(Constant.TYPE_DEPOSTI);
        newHistory.setPrice(Integer.parseInt(edit_so_tien.getText().toString()));
        newHistory.setDateCreated(timecreate);
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
                            money += i;

                        }
                        userRef.child(Constant.MONEY).setValue(money);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(DepositEbankingActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void upToOrder() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        OrderDeposit orderDeposit = new OrderDeposit();
        orderDeposit.setPrice(Integer.parseInt(edit_so_tien.getText().toString()));
        orderDeposit.setIssuingHousCard(bankConnect.getBankName());
        orderDeposit.setDateCreate(timecreate);
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
                        SuccessDialog dialog = new SuccessDialog(DepositEbankingActivity.this, "Nạp tiền thành công");
                        dialog.show();
                        addMoney();
                        edit_so_tien.setText("");
                    }
                });
    }
    public void disconnectBank() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Xác nhận");
        alert.setMessage("Bạn chắc chắn hủy liên kết với ngân hàng hiện tại chứ ?");



        alert.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userRef.child(Constant.WALLET).child(Constant.CONNECT_BANK).removeValue();
                finish();
            }
        });

        alert.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
    @Override
    public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_btn:
                    finish();
                    break;
                case R.id.btn_deposite:
                    String s = edit_so_tien.getText().toString();
                    if(s.isEmpty()) {
                        Toast.makeText(DepositEbankingActivity.this,"Bạn chưa nhập số tiền muốn nạp",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    i = Integer.parseInt(s);
                    if(i < 10000) {
                        Toast.makeText(DepositEbankingActivity.this,"Cần nạp tối thiểu 10.000 VNĐ",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(i > 5000000) {
                        Toast.makeText(DepositEbankingActivity.this,"Chỉ nạp tối đa 5.000.000 VNĐ",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    checkPassword();
                    break;

                case R.id.btn_bankother: {
                    Intent intent = new Intent(DepositEbankingActivity.this,ConnectBankActivity.class);
                    intent.putExtra("connected",true);
                    startActivity(intent);
                    break;
                }
                case R.id.btn_disconnect:
                    disconnectBank();
                    break;
            }
    }
//    private Gson mGson;
//    private SharedPreferences sharedPreferences;
//    private User user;
//
//    private ImageView backButton;
//    private WebView webView;
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_deposit_ebanking;
//    }
//
//    @Override
//    protected void initVariables(Bundle savedInstanceState) {
//        mGson = MyApplication.getGson();
//        sharedPreferences = MyApplication.getSharedPreferences();
//        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);
//
//        backButton = (ImageView) findViewById(R.id.back_btn);
//        webView = (WebView) findViewById(R.id.web_view);
//    }
//
//    @Override
//    protected void initData(Bundle savedInstanceState) {
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
//        if (user != null) {
//            webView.setWebViewClient(new WebViewClient(){
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                    return true;
//                }
//            });
//            webView.setWebChromeClient(new WebChromeClient());
//            webView.loadUrl(Constant.EBANKING_URL + user.getToken());
//        } else {
//            Toast.makeText(this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
//        }
//
//        Constant.increaseHitArea(backButton);
//        backButton.setOnClickListener(this);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//            webView.goBack();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public void onClick(View v) {
//        Constant.restartApp(this);
//    }
}
