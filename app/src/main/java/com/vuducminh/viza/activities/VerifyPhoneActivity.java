package com.vuducminh.viza.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.utils.Constant;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends BaseActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private User user;
    private SharedPreferences sharedPreferences;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_verify_phone;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        getUserData();

        MyApplication app = (MyApplication) getApplication();
        sharedPreferences = app.getSharedPreferences();

        String phonenumber = user.getMobile();
        sendVerificationCode(phonenumber);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Nhập mã xác nhận");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if(getUserData() == 1) {
                                Intent intent = new Intent(VerifyPhoneActivity.this, ProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("newUser",user);
//                                sharedPreferences.edit().putString(Constant.USER_INFO, user.toString()).apply();
//                                sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, true).apply();
//                                FirebaseDatabase.getInstance().getReference(Constant.USER)
//                                        .child(user.getMobile())
//                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        progressBar.setVisibility(View.GONE);
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(VerifyPhoneActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
//                                        } else {
//                                            //display a failure message
//                                        }
//                                    }
//                                });
                                startActivity(intent);
                            }
                            else if( getUserData() == 2) {
                                Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                updatePass();
                                sharedPreferences.edit().putString(Constant.USER_INFO, user.toString()).apply();
                                sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, true).apply();
                                startActivity(intent);

                                finish();
                                Intent i = new Intent(Constant.LOGIN_SUCCESS);
                                sendBroadcast(i);
                            }
                            else {
                                Toast.makeText(VerifyPhoneActivity.this, "Lỗi lấy dữ liệu", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private int getUserData() {
        if(getIntent().getSerializableExtra("newUser") != null) {
            user = (User) getIntent().getSerializableExtra("newUser");
            return 1;
        }
        else if(getIntent().getSerializableExtra("oldUser") != null) {
            user = (User) getIntent().getSerializableExtra("oldUser");
            return 2;
        }
        else return 0;
    }

    public void updatePass() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constant.CUSTOMER);

        userRef.child(user.getMobile()).child(Constant.USER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            Toast.makeText(VerifyPhoneActivity.this,"Số điện thoại này chưa đăng ký",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            User userOrg = dataSnapshot.getValue(User.class);
                            userOrg.setPassword(user.getPassword());

                            sharedPreferences.edit().putString(Constant.USER_INFO, userOrg.toString()).apply();
                            sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, true).apply();

                            Toast.makeText(VerifyPhoneActivity.this,"Đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();

                            finish();
                            Intent i = new Intent(Constant.LOGIN_SUCCESS);
                            sendBroadcast(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
