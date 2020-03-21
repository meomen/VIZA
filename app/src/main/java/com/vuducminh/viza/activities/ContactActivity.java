package com.vuducminh.viza.activities;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.models.PageRequest;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ContactActivity extends BaseActivity implements View.OnClickListener {
    private Gson mGson;
    private Retrofit mRetrofit;
    private IRetrofitAPI mRetrofitAPI;

    private ImageView backButton;
    private TextView textContent;

    private Call<PageRequest> getContentAPI;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_contact;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mGson = MyApplication.getGson();
        mRetrofit = MyApplication.getRetrofit();
        mRetrofitAPI = mRetrofit.create(IRetrofitAPI.class);

        backButton = (ImageView) findViewById(R.id.back_btn);
        textContent = (TextView) findViewById(R.id.text_content);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getContent();

        Constant.increaseHitArea(backButton);
        backButton.setOnClickListener(this);
    }

    private void getContent() {
        HashMap<String, Object> body = new HashMap<>();
        body.put("pagename", "lienhe");

        getContentAPI = mRetrofitAPI.getContentByName(body);
        getContentAPI.enqueue(new Callback<PageRequest>() {
            @Override
            public void onResponse(Call<PageRequest> call, Response<PageRequest> response) {
                int errorCode = response.body().getErrorCode();
                String msg = response.body().getMsg();

                if (errorCode == 1) {
                    Spanned sp = Html.fromHtml(response.body().getData().getFulldescription());
                    textContent.setText(sp);
                } else {
                    Toast.makeText(ContactActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PageRequest> call, Throwable t) {
                Toast.makeText(ContactActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
