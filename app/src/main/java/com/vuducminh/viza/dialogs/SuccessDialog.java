package com.vuducminh.viza.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.vuducminh.viza.R;


public class SuccessDialog extends Dialog {
    private String content;
    private TextView contentText;
    private TextView okButton;

    public SuccessDialog(Context context, String content) {
        super(context);
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        setContentView(R.layout.dialog_success);

        contentText = (TextView) findViewById(R.id.content_text);
        okButton = (TextView) findViewById(R.id.ok_button);

        contentText.setText(content);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
