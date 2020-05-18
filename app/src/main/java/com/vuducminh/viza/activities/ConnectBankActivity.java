package com.vuducminh.viza.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.adapters.ListBankConnectAdapter;
import com.vuducminh.viza.dialogs.LoadingDialog;
import com.vuducminh.viza.dialogs.NotificationDialog;
import com.vuducminh.viza.dialogs.SuccessDialog;
import com.vuducminh.viza.models.CardObject;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.models.BankConnect;
import com.vuducminh.viza.utils.Constant;
import com.vuducminh.viza.utils.GridSpacingItemDecoration;

import java.util.ArrayList;

public class ConnectBankActivity extends BaseActivity implements View.OnClickListener {
    private MyApplication app;
    private Gson mGson;
    private SharedPreferences sharedPreferences;
    private User user;

    private RecyclerView listCardView;
    private ListBankConnectAdapter adapter;
    private ArrayList<CardObject> listCard;
    private ImageView backButton;
    //private EditText editTypeCard;
    private EditText editNumberAccount;
    private EditText editOwnedAccount;
    private Button continueButton;
    private int curPos = 0;
    private LoadingDialog loadingDialog;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_connect_bank;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        app = (MyApplication) getApplication();
        mGson = app.getGson();
        sharedPreferences = app.getSharedPreferences();
        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);

        loadingDialog = new LoadingDialog(this);
        backButton = (ImageView) findViewById(R.id.back_btn);
        listCardView = (RecyclerView) findViewById(R.id.list_card);
        editNumberAccount = (EditText) findViewById(R.id.edit_number_account);
        editOwnedAccount = (EditText) findViewById(R.id.edit_owned_account);
        continueButton = (Button) findViewById(R.id.button_continue);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        listCard = new ArrayList<>();
        getListCard();

        adapter = new ListBankConnectAdapter(this, listCard, new ListBankConnectAdapter.PositionClickListener() {
            @Override
            public void itemClicked(int position) {
                curPos = position;
            }
        });
        adapter.setHasStableIds(true);
        adapter.notifyDataSetChanged();

        listCardView.setLayoutManager(new GridLayoutManager(this, 3));
        listCardView.addItemDecoration(new GridSpacingItemDecoration(3, Constant.convertDpIntoPixels(20, this), true));
        listCardView.setAdapter(adapter);

        Constant.increaseHitArea(backButton);
        backButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        if (!getIntent().getBooleanExtra("connected", false)) {
            NotificationDialog dialog = new NotificationDialog(ConnectBankActivity.this, "Cần liên kết ngân hàng trước khi sử dụng dịch vụ này");
            dialog.show();
        }

    }

    private void getListCard() {
        listCard.addAll(getBank());

    }

    private ArrayList<CardObject> getBank() {
        ArrayList<CardObject> listCardObjects = new ArrayList<CardObject>();
        listCardObjects.add(new CardObject("", "AgriBank", "https://firebasestorage.googleapis.com/v0/b/viza-41f35.appspot.com/o/agribank.png?alt=media&token=8a2a9ffa-34ed-4019-b0b5-68ac384e02de"));
        listCardObjects.add(new CardObject("", "VietcomBank", "https://firebasestorage.googleapis.com/v0/b/viza-41f35.appspot.com/o/vietcombank.jpg?alt=media&token=7d0f1e85-1ef7-4341-8ac4-79dc509a1e8c"));
        listCardObjects.add(new CardObject("", "Vietinbank", "https://firebasestorage.googleapis.com/v0/b/viza-41f35.appspot.com/o/viettinbank.jpg?alt=media&token=1c58d0c7-2f05-4c5f-aca5-a0c07131913b"));
        listCardObjects.add(new CardObject("", "TechcomBank", "https://firebasestorage.googleapis.com/v0/b/viza-41f35.appspot.com/o/techcombank.jpg?alt=media&token=61df94f0-6a21-4845-b2a5-dab1518ea48b"));
        listCardObjects.add(new CardObject("", "OceanBank", "https://firebasestorage.googleapis.com/v0/b/viza-41f35.appspot.com/o/ocebank.jpg?alt=media&token=5be062ce-fec9-45f1-b48d-807886aefb8e"));
        listCardObjects.add(new CardObject("", "SeaBank", "https://firebasestorage.googleapis.com/v0/b/viza-41f35.appspot.com/o/seabank.png?alt=media&token=b4d4945d-9577-4f81-9f37-60fb70aa90f8"));
        return listCardObjects;
    }

    public void updateBankConnect() {

        BankConnect bankConnect = new BankConnect(listCard.get(curPos).getBankName()
                , editNumberAccount.getText().toString()
                , editOwnedAccount.getText().toString()
                , listCard.get(curPos).getImage());

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constant.CUSTOMER)
                .child(user.getMobile()).child(Constant.WALLET);
        userRef.child(Constant.CONNECT_BANK)
                .setValue(bankConnect).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                SuccessDialog dialog = new SuccessDialog(ConnectBankActivity.this, "Liên kết tài khoản thành công");
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(ConnectBankActivity.this, DepositEbankingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.show();
            }
        });
    }

    public boolean checkEditText() {
        if (editNumberAccount.getText().toString().isEmpty()) {
            Toast.makeText(ConnectBankActivity.this, "Vui lòng nhập mã thẻ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editOwnedAccount.getText().toString().isEmpty()) {
            Toast.makeText(ConnectBankActivity.this, "Vui lòng nhập số seri", Toast.LENGTH_SHORT).show();
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
                if (checkEditText()) {
                    updateBankConnect();
                    break;
                }
        }
    }
}
