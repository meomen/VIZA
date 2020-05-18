package com.vuducminh.viza.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.provider.Settings;
import android.view.TouchDelegate;
import android.view.View;


import com.google.firebase.storage.StorageReference;
import com.vuducminh.viza.activities.MainActivity;

import java.math.BigInteger;
import java.security.MessageDigest;


public class Constant {
    public static final String BASE_URL = "https://m.thecaosieure.com";
    public static final String IMAGE_BANNER_URL = "https://thecaosieure.com/Uploads/advertise/";
    public static final String IMAGE_NEWS_URL = "https://thecaosieure.com/Uploads/articles/";
    public static final String IMAGE_CARD_URL = "https://thecaosieure.com/Uploads/banks/";
    public static final String EBANKING_URL = "https://m.thecaosieure.com/DepositBankNetRequest?tokenapp=";

    public static String CUSTOMER = "customer";
    public static String USER = "user";
    public static String HISTORY = "history";
    public static String WALLET = "wallet";
    public static String MONEY = "money";
    public static String ORDER = "order";
    public static String ORDER_DEPOSIT = "order_deposit";
    public static String ORDER_CARD_PHONE = "order_card_phone";
    public static String ORDER_CARD_GAME = "order_card_game";
    public static String CONNECT_BANK = "connect_bank";


    public static String IS_LOGIN = "is_login";
    public static String LOGIN_SUCCESS = "login_success";
    public static String USER_INFO = "user_info";
    public static String CHANGE_FILE_FRAGMENT = "change_file_fragment";
    public static String GOTO_TRANSFER = "goto_transfer";
    public static String GOTO_PAYMENT = "goto_payment";
    public static final String UPDATE_INFO = "update_info";
    public static final String UPDATE_PROFILE = "update_profile";

    public static int TYPE_TRANS = 1;
    public static int TYPE_BUY_CARD_PHONE = 2;
    public static int TYPE_BUY_CARD_GAME = 3;
    public static int TYPE_DEPOSTI = 4;
    public static int TYPE_WITHDAW = 5;

    public static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static String getDeviceId(Context context) {
        String device_uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (device_uuid == null) {
            device_uuid = "12356789"; // for emulator testing
        } else {
            try {
                byte[] _data = device_uuid.getBytes();
                MessageDigest _digest = MessageDigest.getInstance("MD5");
                _digest.update(_data);
                _data = _digest.digest();
                BigInteger _bi = new BigInteger(_data).abs();
                device_uuid = _bi.toString(36);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return device_uuid;
    }

    public static int convertDpIntoPixels(int sizeInDp, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp * scale + 0.5f);
        return dpAsPixels;
    }

    public static void increaseHitArea(final View button) {
        final View parent = (View) button.getParent();  // button: the view you want to enlarge hit area
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                button.getHitRect(rect);
                rect.top -= 100;    // increase top hit area
                rect.left -= 100;   // increase left hit area
                rect.bottom += 100; // increase bottom hit area
                rect.right += 100;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect, button));
            }
        });
    }

    public static String formatTime(int time) {
        String formatedTime = "";

        if (time < 10) {
            formatedTime = "0" + time;
        } else {
            formatedTime = "" + time;
        }

        return formatedTime;
    }

    public static void restartApp(Context context) {
        Intent mStartActivity = new Intent(context, MainActivity.class);
        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

}
