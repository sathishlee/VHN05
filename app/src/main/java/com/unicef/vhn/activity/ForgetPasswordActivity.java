package com.unicef.vhn.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.ChangePasswordPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.view.ChangePasswordViews;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordActivity extends AppCompatActivity implements ChangePasswordViews, View.OnClickListener {

    TextInputLayout  input_new_password, input_conform_password;
    EditText  edt_new_password, edt_conform_password;
    Button btn_submit;
    ProgressDialog progressDialog;
    PreferenceData preferenceData;
    String strOldPassword, strNewPassword, strConformPassword;
    ChangePasswordPresenter changePasswordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initUI();
        showActionBar();
        onClickListner();
    }
    private void initUI() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        preferenceData = new PreferenceData(this);
        changePasswordPresenter = new ChangePasswordPresenter(ForgetPasswordActivity.this, this);

        input_new_password = (TextInputLayout) findViewById(R.id.input_new_password);
        input_conform_password = (TextInputLayout) findViewById(R.id.input_conform_password);

        edt_new_password = (EditText) findViewById(R.id.edt_new_password);
        edt_conform_password = (EditText) findViewById(R.id.edt_conform_password);
        btn_submit = (Button) findViewById(R.id.btn_submit);

    }

    private void onClickListner() {
        btn_submit.setOnClickListener(this);
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Forgot Password");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        progressDialog.show();

    }

    @Override
    public void hideProgress() {
progressDialog.dismiss();
    }

    @Override
    public void changePasswordSuccess(String response) {

        Log.e(ForgetPasswordActivity.class.getSimpleName(), "Response Success--->" + response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("message");
            String password = jsonObject.getString("password");
            if (status.equalsIgnoreCase("1")) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//
                AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
                builder.setTitle(msg);
//                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Your Password is :- "+password)
                        .setCancelable(false)
                        .setPositiveButton("Back To Login", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void changePasswordFailure(String response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                changePassword();
                break;
        }
    }

    private void changePassword() {

        textValues();

        if (TextUtils.isEmpty(strNewPassword)) {
            input_new_password.setError("Please Enter Vhn Code");
        } else {
            input_new_password.setError(null);
        }
        if (TextUtils.isEmpty(strConformPassword)) {
            input_conform_password.setError("Please Enter Mobile Number");
        } else {
            input_conform_password.setError(null);
            changePasswordPresenter.forgetPassword(strNewPassword, strConformPassword);
        }
    }
    private void textValues() {

        strNewPassword = edt_new_password.getText().toString();
        strConformPassword = edt_conform_password.getText().toString();
    }
}
