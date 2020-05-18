package com.vuducminh.viza.fragments.mainfragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.activities.DepositActivity;
import com.vuducminh.viza.activities.DetailActivity;
import com.vuducminh.viza.activities.IntroActivity;
import com.vuducminh.viza.activities.NewsActivity;
import com.vuducminh.viza.activities.WithdrawActivity;
import com.vuducminh.viza.adapters.HomePagerAdapter;
import com.vuducminh.viza.adapters.ListFunctionAdapter;
import com.vuducminh.viza.adapters.ListNewsHomeAdapter;
import com.vuducminh.viza.fragments.BaseFragment;
import com.vuducminh.viza.models.BannerObject;
import com.vuducminh.viza.models.BannerRequest;
import com.vuducminh.viza.models.MenuObject;
import com.vuducminh.viza.models.NewsObject;
import com.vuducminh.viza.models.NewsRequest;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private Gson mGson;
    private Retrofit mRetrofit;
    private IRetrofitAPI mRetrofitAPI;
    private SharedPreferences sharedPreferences;

    private ViewPager pager;
    private HomePagerAdapter pagerAdapter;
    private ArrayList<BannerObject> listImg;
    private ImageView[] mDots;
    private LinearLayout mLinearLayout;
    private RecyclerView listFunctionView;
    private ListFunctionAdapter listFunctionAdapter;
    private ArrayList<MenuObject> listFunction;
    private RecyclerView listNewsView;
    private ListNewsHomeAdapter listNewsAdapter;
    private ArrayList<NewsObject> listNews;
    private RelativeLayout newsLayout;

    private Call<BannerRequest> getBannerAPI;
    private Call<NewsRequest> getNewsAPI;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState, View rootView) {
        mGson = MyApplication.getGson();
        mRetrofit = MyApplication.getRetrofit();
        mRetrofitAPI = mRetrofit.create(IRetrofitAPI.class);
        sharedPreferences = MyApplication.getSharedPreferences();

        pager = (ViewPager) rootView.findViewById(R.id.pager);
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.viewPagerCountDots);
        listFunctionView = (RecyclerView) rootView.findViewById(R.id.list_function);
        listNewsView = (RecyclerView) rootView.findViewById(R.id.list_news);
        newsLayout = (RelativeLayout) rootView.findViewById(R.id.news_layout);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //Create slide image
        listImg = new ArrayList<>();
        getListBanner();

        pagerAdapter = new HomePagerAdapter(getActivity(), listImg);
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                drawPageSelectionIndicators(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Create list function
        listFunction = new ArrayList<>();
        listFunction.add(new MenuObject(R.drawable.ic_about_state, getActivity().getResources().getString(R.string.gioi_thieu)));
        listFunction.add(new MenuObject(R.drawable.ic_deposit_state, getActivity().getResources().getString(R.string.nap_tien)));
        listFunction.add(new MenuObject(R.drawable.ic_withdraw_state, getActivity().getResources().getString(R.string.rut_tien)));
        listFunction.add(new MenuObject(R.drawable.ic_transfer_money_state, getActivity().getResources().getString(R.string.chuyen_tien)));
        listFunction.add(new MenuObject(R.mipmap.ic_service, getActivity().getResources().getString(R.string.dich_vu)));

        listFunctionAdapter = new ListFunctionAdapter(getActivity(), listFunction, R.layout.item_function_main, new ListFunctionAdapter.PositionClickListener() {
            @Override
            public void itemClicked(int position) {
                switch (position) {
                    case 0:
                        startActivity(IntroActivity.class);
                        break;
                    case 1:
                        startActivity(DepositActivity.class);
                        break;
                    case 2:
                        startActivity(WithdrawActivity.class);
                        break;
                    case 3:
                        Intent i = new Intent(Constant.GOTO_TRANSFER);
                        getActivity().sendBroadcast(i);
                        break;
                    case 4:
                        Intent i1 = new Intent(Constant.GOTO_PAYMENT);
                        getActivity().sendBroadcast(i1);
                        break;
                }
            }
        });
        listFunctionAdapter.setHasStableIds(true);

        listFunctionView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listFunctionView.setAdapter(listFunctionAdapter);

        //Create list news
        listNews = new ArrayList<>();
        getListNews();

        listNewsAdapter = new ListNewsHomeAdapter(getActivity(), listNews, new ListNewsHomeAdapter.PositionClickListener() {
            @Override
            public void itemClicked(int position) {
                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra("title", listNews.get(position).getTitle());
                i.putExtra("url", listNews.get(position).getUrl());
                startActivity(i);
            }
        });
        listNewsAdapter.setHasStableIds(true);

        listNewsView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listNewsView.setAdapter(listNewsAdapter);

        newsLayout.setOnClickListener(this);
    }

    private void getListBanner() {
        getBannerAPI = mRetrofitAPI.getBanner();
        getBannerAPI.enqueue(new Callback<BannerRequest>() {
            @Override
            public void onResponse(Call<BannerRequest> call, Response<BannerRequest> response) {
                int errorCode = response.body().getErrorCode();
                String msg = response.body().getMsg();

                if (errorCode == 1) {
                    listImg.addAll(response.body().getData());
                    pagerAdapter.notifyDataSetChanged();

                    drawPageSelectionIndicators(0);
                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BannerRequest> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsRequest> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawPageSelectionIndicators(int mPosition){
        int margin = Constant.convertDpIntoPixels(2, getActivity());

        if(mLinearLayout!=null) {
            mLinearLayout.removeAllViews();
        }

        mDots = new ImageView[listImg.size()];

        //set image with orange circle if mDots[i] == mPosition
        for (int i = 0; i < listImg.size(); i++) {
            mDots[i] = new ImageView(getActivity());
            if(i == mPosition)
                mDots[i].setImageDrawable(getResources().getDrawable(R.mipmap.ic_circle_selected));
            else
                mDots[i].setImageDrawable(getResources().getDrawable(R.mipmap.ic_circle_unselected));

            //set layout for circle indicators
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 4, 0);
            mDots[i].setPadding(margin, 0, margin, 0);
            mLinearLayout.addView(mDots[i], params);
        }
    }

    @Override
    public void onClick(View v) {
        startActivity(NewsActivity.class);
    }
}
