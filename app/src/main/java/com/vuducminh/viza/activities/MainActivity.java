package com.vuducminh.viza.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.adapters.ListMenuAdapter;
import com.vuducminh.viza.adapters.ListNotiAdapter;
import com.vuducminh.viza.adapters.TabsPagerAdapter;
import com.vuducminh.viza.dialogs.LoadingDialog;
import com.vuducminh.viza.fragments.mainfragments.HomeFragment;
import com.vuducminh.viza.fragments.mainfragments.PaymentFragment;
import com.vuducminh.viza.fragments.mainfragments.TransferFragment;
import com.vuducminh.viza.fragments.mainfragments.WalletFragment;
import com.vuducminh.viza.models.BalanceRequest;
import com.vuducminh.viza.models.MenuObject;
import com.vuducminh.viza.models.OtherRequest;
import com.vuducminh.viza.models.TransactionObject;
import com.vuducminh.viza.models.TransactionRequest;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.retrofit.IRetrofitAPI;
import com.vuducminh.viza.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private MyApplication app;
    private Gson mGson;
    private SharedPreferences sharedPreferences;
    private User user;
    private boolean isLogin;

    private DrawerLayout drawer;
    private NavigationView navRight;
    private RelativeLayout accountLayout;
    private TextView loginText;
    private LinearLayout infoLayout;
    private TextView balance;
    private TextView infoText;
    private LinearLayout footLayout;
    private RelativeLayout loginButton;
    private RelativeLayout registerButton;
    private ListView listMenuView;
    private ListMenuAdapter listMenuAdapter;
    private ListView listNotiView;
    private ListNotiAdapter listNotiAdapter;
    private ArrayList<TransactionObject> listTrans;
    private ArrayList<MenuObject> listMenu;
    private RelativeLayout menuButton;
    private ImageView notiButton;
    private RelativeLayout searchLayout;
    private EditText editSearch;
    private LinearLayout textSearch;
    private String searchContent = "";
    private Handler handler = new Handler();
    private long delay = 1000;
    private long last_text_edit = 0;

    private TabLayout tabs;
    private ViewPager pager;
    private TabsPagerAdapter pagerAdapter;
    private ArrayList<Fragment> listFragment;
    private TextView titleText;
    private View shadowView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private boolean doubleBackToExitPressedOnce = false;

    private BroadcastReceiver gotoTransferReceiver, gotoPaymentReceiver, loginSuccessReceiver, updateInfoReceiver;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        app = (MyApplication) getApplication();
        mGson = app.getGson();
        sharedPreferences = app.getSharedPreferences();
        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navRight = (NavigationView) findViewById(R.id.nav_view_right);
        accountLayout = (RelativeLayout) findViewById(R.id.account_layout);
        loginText = (TextView) findViewById(R.id.login_text);
        infoLayout = (LinearLayout) findViewById(R.id.info_layout);
        balance = (TextView) findViewById(R.id.balance);
        infoText = (TextView) findViewById(R.id.info_text);
        footLayout = (LinearLayout) findViewById(R.id.foot_layout);
        loginButton = (RelativeLayout) findViewById(R.id.login_button);
        registerButton = (RelativeLayout) findViewById(R.id.register_button);
        listMenuView = (ListView) findViewById(R.id.list_menu);
        listNotiView = (ListView) findViewById(R.id.list_noti);

        titleText = (TextView) findViewById(R.id.title_text);
        shadowView = this.findViewById(R.id.shadow_view);
        menuButton = (RelativeLayout) findViewById(R.id.menu_button);
        notiButton = (ImageView) findViewById(R.id.noti_button);
        searchLayout = (RelativeLayout) findViewById(R.id.search_layout);
        editSearch = (EditText) findViewById(R.id.edit_search);
        textSearch = (LinearLayout) findViewById(R.id.text_search);

        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        isLogin = sharedPreferences.getBoolean(Constant.IS_LOGIN, false);
        if (isLogin) {
            footLayout.setVisibility(View.GONE);
            loginText.setVisibility(View.GONE);
            infoLayout.setVisibility(View.VISIBLE);

            infoText.setText(user.getUsername() + " - " + user.getMobile());
        } else {
            footLayout.setVisibility(View.VISIBLE);
            loginText.setVisibility(View.VISIBLE);
            infoLayout.setVisibility(View.GONE);
        }

        createMainLayout();
        createDrawerLayout();

        Constant.increaseHitArea(menuButton);
        Constant.increaseHitArea(notiButton);
        menuButton.setOnClickListener(this);
        notiButton.setOnClickListener(this);
        textSearch.setOnClickListener(this);
        accountLayout.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        setupReceiver();
    }

    public void createMainLayout() {
        listFragment = new ArrayList<>();
        listFragment.add(HomeFragment.newInstance());
        listFragment.add(TransferFragment.newInstance());
        listFragment.add(PaymentFragment.newInstance());
        listFragment.add(WalletFragment.newInstance());

        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), listFragment);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(4);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        titleText.setText(getResources().getString(R.string.home_page));
                        shadowView.setVisibility(View.GONE);
                        break;
                    case 1:
                        titleText.setText(getResources().getString(R.string.transfer));
                        shadowView.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        titleText.setText(getResources().getString(R.string.payment));
                        shadowView.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        titleText.setText(getResources().getString(R.string.personal_information));
                        shadowView.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabs.setupWithViewPager(pager);

        TabLayout.Tab tab1 = tabs.getTabAt(0);
        tab1.setCustomView(createTabView(R.drawable.img_home));
        TabLayout.Tab tab2 = tabs.getTabAt(1);
        tab2.setCustomView(createTabView(R.drawable.img_transfer));
        TabLayout.Tab tab3 = tabs.getTabAt(2);
        tab3.setCustomView(createTabView(R.drawable.img_payment));
        TabLayout.Tab tab4 = tabs.getTabAt(3);
        tab4.setCustomView(createTabView(R.drawable.img_wallet));

        LinearLayout tabStrip = ((LinearLayout) tabs.getChildAt(0));
        if (!sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
            tabStrip.setEnabled(false);
            tabStrip.getChildAt(3).setClickable(false);
        } else {
            tabStrip.setEnabled(true);
            tabStrip.getChildAt(3).setClickable(true);
        }

        if (!sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
            tab4.getCustomView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void createDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Create Navigation Drawer layout left
        listMenu = new ArrayList<>();
        listMenu.add(new MenuObject(R.mipmap.ic_home_new, getResources().getString(R.string.home_page)));
        listMenu.add(new MenuObject(R.mipmap.ic_chuyen_tien, getResources().getString(R.string.chuyen_tien)));
        listMenu.add(new MenuObject(R.mipmap.ic_nap_dt, getResources().getString(R.string.nap_dien_thoai)));
        listMenu.add(new MenuObject(R.mipmap.ic_mua_the_dt, getResources().getString(R.string.mua_the_dien_thoai)));
        listMenu.add(new MenuObject(R.mipmap.ic_doi_the_cao, getResources().getString(R.string.doi_the_cao)));
        listMenu.add(new MenuObject(R.mipmap.ic_mua_game, getResources().getString(R.string.mua_the_game)));
        listMenu.add(new MenuObject(R.mipmap.ic_nap_tien, getResources().getString(R.string.nap_tien)));
        listMenu.add(new MenuObject(R.mipmap.ic_rut_tien, getResources().getString(R.string.rut_tien)));
        listMenu.add(new MenuObject(R.mipmap.ic_hoa_don, getResources().getString(R.string.thanh_toan_hoa_don)));
        listMenu.add(new MenuObject(R.mipmap.ic_lien_he, getResources().getString(R.string.lien_he)));
        if (isLogin) {
            listMenu.add(new MenuObject(R.mipmap.ic_logout, getResources().getString(R.string.dang_xuat)));
        }

        listMenuAdapter = new ListMenuAdapter(this, listMenu);
        listMenuView.setAdapter(listMenuAdapter);
        listMenuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        pager.setCurrentItem(0);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        pager.setCurrentItem(1);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        startActivity(PhoneRechargeActivity.class);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        startActivity(BuyPhoneCardActivity.class);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        startActivity(DepositCardActivity.class);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 5:
                        startActivity(BuyGameCardActivity.class);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 6:
                        startActivity(DepositActivity.class);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 7:
                        startActivity(WithdrawActivity.class);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 8:
                        startActivity(TraGopActivity.class);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 9:
                        startActivity(ContactActivity.class);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 10:
                        logout();
                        break;
                }
            }
        });

        // nar bên phải
        // Lấy thông báo

    }

    private void logout() {
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        drawer.closeDrawer(GravityCompat.START);

        sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
        sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();

        Intent i = getIntent();
        finish();
        startActivity(i);
    }

    private void setupReceiver() {
        gotoTransferReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                pager.setCurrentItem(1);
            }
        };
        registerReceiver(gotoTransferReceiver, new IntentFilter(Constant.GOTO_TRANSFER));

        gotoPaymentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                pager.setCurrentItem(2);
            }
        };
        registerReceiver(gotoPaymentReceiver, new IntentFilter(Constant.GOTO_PAYMENT));

        loginSuccessReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent i = getIntent();
                finish();
                startActivity(i);
            }
        };
        registerReceiver(loginSuccessReceiver, new IntentFilter(Constant.LOGIN_SUCCESS));

        // Cập nhận thông báo
 /*
      updateInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getBalance();
                getAllTrans("");
            }
        };
        registerReceiver(updateInfoReceiver, new IntentFilter(Constant.UPDATE_INFO));
  */
    }

    private View createTabView(int imgRes) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.tabs_main_layout, null);

        ImageView tabsIcon = (ImageView) itemView.findViewById(R.id.tabs_icon);
        tabsIcon.setImageResource(imgRes);

        return itemView;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_button:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.noti_button:
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
                break;
            case R.id.text_search:
                textSearch.setVisibility(View.GONE);
                editSearch.setVisibility(View.VISIBLE);
                editSearch.requestFocus();
                break;
            case R.id.account_layout:
                if (isLogin) {
                    pager.setCurrentItem(3);
                } else {
                    startActivity(LoginActivity.class);
                }

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                break;
            case R.id.login_button:
                startActivity(LoginActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.register_button:
                startActivity(RegisterActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Nhấn BACK lần nữa để thoát", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (gotoTransferReceiver != null) {
            unregisterReceiver(gotoTransferReceiver);
        }
        if (gotoPaymentReceiver != null) {
            unregisterReceiver(gotoPaymentReceiver);
        }
        if (loginSuccessReceiver != null) {
            unregisterReceiver(loginSuccessReceiver);
        }
        if (updateInfoReceiver != null) {
            unregisterReceiver(updateInfoReceiver);
        }
    }


