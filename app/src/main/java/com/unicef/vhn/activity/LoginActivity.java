package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.LoginPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.view.LoginViews;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements LoginViews {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    private EditText edtVhnId, edtPassword;
    String strVhnId, strPassword,mobileCheck, ipAddress;
    FloatingActionButton fabLogin;

    ProgressDialog pDialog;
    PreferenceData preferenceData;

    LoginPresenter loginPresenter;
    ConnectivityManager conMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        initUI();
        onClickListner();

    }

    private void onClickListner() {
        fabLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        strVhnId = edtVhnId.getText().toString();
        strPassword = edtPassword.getText().toString();
        preferenceData.getDeviceId();
        if (strVhnId.equalsIgnoreCase("")) {
            edtVhnId.setError("VHN ID is Empty");
        } else if (strPassword.equalsIgnoreCase("")) {
            edtPassword.setError("Password is Empty");
        } else {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());


            mobileCheck = "Mobile:"+ Build.MANUFACTURER +","+ "Model:" +Build.MODEL + "," + "Api Version:"
                    + Build.VERSION.RELEASE + "," + "SDK Version:" + Build.VERSION.SDK_INT + "," + "IP Address:"+ ipAddress;

            Log.d("Mobile Check Version-->", mobileCheck);
            loginPresenter.login(strVhnId, strPassword, preferenceData.getDeviceId(),mobileCheck, AppConstants.EXTRA_LATITUDE, AppConstants.EXTRA_LONGITUDE);

        }

    }

    private void initUI() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");

        loginPresenter = new LoginPresenter(LoginActivity.this, this);
        preferenceData = new PreferenceData(this);

        edtVhnId = (EditText) findViewById(R.id.edt_vhn_id);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        fabLogin = (FloatingActionButton) findViewById(R.id.btnLogin);
    }

    @Override
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.dismiss();
    }

    @Override
    public void showLoginSuccess(String response) {

        Log.d("Response success", response);
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(response);
            String status = jObj.getString("status");
            JSONObject strVhnDetails = jObj.getJSONObject("VhnDetails");
            String message = jObj.getString("message");
            if (status.equalsIgnoreCase("1")) {
                Log.d("message---->", message);
                preferenceData.storeUserInfo(strVhnDetails.getString("vhnName"), strVhnDetails.getString("vhnCode"),
                        strVhnDetails.getString("vhnId"));
                preferenceData.setLogin(true);
                if (message.equalsIgnoreCase("Successfully Logined..!")) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }
            } else {
                Log.d("message---->", message);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoginError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }


}

