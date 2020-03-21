package com.vuducminh.viza.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.vuducminh.viza.R;


public class PhoneNumberDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private OnNumberClickListener listener;

    private TextView number1, number2;

    public PhoneNumberDialog(@NonNull Context context, OnNumberClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_phone_number);

        number1 = (TextView) findViewById(R.id.number_1);
        number2 = (TextView) findViewById(R.id.number_2);

        number1.setOnClickListener(this);
        number2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.number_1:
                listener.onNumber1Click();
                break;
            case R.id.number_2:
                listener.onNumber2Click();
                break;
        }
    }

    public interface OnNumberClickListener {
        void onNumber1Click();
        void onNumber2Click();
    }
}
