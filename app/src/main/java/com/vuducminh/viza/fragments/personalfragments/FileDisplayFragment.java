package com.vuducminh.viza.fragments.personalfragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.fragments.BaseFragment;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.models.UserRequest;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FileDisplayFragment extends BaseFragment implements View.OnClickListener {
    private MyApplication app;
    private Gson mGson;
    private Retrofit mRetrofit;
    private IRetrofitAPI mRetrofitAPI;
    private SharedPreferences sharedPreferences;
    private User user;

    private TextView fullName;
    private TextView mobile;
    private TextView email;
    private TextView identityNumber;
    private TextView dateBirth;
    private TextView sex;
    private TextView address;
    private TextView updateButton;
    private BroadcastReceiver updateProfileReceiver;

    private Call<UserRequest> getUserInfoAPI;

    public static FileDisplayFragment newInstance() {

        Bundle args = new Bundle();

        FileDisplayFragment fragment = new FileDisplayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_file_display;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState, View rootView) {
        app = (MyApplication) getActivity().getApplication();
        mGson = app.getGson();
        mRetrofit = app.getRetrofit();
        mRetrofitAPI = mRetrofit.create(IRetrofitAPI.class);
        sharedPreferences = app.getSharedPreferences();
        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);

        fullName = (TextView) rootView.findViewById(R.id.full_name);
        mobile = (TextView) rootView.findViewById(R.id.mobile);
        email = (TextView) rootView.findViewById(R.id.email);
        identityNumber = (TextView) rootView.findViewById(R.id.identity_number);
        dateBirth = (TextView) rootView.findViewById(R.id.date_birth);
        sex = (TextView) rootView.findViewById(R.id.sex);
        address = (TextView) rootView.findViewById(R.id.address);
        updateButton = (TextView) rootView.findViewById(R.id.update_button);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (user != null) {
            fullName.setText(user.getFullname());
            mobile.setText(user.getMobile());
            email.setText(user.getEmail());
            identityNumber.setText(user.getIdentityNumber());
            dateBirth.setText(user.getDateBirth());
            if (user.getSex().equals("MALE")) {
                sex.setText("Nam");
            } else {
                sex.setText("Nữ");
            }
            address.setText(user.getAddress());
        }

        updateProfileReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getUserInfo();
            }
        };
        getActivity().registerReceiver(updateProfileReceiver, new IntentFilter(Constant.UPDATE_PROFILE));

        updateButton.setOnClickListener(this);
    }

    private void getUserInfo() {
        getUserInfoAPI = mRetrofitAPI.getUserInfo(user.getToken());
        getUserInfoAPI.enqueue(new Callback<UserRequest>() {
            @Override
            public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {
                int errorCode = response.body().getErrorCode();
                String msg = response.body().getMsg();

                if (errorCode == 1) {
                    User myUser = response.body().getData();
                    fullName.setText(myUser.getFullname());
                    mobile.setText(myUser.getMobile());
                    email.setText(myUser.getEmail());
                    identityNumber.setText(myUser.getIdentityNumber());
                    dateBirth.setText(myUser.getDateBirth());
                    if (user.getSex().equals("MALE")) {
                        sex.setText("Nam");
                    } else {
                        sex.setText("Nữ");
                    }
                    address.setText(myUser.getAddress());

                    myUser.setToken(user.getToken());

                    String jsonUser = mGson.toJson(myUser);
                    sharedPreferences.edit().putString(Constant.USER_INFO, jsonUser).apply();
                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                    if (errorCode == -2) {
                        sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
                        sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
                        Constant.restartApp(getActivity());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRequest> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(Constant.CHANGE_FILE_FRAGMENT);
        i.putExtra("command", 0);
        getActivity().sendBroadcast(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateProfileReceiver != null) {
            getActivity().unregisterReceiver(updateProfileReceiver);
        }
    }
}
