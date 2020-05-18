package com.vuducminh.viza.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.dialogs.PhoneNumberDialog;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.utils.Constant;

/**
 * Created by Minh Vu on 1/4/2020.
 */
public class ForgotPassActivity extends BaseActivity implements View.OnClickListener {
    private MyApplication app;
    private ImageView backButton;
    private Button buttonSend;
    private EditText  editPhone, editPassword, editRePassword;
    private ImageView buttonFanpage;
    private LinearLayout buttonCall;
    private CountryCodePicker ccp;

    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_forgot_pass;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        backButton = (ImageView) this.findViewById(R.id.back_btn);
        buttonSend = (Button) this.findViewById(R.id.button_reset_pass);

        editPhone = (EditText) this.findViewById(R.id.editPhone);
        editPassword = (EditText) this.findViewById(R.id.edit_pass);
        editRePassword = (EditText) this.findViewById(R.id.edit_repass);
        buttonFanpage = (ImageView) this.findViewById(R.id.button_fanpage);
        buttonCall = (LinearLayout) this.findViewById(R.id.button_call);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        mAuth = FirebaseAuth.getInstance();
        app = (MyApplication) getApplication();
        sharedPreferences = app.getSharedPreferences();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        Constant.increaseHitArea(backButton);
        backButton.setOnClickListener(this);
        buttonSend.setOnClickListener(this);
        buttonFanpage.setOnClickListener(this);
        buttonCall.setOnClickListener(this);
        ccp.registerCarrierNumberEditText(editPhone);
        mAuth = FirebaseAuth.getInstance();    }

    private void makeCall(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ForgotPassActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            startActivity(intent);
        }
    }
    private boolean checkInfo() {

        String phone = editPhone.getText().toString();
        String password = editPassword.getText().toString();
        String repassword = editRePassword.getText().toString();


        if(phone.isEmpty()) {
            Toast.makeText(ForgotPassActivity.this,"Vui lòng nhập số điện thoạt",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.isEmpty()) {
            Toast.makeText(ForgotPassActivity.this,"Vui lòng nhập mật khẩu",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(phone.isEmpty()) {
            Toast.makeText(ForgotPassActivity.this,"Vui lòng nhập lại mật khẩu",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.equals(repassword)) {
            Toast.makeText(ForgotPassActivity.this,"Nhập lại mật khẩu không chính xác",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.length() < 9) {
            Toast.makeText(ForgotPassActivity.this,"Số điện thoại không hợp lệ",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn: {
                finish();
                break;
            }
            case R.id.button_reset_pass: {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constant.USER);

                userRef.child(ccp.getFullNumberWithPlus())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.exists()) {
                                    Toast.makeText(ForgotPassActivity.this,"Số điện thoại này chưa đăng ký",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    User user = new User();
                                    user.setPassword(editPassword.getText().toString());
                                    user.setMobile(ccp.getFullNumberWithPlus());
                                    Intent intent = new Intent(ForgotPassActivity.this,VerifyPhoneActivity.class);
                                    intent.putExtra("oldUser",user);
                                    startActivity(intent);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                break;
            }
            case R.id.button_fanpage:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/V%C3%AD-%C4%91i%E1%BB%87n-t%E1%BB%AD-VIZA-115033150195445/")));
                break;
            case R.id.button_call:
                PhoneNumberDialog dialog = new PhoneNumberDialog(this, new PhoneNumberDialog.OnNumberClickListener() {
                    @Override
                    public void onNumber1Click() {
                        makeCall("0949762975");
                    }

                    @Override
                    public void onNumber2Click() {
                        makeCall("0333319978");
                    }
                });
                dialog.show();
                break;
        }
    }
}



