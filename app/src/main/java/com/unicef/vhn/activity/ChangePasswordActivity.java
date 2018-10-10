package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, ChangePasswordViews {

    TextInputLayout input_old_password, input_new_password, input_conform_password;
    EditText edt_old_password, edt_new_password, edt_conform_password;
    Button btn_submit;
    ProgressDialog progressDialog;
    PreferenceData preferenceData;
    String strOldPassword, strNewPassword, strConformPassword;
    ChangePasswordPresenter changePasswordPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initUI();
        showActionBar();
        onClickListner();


    }

    private void initUI() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        preferenceData = new PreferenceData(this);
        changePasswordPresenter = new ChangePasswordPresenter(ChangePasswordActivity.this, this);
        input_old_password = (TextInputLayout) findViewById(R.id.input_old_password);
        input_new_password = (TextInputLayout) findViewById(R.id.input_new_password);
        input_conform_password = (TextInputLayout) findViewById(R.id.input_conform_password);
        edt_old_password = (EditText) findViewById(R.id.edt_old_password);
        edt_new_password = (EditText) findViewById(R.id.edt_new_password);
        edt_conform_password = (EditText) findViewById(R.id.edt_conform_password);
        btn_submit = (Button) findViewById(R.id.btn_submit);

    }

    private void onClickListner() {
        btn_submit.setOnClickListener(this);
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Change Password");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
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
        progressDialog.hide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void changePasswordSuccess(String response) {
        Log.e(ChangePasswordActivity.class.getSimpleName(), "Response Success--->" + response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
        if (TextUtils.isEmpty(strOldPassword)) {
            input_old_password.setError("Please Enter Old Password");
        } else {
            input_old_password.setError(null);
        }
        if (TextUtils.isEmpty(strNewPassword)) {
            input_new_password.setError("Please Enter New Password");
        } else {
            input_new_password.setError(null);
        }
        if (TextUtils.isEmpty(strConformPassword)) {
            input_conform_password.setError("Please Enter Conform Password");
        } else {
            input_conform_password.setError(null);
            changePasswordPresenter.changePassword(preferenceData.getVhnCode(), strOldPassword, strNewPassword, strConformPassword);
        }
    }

    private void textValues() {
        strOldPassword = edt_old_password.getText().toString();
        strNewPassword = edt_new_password.getText().toString();
        strConformPassword = edt_conform_password.getText().toString();
    }
}
