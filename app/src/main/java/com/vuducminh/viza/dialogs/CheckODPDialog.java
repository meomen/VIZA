package com.vuducminh.viza.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.vuducminh.viza.R;


/**
 * Created by lequy on 10/16/2017.
 */

public class CheckODPDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private OnButtonClickListener listener;

    private LinearLayout layoutInput;
    private EditText editODP;
    private TextView textNum1, textNum2, textNum3, textNum4;
    private TextView buttonResend;
    private TextView buttonAccept;

    private InputMethodManager inputMethodManager;

    public CheckODPDialog(@NonNull Context context, OnButtonClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_check_odp);

        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        layoutInput = (LinearLayout) findViewById(R.id.layout_input);
        editODP = (EditText) findViewById(R.id.edit_odp);
        textNum1 = (TextView) findViewById(R.id.text_num_1);
        textNum2 = (TextView) findViewById(R.id.text_num_2);
        textNum3 = (TextView) findViewById(R.id.text_num_3);
        textNum4 = (TextView) findViewById(R.id.text_num_4);
        buttonResend = (TextView) findViewById(R.id.button_resend);
        buttonAccept = (TextView) findViewById(R.id.button_accept);

        editODP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String odp = s.toString();
                int length = odp.length();
                switch (length) {
                    case 0:
                        textNum1.setVisibility(View.GONE);
                        textNum2.setVisibility(View.GONE);
                        textNum3.setVisibility(View.GONE);
                        textNum4.setVisibility(View.GONE);
                        break;
                    case 1:
                        textNum1.setVisibility(View.VISIBLE);
                        textNum2.setVisibility(View.GONE);
                        textNum3.setVisibility(View.GONE);
                        textNum4.setVisibility(View.GONE);

                        textNum1.setText(odp);
                        break;
                    case 2:
                        textNum1.setVisibility(View.VISIBLE);
                        textNum2.setVisibility(View.VISIBLE);
                        textNum3.setVisibility(View.GONE);
                        textNum4.setVisibility(View.GONE);

                        textNum1.setText(String.valueOf(odp.charAt(0)));
                        textNum2.setText(String.valueOf(odp.charAt(1)));
                        break;
                    case 3:
                        textNum1.setVisibility(View.VISIBLE);
                        textNum2.setVisibility(View.VISIBLE);
                        textNum3.setVisibility(View.VISIBLE);
                        textNum4.setVisibility(View.GONE);

                        textNum1.setText(String.valueOf(odp.charAt(0)));
                        textNum2.setText(String.valueOf(odp.charAt(1)));
                        textNum3.setText(String.valueOf(odp.charAt(2)));
                        break;
                    case 4:
                        textNum1.setVisibility(View.VISIBLE);
                        textNum2.setVisibility(View.VISIBLE);
                        textNum3.setVisibility(View.VISIBLE);
                        textNum4.setVisibility(View.VISIBLE);

                        textNum1.setText(String.valueOf(odp.charAt(0)));
                        textNum2.setText(String.valueOf(odp.charAt(1)));
                        textNum3.setText(String.valueOf(odp.charAt(2)));
                        textNum4.setText(String.valueOf(odp.charAt(3)));
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        layoutInput.setOnClickListener(this);
        buttonResend.setOnClickListener(this);
        buttonAccept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_input:
                editODP.requestFocus();
                editODP.setText("");

                inputMethodManager.toggleSoftInputFromWindow(editODP.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                break;
            case R.id.button_resend:
                listener.onResendClick();
                break;
            case R.id.button_accept:
                listener.onAcceptClick(editODP.getText().toString());
                break;
        }
    }

    public interface OnButtonClickListener {
        void onResendClick();
        void onAcceptClick(String odp);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
