package com.unicef.vhn.activity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.LoginPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.fragment.home;
import com.unicef.vhn.view.LoginViews;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements LoginViews {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    private EditText edtVhnId, edtPassword;
    String strVhnId, strPassword, mobileCheck, ipAddress;
    Button btnlogin;

    ProgressDialog pDialog;
    PreferenceData preferenceData;

    LoginPresenter loginPresenter;
    ConnectivityManager conMgr;
    private TextView txtForgotPassword;

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
        btnlogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        txtForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
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


            mobileCheck = "Mobile:" + Build.MANUFACTURER + "," + "Model:" + Build.MODEL + "," + "Api Version:"
                    + Build.VERSION.RELEASE + "," + "SDK Version:" + Build.VERSION.SDK_INT + "," + "IP Address:" + ipAddress;

            Log.d("Mobile Check Version-->", mobileCheck);

            PackageInfo packageInfo = null;
            String version_name = "Latest";
            int version_code = 5;
            String appversion = String.valueOf(version_code);

            try {
                packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                version_name = packageInfo.versionName;
                version_code = packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            loginPresenter.login(strVhnId, strPassword, preferenceData.getDeviceId(), mobileCheck, AppConstants.EXTRA_LATITUDE, AppConstants.EXTRA_LONGITUDE, appversion);

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
        btnlogin = (Button) findViewById(R.id.btnlogin);
        txtForgotPassword = (TextView) findViewById(R.id.txt_forgot_password);
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

        Log.e("Response success", response);
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(response);
            String status = jObj.getString("status");
            String message = jObj.getString("message");
            if (message.equalsIgnoreCase("Please update the latest version app.")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Your are using Older Version of Apk Please Click Ok to Download New Apk")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                openUrl();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else if (status.equalsIgnoreCase("1")) {
                JSONObject strVhnDetails = jObj.getJSONObject("VhnDetails");
                Log.d("message---->", message);
                preferenceData.storeUserInfo(strVhnDetails.getString("vhnName"), strVhnDetails.getString("vhnCode"),
                        strVhnDetails.getString("vhnId"), strVhnDetails.getString("vphoto"));
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
        Log.e(LoginActivity.class.getSimpleName(), "error " + message);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.create();

    }

    private void openUrl() {
        DownloadManager downloadManager;
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(Apiconstants.APK_URL + Apiconstants.DOWNLOAD_APK);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Are you Sure do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}

