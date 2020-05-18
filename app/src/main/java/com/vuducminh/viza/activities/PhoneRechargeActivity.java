package com.vuducminh.viza.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vuducminh.viza.R;
import com.vuducminh.viza.adapters.TabsPagerAdapter;
import com.vuducminh.viza.fragments.phonerechargefragments.PostpaidFragment;
import com.vuducminh.viza.fragments.phonerechargefragments.PrepayFragment;
import com.vuducminh.viza.utils.Constant;
import java.util.ArrayList;



public class PhoneRechargeActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backButton;
    private TabLayout tabs;
    private ViewPager pager;
    private TabsPagerAdapter adapter;
    private ArrayList<Fragment> listFragment;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_phone_recharge;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        backButton = (ImageView) findViewById(R.id.back_btn);
        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        listFragment = new ArrayList<>();
        listFragment.add(PrepayFragment.newInstance());
        listFragment.add(PostpaidFragment.newInstance());

        adapter = new TabsPagerAdapter(getSupportFragmentManager(), listFragment);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);

        tabs.setupWithViewPager(pager);

        TabLayout.Tab tab1 = tabs.getTabAt(0);
        tab1.setCustomView(createTabView(getResources().getString(R.string.prepay)));
        TabLayout.Tab tab2 = tabs.getTabAt(1);
        tab2.setCustomView(createTabView(getResources().getString(R.string.postpaid)));

        Constant.increaseHitArea(backButton);

        backButton.setOnClickListener(this);
    }

    private View createTabView(String title) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.tabs_personal_layout, null);

        TextView tabsTitle = (TextView) itemView.findViewById(R.id.tab_title);
        tabsTitle.setText(title);
        tabsTitle.setTextColor(getResources().getColorStateList(R.color.text_tab_indicator));

        return itemView;
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
