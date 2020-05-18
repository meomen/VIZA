package com.vuducminh.viza.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.vuducminh.viza.R;
import com.vuducminh.viza.dialogs.PhoneNumberDialog;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.utils.Constant;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView backButton;
    private Button buttonSend;
    private EditText editUseName, editPhone, editPassword, editRePassword;
    private ImageView buttonFanpage;
    private LinearLayout buttonCall;
    private DatabaseReference userRef;
    private CountryCodePicker ccp;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        backButton = (ImageView) this.findViewById(R.id.back_btn);
        buttonSend = (Button) this.findViewById(R.id.button_register);

        editUseName = (EditText) this.findViewById(R.id.edit_username);
        editPhone = (EditText) this.findViewById(R.id.editPhone);
        editPassword = (EditText) this.findViewById(R.id.edit_pass);
        editRePassword = (EditText) this.findViewById(R.id.edit_repass);
        buttonFanpage = (ImageView) this.findViewById(R.id.button_fanpage);
        buttonCall = (LinearLayout) this.findViewById(R.id.button_call);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Constant.increaseHitArea(backButton);
        backButton.setOnClickListener(this);
        buttonSend.setOnClickListener(this);
        buttonFanpage.setOnClickListener(this);
        buttonCall.setOnClickListener(this);
        ccp.registerCarrierNumberEditText(editPhone);
        userRef = FirebaseDatabase.getInstance().getReference(Constant.USER);
    }

    private void makeCall(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            startActivity(intent);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Toast.makeText(RegisterActivity.this, "Cấp quyền thành công!", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(RegisterActivity.this, "Quyền bị từ chối!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private boolean checkInfo() {
        String name = editUseName.getText().toString();
        String phone = editPhone.getText().toString();
        String password = editPassword.getText().toString();
        String repassword = editRePassword.getText().toString();

        if(name.isEmpty()) {
            Toast.makeText(RegisterActivity.this,"Vui lòng nhập tên tài khoản",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(phone.isEmpty()) {
            Toast.makeText(RegisterActivity.this,"Vui lòng nhập số điện thoạt",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.isEmpty()) {
            Toast.makeText(RegisterActivity.this,"Vui lòng nhập mật khẩu",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(phone.isEmpty()) {
            Toast.makeText(RegisterActivity.this,"Vui lòng nhập lại mật khẩu",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.equals(repassword)) {
            Toast.makeText(RegisterActivity.this,"Nhập lại mật khẩu không chính xác",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length() < 6) {
            Toast.makeText(RegisterActivity.this,"Mật khẩu cần ít nhất 6 ký tự",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.length() < 9) {
            Toast.makeText(RegisterActivity.this,"Số điện thoại không hợp lệ",Toast.LENGTH_SHORT).show();
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
            case R.id.button_register: {
                if(!checkInfo()) break;

                userRef.child(ccp.getFullNumberWithPlus())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    Toast.makeText(RegisterActivity.this,"Số điện thoại này đã được đăng ký",Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    User user = new User();
                                    user.setUsername(editUseName.getText().toString());
                                    user.setPassword(editPassword.getText().toString());
                                    user.setMobile(ccp.getFullNumberWithPlus());
                                    Intent intent = new Intent(RegisterActivity.this,VerifyPhoneActivity.class);
                                    intent.putExtra("newUser",user);
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
//    private ImageView backButton;
//    private Button buttonSend;
//    private TextView dk1, dk2;
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_register;
//    }
//
//    @Override
//    protected void initVariables(Bundle savedInstanceState) {
//        backButton = (ImageView) findViewById(R.id.back_btn);
//        buttonSend = (Button) findViewById(R.id.button_send);
//        dk1 = (TextView) findViewById(R.id.text_dk1);
//        dk2 = (TextView) findViewById(R.id.text_dk2);
//    }
//
//    @Override
//    protected void initData(Bundle savedInstanceState) {
//        Spanned sp1 = Html.fromHtml(getResources().getText(R.string.cu_phap_1).toString());
//        dk1.setText(sp1);
//
//        Spanned sp2 = Html.fromHtml(getResources().getText(R.string.dang_ky_2).toString());
//        dk2.setText(sp2);
//
//        Constant.increaseHitArea(backButton);
//        backButton.setOnClickListener(this);
//        buttonSend.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.back_btn:
//                finish();
//                break;
//            case R.id.button_send:
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "8298"));
//                intent.putExtra("sms_body", "TCSR DK TK");
//                startActivity(intent);
//                break;
//        }
//    }