//    private MyApplication app;
//    private Gson mGson;
//    private Retrofit mRetrofit;
//    private IRetrofitAPI mRetrofitAPI;
//    private SharedPreferences sharedPreferences;
//    private User user;
//    private boolean isLogin;
//
//    private DrawerLayout drawer;
//    private NavigationView navRight;
//    private RelativeLayout accountLayout;
//    private TextView loginText;
//    private LinearLayout infoLayout;
//    private TextView balance;
//    private TextView infoText;
//    private LinearLayout footLayout;
//    private RelativeLayout loginButton;
//    private RelativeLayout registerButton;
//    private ListView listMenuView;
//    private ListMenuAdapter listMenuAdapter;
//    private ListView listNotiView;
//    private ListNotiAdapter listNotiAdapter;
//    private ArrayList<TransactionObject> listTrans;
//    private ArrayList<MenuObject> listMenu;
//    private RelativeLayout menuButton;
//    private ImageView notiButton;
//    private RelativeLayout searchLayout;
//    private EditText editSearch;
//    private LinearLayout textSearch;
//    private String searchContent = "";
//    private Handler handler = new Handler();
//    private long delay = 1000;
//    private long last_text_edit = 0;
//
//    private TabLayout tabs;
//    private ViewPager pager;
//    private TabsPagerAdapter pagerAdapter;
//    private ArrayList<Fragment> listFragment;
//    private TextView titleText;
//    private View shadowView;
//    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//    private boolean doubleBackToExitPressedOnce = false;
//
//    private BroadcastReceiver gotoTransferReceiver, gotoPaymentReceiver, loginSuccessReceiver, updateInfoReceiver;
//
//    private Call<TransactionRequest> getAllTransAPI;
//    private Call<OtherRequest> logOutAPI;
//    private Call<BalanceRequest> getBalanceAPI;
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_main;
//    }
//
//    @Override
//    protected void initVariables(Bundle savedInstanceState) {
//        app = (MyApplication) getApplication();
//        mGson = app.getGson();
//        mRetrofit = app.getRetrofit();
//        mRetrofitAPI = mRetrofit.create(IRetrofitAPI.class);
//        sharedPreferences = app.getSharedPreferences();
//        user = mGson.fromJson(sharedPreferences.getString(Constant.USER_INFO, ""), User.class);
//
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        navRight = (NavigationView) findViewById(R.id.nav_view_right);
//        accountLayout = (RelativeLayout) findViewById(R.id.account_layout);
//        loginText = (TextView) findViewById(R.id.login_text);
//        infoLayout = (LinearLayout) findViewById(R.id.info_layout);
//        balance = (TextView) findViewById(R.id.balance);
//        infoText = (TextView) findViewById(R.id.info_text);
//        footLayout = (LinearLayout) findViewById(R.id.foot_layout);
//        loginButton = (RelativeLayout) findViewById(R.id.login_button);
//        registerButton = (RelativeLayout) findViewById(R.id.register_button);
//        listMenuView = (ListView) findViewById(R.id.list_menu);
//        listNotiView = (ListView) findViewById(R.id.list_noti);
//
//        titleText = (TextView) findViewById(R.id.title_text);
//        shadowView = findViewById(R.id.shadow_view);
//        menuButton = (RelativeLayout) findViewById(R.id.menu_button);
//        notiButton = (ImageView) findViewById(R.id.noti_button);
//        searchLayout = (RelativeLayout) findViewById(R.id.search_layout);
//        editSearch = (EditText) findViewById(R.id.edit_search);
//        textSearch = (LinearLayout) findViewById(R.id.text_search);
//
//        tabs = (TabLayout) findViewById(R.id.tabs);
//        pager = (ViewPager) findViewById(R.id.pager);
//    }
//
//    @Override
//    protected void initData(Bundle savedInstanceState) {
//        isLogin = sharedPreferences.getBoolean(Constant.IS_LOGIN, false);
//        if (isLogin) {
//            footLayout.setVisibility(View.GONE);
//            loginText.setVisibility(View.GONE);
//            infoLayout.setVisibility(View.VISIBLE);
//
//            getBalance();
//            infoText.setText(user.getFullname() + " - " + user.getMobile());
//        } else {
//            footLayout.setVisibility(View.VISIBLE);
//            loginText.setVisibility(View.VISIBLE);
//            infoLayout.setVisibility(View.GONE);
//        }
//
//        createMainLayout();
//        createDrawerLayout();
//
//        Constant.increaseHitArea(menuButton);
//        Constant.increaseHitArea(notiButton);
//
//        menuButton.setOnClickListener(this);
//        notiButton.setOnClickListener(this);
//        textSearch.setOnClickListener(this);
//        accountLayout.setOnClickListener(this);
//        loginButton.setOnClickListener(this);
//        registerButton.setOnClickListener(this);
//
//        setupReceiver();
//    }
//
//    public void createMainLayout() {
//        listFragment = new ArrayList<>();
//        listFragment.add(HomeFragment.newInstance());
//        listFragment.add(TransferFragment.newInstance());
//        listFragment.add(PaymentFragment.newInstance());
//        listFragment.add(WalletFragment.newInstance());
//
//        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), listFragment);
//        pager.setAdapter(pagerAdapter);
//        pager.setOffscreenPageLimit(4);
//        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                switch (position) {
//                    case 0:
//                        titleText.setText(getResources().getString(R.string.home_page));
//                        shadowView.setVisibility(View.GONE);
//                        break;
//                    case 1:
//                        titleText.setText(getResources().getString(R.string.transfer));
//                        shadowView.setVisibility(View.VISIBLE);
//                        break;
//                    case 2:
//                        titleText.setText(getResources().getString(R.string.payment));
//                        shadowView.setVisibility(View.VISIBLE);
//                        break;
//                    case 3:
//                        titleText.setText(getResources().getString(R.string.personal_information));
//                        shadowView.setVisibility(View.VISIBLE);
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//        tabs.setupWithViewPager(pager);
//
//        TabLayout.Tab tab1 = tabs.getTabAt(0);
//        tab1.setCustomView(createTabView(R.drawable.img_home));
//        TabLayout.Tab tab2 = tabs.getTabAt(1);
//        tab2.setCustomView(createTabView(R.drawable.img_transfer));
//        TabLayout.Tab tab3 = tabs.getTabAt(2);
//        tab3.setCustomView(createTabView(R.drawable.img_payment));
//        TabLayout.Tab tab4 = tabs.getTabAt(3);
//        tab4.setCustomView(createTabView(R.drawable.img_wallet));
//
//        LinearLayout tabStrip = ((LinearLayout) tabs.getChildAt(0));
//        if (!sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
//            tabStrip.setEnabled(false);
//            tabStrip.getChildAt(3).setClickable(false);
//        } else {
//            tabStrip.setEnabled(true);
//            tabStrip.getChildAt(3).setClickable(true);
//        }
//
//        if (!sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
//            tab4.getCustomView().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(MainActivity.this, "Bạn chưa đăng nhập, vui lòng đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    public void createDrawerLayout() {
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        // Create Navigation Drawer layout left
//        listMenu = new ArrayList<>();
//        listMenu.add(new MenuObject(R.mipmap.ic_home_new, getResources().getString(R.string.home_page)));
//        listMenu.add(new MenuObject(R.mipmap.ic_chuyen_tien, getResources().getString(R.string.chuyen_tien)));
//        listMenu.add(new MenuObject(R.mipmap.ic_nap_dt, getResources().getString(R.string.nap_dien_thoai)));
//        listMenu.add(new MenuObject(R.mipmap.ic_mua_the_dt, getResources().getString(R.string.mua_the_dien_thoai)));
//        listMenu.add(new MenuObject(R.mipmap.ic_doi_the_cao, getResources().getString(R.string.doi_the_cao)));
//        listMenu.add(new MenuObject(R.mipmap.ic_mua_game, getResources().getString(R.string.mua_the_game)));
//        listMenu.add(new MenuObject(R.mipmap.ic_nap_tien, getResources().getString(R.string.nap_tien)));
//        listMenu.add(new MenuObject(R.mipmap.ic_rut_tien, getResources().getString(R.string.rut_tien)));
//        listMenu.add(new MenuObject(R.mipmap.ic_hoa_don, getResources().getString(R.string.thanh_toan_hoa_don)));
//        listMenu.add(new MenuObject(R.mipmap.ic_lien_he, getResources().getString(R.string.lien_he)));
//        if (isLogin) {
//            listMenu.add(new MenuObject(R.mipmap.ic_logout, getResources().getString(R.string.dang_xuat)));
//        }
//
//        listMenuAdapter = new ListMenuAdapter(this, listMenu);
//        listMenuView.setAdapter(listMenuAdapter);
//        listMenuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        pager.setCurrentItem(0);
//                        drawer.closeDrawer(GravityCompat.START);
//                        break;
//                    case 1:
//                        pager.setCurrentItem(1);
//                        drawer.closeDrawer(GravityCompat.START);
//                        break;
//                    case 2:
//                        startActivity(PhoneRechargeActivity.class);
//                        drawer.closeDrawer(GravityCompat.START);
//                        break;
//                    case 3:
//                        startActivity(BuyPhoneCardActivity.class);
//                        drawer.closeDrawer(GravityCompat.START);
//                        break;
//                    case 4:
//                        startActivity(DepositCardActivity.class);
//                        drawer.closeDrawer(GravityCompat.START);
//                        break;
//                    case 5:
//                        startActivity(BuyGameCardActivity.class);
//                        drawer.closeDrawer(GravityCompat.START);
//                        break;
//                    case 6:
//                        startActivity(DepositActivity.class);
//                        drawer.closeDrawer(GravityCompat.START);
//                        break;
//                    case 7:
//                        startActivity(WithdrawActivity.class);
//                        drawer.closeDrawer(GravityCompat.START);
//                        break;
//                    case 8:
//                        startActivity(TraGopActivity.class);
//                        drawer.closeDrawer(GravityCompat.START);
//                        break;
//                    case 9:
//                        startActivity(ContactActivity.class);
//                        drawer.closeDrawer(GravityCompat.START);
//                        break;
//                    case 10:
//                        logout();
//                        break;
//                }
//            }
//        });
//
//        // Create Navigation Drawer layout right
//        listTrans = new ArrayList<>();
//        if (sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
//            getAllTrans("");
//        }
//
//        listNotiAdapter = new ListNotiAdapter(this, listTrans);
//        listNotiView.setAdapter(listNotiAdapter);
//        listNotiView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(MainActivity.this, TransDetailActivity.class);
//                i.putExtra("content", listTrans.get(position).getContent());
//                i.putExtra("time", listTrans.get(position).getTransactionDate());
//                startActivity(i);
//            }
//        });
//
//        editSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                handler.removeCallbacks(input_finish_checker);
//                if (s.length() > 0) {
//                    searchContent = s.toString();
//                } else {
//                    textSearch.setVisibility(View.VISIBLE);
//                    editSearch.setVisibility(View.GONE);
//                    searchContent = "";
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    last_text_edit = System.currentTimeMillis();
//                    handler.postDelayed(input_finish_checker, delay);
//                }
//            }
//        });
//    }
//
//    private Runnable input_finish_checker = new Runnable() {
//        public void run() {
//            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
//                getAllTrans(searchContent);
//            }
//        }
//    };
//
//    private void getAllTrans(String value) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -1);
//        Date fromDate = calendar.getTime();
//
//        HashMap<String, Object> body = new HashMap<>();
//        body.put("value", value);
//        body.put("fieldName", "content");
//        body.put("type", "");
//        body.put("fromDate", dateFormat.format(fromDate));
//        body.put("toDate", dateFormat.format(new Date()));
//
//        getAllTransAPI = mRetrofitAPI.getAllTransaction(user.getToken(), body);
//        getAllTransAPI.enqueue(new Callback<TransactionRequest>() {
//            @Override
//            public void onResponse(Call<TransactionRequest> call, Response<TransactionRequest> response) {
//                int errorCode = response.body().getErrorCode();
//                String msg = response.body().getMsg();
//
//                if (errorCode == 1) {
//                    listTrans.removeAll(listTrans);
//                    listTrans.addAll(response.body().getData());
//                    listNotiAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    if (errorCode == -2) {
//                        if (sharedPreferences.getBoolean(Constant.IS_LOGIN, false)) {
//                            logout();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TransactionRequest> call, Throwable t) {
//                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void logout() {
//        final LoadingDialog loadingDialog = new LoadingDialog(this);
//        loadingDialog.show();
//
//        logOutAPI = mRetrofitAPI.logout(user.getToken());
//        logOutAPI.enqueue(new Callback<OtherRequest>() {
//            @Override
//            public void onResponse(Call<OtherRequest> call, Response<OtherRequest> response) {
//                int errorCode = response.body().getErrorCode();
//                String msg = response.body().getMsg();
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                loadingDialog.dismiss();
//
//                drawer.closeDrawer(GravityCompat.START);
//
//                sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, false).apply();
//                sharedPreferences.edit().putString(Constant.USER_INFO, "").apply();
//
//                Intent i = getIntent();
//                finish();
//                startActivity(i);
//            }
//
//            @Override
//            public void onFailure(Call<OtherRequest> call, Throwable t) {
//                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                loadingDialog.dismiss();
//            }
//        });
//    }
//
//    private void getBalance() {
//        getBalanceAPI = mRetrofitAPI.getBalance(user.getToken());
//        getBalanceAPI.enqueue(new Callback<BalanceRequest>() {
//            @Override
//            public void onResponse(Call<BalanceRequest> call, Response<BalanceRequest> response) {
//                int errorCode = response.body().getErrorCode();
//                String msg = response.body().getMsg();
//
//                if (errorCode == 1) {
//                    balance.setText(response.body().getData().getBalance() + " VNĐ");
//                } else {
//                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BalanceRequest> call, Throwable t) {
//                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void setupReceiver() {
//        gotoTransferReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                pager.setCurrentItem(1);
//            }
//        };
//        registerReceiver(gotoTransferReceiver, new IntentFilter(Constant.GOTO_TRANSFER));
//
//        gotoPaymentReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                pager.setCurrentItem(2);
//            }
//        };
//        registerReceiver(gotoPaymentReceiver, new IntentFilter(Constant.GOTO_PAYMENT));
//
//        loginSuccessReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Intent i = getIntent();
//                finish();
//                startActivity(i);
//            }
//        };
//        registerReceiver(loginSuccessReceiver, new IntentFilter(Constant.LOGIN_SUCCESS));
//
//        updateInfoReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                getBalance();
//                getAllTrans("");
//            }
//        };
//        registerReceiver(updateInfoReceiver, new IntentFilter(Constant.UPDATE_INFO));
//    }
//
//    private View createTabView(int imgRes) {
//        View itemView = LayoutInflater.from(this).inflate(R.layout.tabs_main_layout, null);
//
//        ImageView tabsIcon = (ImageView) itemView.findViewById(R.id.tabs_icon);
//        tabsIcon.setImageResource(imgRes);
//
//        return itemView;
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.menu_button:
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                } else {
//                    drawer.openDrawer(GravityCompat.START);
//                }
//                break;
//            case R.id.noti_button:
//                if (drawer.isDrawerOpen(GravityCompat.END)) {
//                    drawer.closeDrawer(GravityCompat.END);
//                } else {
//                    drawer.openDrawer(GravityCompat.END);
//                }
//                break;
//            case R.id.text_search:
//                textSearch.setVisibility(View.GONE);
//                editSearch.setVisibility(View.VISIBLE);
//                editSearch.requestFocus();
//                break;
//            case R.id.account_layout:
//                if (isLogin) {
//                    pager.setCurrentItem(3);
//                } else {
//                    startActivity(LoginActivity.class);
//                }
//
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                } else if (drawer.isDrawerOpen(GravityCompat.END)) {
//                    drawer.closeDrawer(GravityCompat.END);
//                }
//                break;
//            case R.id.login_button:
//                startActivity(LoginActivity.class);
//                drawer.closeDrawer(GravityCompat.START);
//                break;
//            case R.id.register_button:
//                startActivity(RegisterActivity.class);
//                drawer.closeDrawer(GravityCompat.START);
//                break;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
//        } else {
//            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
//                return;
//            }
//
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, "Nhấn BACK lần nữa để thoát", Toast.LENGTH_SHORT).show();
//
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce=false;
//                }
//            }, 2000);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (gotoTransferReceiver != null) {
//            unregisterReceiver(gotoTransferReceiver);
//        }
//        if (gotoPaymentReceiver != null) {
//            unregisterReceiver(gotoPaymentReceiver);
//        }
//        if (loginSuccessReceiver != null) {
//            unregisterReceiver(loginSuccessReceiver);
//        }
//        if (updateInfoReceiver != null) {
//            unregisterReceiver(updateInfoReceiver);
//        }
//    }
}
