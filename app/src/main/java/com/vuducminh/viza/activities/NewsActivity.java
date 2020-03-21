package com.vuducminh.viza.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.adapters.ListNewsAllAdapter;
import com.vuducminh.viza.models.NewsObject;
import com.vuducminh.viza.models.NewsRequest;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;
import com.vuducminh.viza.utils.SimpleDividerItemDecoration;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class NewsActivity extends BaseActivity implements View.OnClickListener {
    private Gson mGson;
    private Retrofit mRetrofit;
    private IRetrofitAPI mRetrofitAPI;

    private ImageView backButton;
    private RecyclerView listNewsView;
    private ListNewsAllAdapter listNewsAdapter;
    private ArrayList<NewsObject> listNews;

    private Call<NewsRequest> getNewsAPI;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_news;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mGson = MyApplication.getGson();
        mRetrofit = MyApplication.getRetrofit();
        mRetrofitAPI = mRetrofit.create(IRetrofitAPI.class);

        backButton = (ImageView) findViewById(R.id.back_btn);
        listNewsView = (RecyclerView) findViewById(R.id.list_news);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        listNews = new ArrayList<>();
        getListNews();

        listNewsAdapter = new ListNewsAllAdapter(this, listNews, new ListNewsAllAdapter.PositionClickListener() {
            @Override
            public void itemClicked(int position) {
                Intent i = new Intent(NewsActivity.this, DetailActivity.class);
                i.putExtra("title", listNews.get(position).getTitle());
                i.putExtra("url", listNews.get(position).getUrl());
                startActivity(i);
            }
        });
        listNewsAdapter.setHasStableIds(true);

        listNewsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listNewsView.addItemDecoration(new SimpleDividerItemDecoration(this));
        listNewsView.setAdapter(listNewsAdapter);

        Constant.increaseHitArea(backButton);

        backButton.setOnClickListener(this);
    }

    private void getListNews() {
        getNewsAPI = mRetrofitAPI.getArticle();
        getNewsAPI.enqueue(new Callback<NewsRequest>() {
            @Override
            public void onResponse(Call<NewsRequest> call, Response<NewsRequest> response) {
                int errorCode = response.body().getErrorCode();
                String msg = response.body().getMsg();

                if (errorCode == 1) {
                    listNews.addAll(response.body().getData());
                    listNewsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(NewsActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsRequest> call, Throwable t) {
                Toast.makeText(NewsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
