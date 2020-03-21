package com.vuducminh.viza.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.dialogs.CheckODPDialog;
import com.vuducminh.viza.dialogs.LoadingDialog;
import com.vuducminh.viza.dialogs.PhoneNumberDialog;
import com.vuducminh.viza.models.OtherRequest;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.models.UserRequest;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private MyApplication app;
    private Gson mGson;
    private Retrofit mRetrofit;
    private IRetrofitAPI mRetrofitAPI;
    private SharedPreferences sharedPreferences;

    private EditText editUsername;
    private EditText editPass;
    private Button loginButton;
    private TextView forgotPassButton;
    private TextView registerButton;
    private ImageView buttonFanpage;
    private LinearLayout buttonCall;
    private LoadingDialog loadingDialog;

    private Call<OtherRequest> checkOdpAPI;
    private Call<OtherRequest> resendOdpAPI;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        app = (MyApplication) getApplication();
        mGson = app.getGson();
        mRetrofit = app.getRetrofit();
        mRetrofitAPI = mRetrofit.create(IRetrofitAPI.class);
        sharedPreferences = app.getSharedPreferences();

        loadingDialog = new LoadingDialog(this);

        editUsername = (EditText) findViewById(R.id.edit_username);
        editPass = (EditText) findViewById(R.id.edit_pass);
        loginButton = (Button) findViewById(R.id.login);
        forgotPassButton = (TextView) findViewById(R.id.forgot_pass_button);
        registerButton = (TextView) findViewById(R.id.register_button);
        buttonFanpage = (ImageView) findViewById(R.id.button_fanpage);
        buttonCall = (LinearLayout) findViewById(R.id.button_call);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        loginButton.setOnClickListener(this);
        forgotPassButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        buttonFanpage.setOnClickListener(this);
        buttonCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                HashMap<String, Object> body = new HashMap<>();
                body.put("username", editUsername.getText().toString());
                body.put("password", editPass.getText().toString());
                body.put("deviceID", Constant.getDeviceId(this));

                loadingDialog.show();
                Call<UserRequest> callLogin = mRetrofitAPI.login(body);
                callLogin.enqueue(new Callback<UserRequest>() {
                    @Override
                    public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {
                        int errorCode = response.body().getErrorCode();
                        String msg = response.body().getMsg();
                        loadingDialog.dismiss();
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

                        if (errorCode == 1) {
                            User user = response.body().getData();
                            String jsonUser = mGson.toJson(user);
                            sharedPreferences.edit().putString(Constant.USER_INFO, jsonUser).apply();
                            sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, true).apply();

                            finish();
                            Intent i = new Intent(Constant.LOGIN_SUCCESS);
                            sendBroadcast(i);
                        } else if (errorCode == 4) {
                            final User user = response.body().getData();

                            CheckODPDialog dialog = new CheckODPDialog(LoginActivity.this, new CheckODPDialog.OnButtonClickListener() {
                                @Override
                                public void onResendClick() {
                                    final LoadingDialog dialog1 = new LoadingDialog(LoginActivity.this);
                                    dialog1.show();

                                    resendOdpAPI = mRetrofitAPI.resendODP(user.getToken());
                                    resendOdpAPI.enqueue(new Callback<OtherRequest>() {
                                        @Override
                                        public void onResponse(Call<OtherRequest> call, Response<OtherRequest> response) {
                                            int errorCode = response.body().getErrorCode();
                                            String msg = response.body().getMsg();
                                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

                                            if (errorCode == -2) {
                                                sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
                                                sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
                                                Constant.restartApp(LoginActivity.this);
                                            }

                                            dialog1.dismiss();
                                        }

                                        @Override
                                        public void onFailure(Call<OtherRequest> call, Throwable t) {
                                            Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            dialog1.dismiss();
                                        }
                                    });
                                }

                                @Override
                                public void onAcceptClick(String odp) {
                                    HashMap<String, Object> body = new HashMap<>();
                                    body.put("ODP", odp);

                                    final LoadingDialog dialog1 = new LoadingDialog(LoginActivity.this);
                                    dialog1.show();

                                    checkOdpAPI = mRetrofitAPI.checkODP(user.getToken(), body);
                                    checkOdpAPI.enqueue(new Callback<OtherRequest>() {
                                        @Override
                                        public void onResponse(Call<OtherRequest> call, Response<OtherRequest> response) {
                                            int errorCode = response.body().getErrorCode();
                                            String msg = response.body().getMsg();
                                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            dialog1.dismiss();

                                            if (errorCode == 1) {
                                                String jsonUser = mGson.toJson(user);
                                                sharedPreferences.edit().putString(Constant.USER_INFO, jsonUser).apply();
                                                sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, true).apply();

                                                finish();
                                                Intent i = new Intent(Constant.LOGIN_SUCCESS);
                                                sendBroadcast(i);
                                            } else if (errorCode == -2) {
                                                sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
                                                sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
                                                Constant.restartApp(LoginActivity.this);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<OtherRequest> call, Throwable t) {
                                            Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            dialog1.dismiss();
                                        }
                                    });
                                }
                            });
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRequest> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });
                break;
            case R.id.forgot_pass_button:
                startActivity(ForgotPassActivity.class);
                break;
            case R.id.register_button:
                startActivity(RegisterActivity.class);
                break;
            case R.id.button_fanpage:
                try {
                    getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/Thecaosieure")));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Thecaosieure")));
                }
                break;
            case R.id.button_call:
                PhoneNumberDialog dialog = new PhoneNumberDialog(this, new PhoneNumberDialog.OnNumberClickListener() {
                    @Override
                    public void onNumber1Click() {
                        makeCall("0963986398");
                    }

                    @Override
                    public void onNumber2Click() {
                        makeCall("0979943129");
                    }
                });
                dialog.show();
                break;
        }
    }

    private void makeCall(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this,
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

                    Toast.makeText(LoginActivity.this, "Cấp quyền thành công!", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(LoginActivity.this, "Quyền bị từ chối!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
