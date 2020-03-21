package com.vuducminh.viza.fragments.personalfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.dialogs.ErrorDialog;
import com.vuducminh.viza.dialogs.LoadingDialog;
import com.vuducminh.viza.dialogs.SuccessDialog;
import com.vuducminh.viza.fragments.BaseFragment;
import com.vuducminh.viza.models.OtherRequest;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PasswordFragment extends BaseFragment implements View.OnClickListener {
    private MyApplication app;
    private Gson mGson;
    private Retrofit mRetrofit;
    private IRetrofitAPI mRetrofitAPI;
    private SharedPreferences sharedPreferences;
    private User user;

    private RadioButton pass1;
    private RadioButton pass2;
    private EditText currentPass;
    private EditText newPass;
    private EditText retypeNewPass;
    private Button changePassButton;
    private LoadingDialog loadingDialog;

    public static PasswordFragment newInstance() {

        Bundle args = new Bundle();

        PasswordFragment fragment = new PasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_password;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState, View rootView) {
        app = (MyApplication) getActivity().getApplication();
        mGson = app.getGson();
        mRetrofit = app.getRetrofit();
        mRetrofitAPI = mRetrofit.create(IRetrofitAPI.class);
        sharedPreferences = app.getSharedPreferences();
        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);

        loadingDialog = new LoadingDialog(getActivity());

        pass1 = (RadioButton) rootView.findViewById(R.id.pass_1);
        pass2 = (RadioButton) rootView.findViewById(R.id.pass_2);
        currentPass = (EditText) rootView.findViewById(R.id.current_pass);
        newPass = (EditText) rootView.findViewById(R.id.new_pass);
        retypeNewPass = (EditText) rootView.findViewById(R.id.retype_new_pass);
        changePassButton = (Button) rootView.findViewById(R.id.change_pass_button);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        changePassButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_pass_button:
                if (currentPass.getText().toString().equals("") || newPass.getText().toString().equals("")
                        || retypeNewPass.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    if ((currentPass.getText().toString().length() < 6 || currentPass.getText().toString().length() > 24)
                            || (newPass.getText().toString().length() < 6 || newPass.getText().toString().length() > 24)
                            || (retypeNewPass.getText().toString().length() < 6 || retypeNewPass.getText().toString().length() > 24)) {
                        ErrorDialog dialog = new ErrorDialog(getActivity(), "Mật khẩu phải từ 6 đến 24 ký tự bao gồm chữ và số");
                        dialog.show();
                    } else {
                        if (newPass.getText().toString().equals(retypeNewPass.getText().toString())) {
                            loadingDialog.show();
                            if (pass1.isChecked()) {
                                HashMap<String, Object> body = new HashMap<>();
                                body.put("old_mk1", currentPass.getText().toString());
                                body.put("new_mk1", newPass.getText().toString());

                                Call<OtherRequest> changePass1 = mRetrofitAPI.changePass1(user.getToken(), body);
                                changePass1.enqueue(new Callback<OtherRequest>() {
                                    @Override
                                    public void onResponse(Call<OtherRequest> call, Response<OtherRequest> response) {
                                        int errorCode = response.body().getErrorCode();
                                        String msg = response.body().getMsg();
                                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();

                                        if (errorCode == 1) {
                                            SuccessDialog dialog = new SuccessDialog(getActivity(), "Chúc mừng bạn đã đổi mật khẩu thành công");
                                            dialog.show();

                                            currentPass.setText("");
                                            newPass.setText("");
                                            retypeNewPass.setText("");
                                        } else if (errorCode == 0) {
                                            ErrorDialog dialog = new ErrorDialog(getActivity(), "Mật khẩu cấp 1 không chính xác\nVui lòng nhập lại");
                                            dialog.show();
                                        } else {
                                            if (errorCode == -2) {
                                                sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
                                                sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
                                                Constant.restartApp(getActivity());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<OtherRequest> call, Throwable t) {
                                        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }
                                });
                            } else {
                                HashMap<String, Object> body = new HashMap<>();
                                body.put("old_mk2", currentPass.getText().toString());
                                body.put("new_mk2", newPass.getText().toString());

                                Call<OtherRequest> changePass2 = mRetrofitAPI.changePass2(user.getToken(), body);
                                changePass2.enqueue(new Callback<OtherRequest>() {
                                    @Override
                                    public void onResponse(Call<OtherRequest> call, Response<OtherRequest> response) {
                                        int errorCode = response.body().getErrorCode();
                                        String msg = response.body().getMsg();
                                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();

                                        if (errorCode == 1) {
                                            SuccessDialog dialog = new SuccessDialog(getActivity(), "Chúc mừng bạn đã đổi mật khẩu thành công");
                                            dialog.show();

                                            currentPass.setText("");
                                            newPass.setText("");
                                            retypeNewPass.setText("");
                                        } else if (errorCode == 0) {
                                            ErrorDialog dialog = new ErrorDialog(getActivity(), "Mật khẩu cấp 2 không chính xác\nVui lòng nhập lại");
                                            dialog.show();
                                        } else {
                                            if (errorCode == -2) {
                                                sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
                                                sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
                                                Constant.restartApp(getActivity());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<OtherRequest> call, Throwable t) {
                                        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }
                                });
                            }

                        } else {
                            ErrorDialog dialog = new ErrorDialog(getActivity(), "Mật khẩu mới và nhập lại mật khẩu không khớp nhau");
                            dialog.show();
                        }
                    }
                }
                break;
        }
    }
}
