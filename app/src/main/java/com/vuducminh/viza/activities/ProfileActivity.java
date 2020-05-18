package com.vuducminh.viza.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.dialogs.LoadingDialog;
import com.vuducminh.viza.models.User;
import com.vuducminh.viza.utils.Constant;

import java.util.Calendar;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private MyApplication app;
    private Gson mGson;
    private SharedPreferences sharedPreferences;
    private User user;

    private EditText editFullName;
    private EditText editEmail;
    private EditText editIdentity;
    private EditText editDateBirth;
    private EditText editAddress;
    private RadioButton male;
    private RadioButton female;
    private ImageView pickDate;
    private Button updateButton;
    private LoadingDialog loadingDialog;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        app = (MyApplication) getApplication();
        sharedPreferences = app.getSharedPreferences();

        loadingDialog = new LoadingDialog(this);

        editFullName = (EditText) this.findViewById(R.id.edit_fullname);
        editEmail = (EditText) this.findViewById(R.id.edit_email);
        editIdentity = (EditText) this.findViewById(R.id.edit_identity);
        editDateBirth = (EditText) this.findViewById(R.id.edit_datebirth);
        editAddress = (EditText) this.findViewById(R.id.edit_address);
        male = (RadioButton) this.findViewById(R.id.male);
        female = (RadioButton) this.findViewById(R.id.female);
        pickDate = (ImageView) this.findViewById(R.id.pick_date);
        updateButton = (Button) this.findViewById(R.id.update_button);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        pickDate.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        male.setChecked(true);
        female.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update_button) {

            updateUser();
        }
        if(v.getId() == R.id.pick_date) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    String dateString = Constant.formatTime(dayOfMonth) + "/" + Constant.formatTime(month + 1) + "/" + year;
                    editDateBirth.setText(dateString);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePicker.show();

        }
    }

    public void updateUser() {
        user = (User) getIntent().getSerializableExtra("newUser");
        user.setFullname(editFullName.getText().toString());
        if(Constant.isValid(editEmail.getText().toString())) {
            user.setEmail(editEmail.getText().toString());
        }
        else {
            Toast.makeText(ProfileActivity.this,"Bạn nhận sai định dạng email !",Toast.LENGTH_SHORT).show();
            return;
        }
        user.setIdentityNumber(editIdentity.getText().toString());
        user.setDateBirth(editDateBirth.getText().toString());
        user.setAddress(editAddress.getText().toString());
        if(male.isChecked()) {
            user.setSex("MALE");
        }
        else if(female.isChecked()) {
            user.setSex("FEMALE");
        }

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("newUser", user);
        sharedPreferences.edit().putString(Constant.USER_INFO, user.toString()).apply();
        sharedPreferences.edit().putBoolean(Constant.IS_LOGIN, true).apply();
        FirebaseDatabase.getInstance().getReference(Constant.CUSTOMER)
                .child(user.getMobile())
                .child(Constant.USER)
                .setValue(user);
        startActivity(intent);

        finish();
        Intent i = new Intent(Constant.LOGIN_SUCCESS);
        sendBroadcast(i);
    }
}
