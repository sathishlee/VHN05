package com.unicef.vhn.Presenter;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.interactor.ChangePasswordInteractor;
import com.unicef.vhn.view.ChangePasswordViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ChangePasswordPresenter implements ChangePasswordInteractor {


    private ChangePasswordViews changePasswordViews;
    private Activity activity;

    public ChangePasswordPresenter(ChangePasswordViews changePasswordViews, Activity activity) {
        this.changePasswordViews = changePasswordViews;
        this.activity = activity;
    }

    @Override
    public void changePassword(final String vhnCode, final String vhnPassword, final String vhnNewPassword, final String vhnConPassword) {
        changePasswordViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.CHANGE_PASSWORD;
        Log.d("VHN Code--->", vhnCode);
        Log.d("VHN Password--->", vhnPassword);
        Log.d("VHN New Password", vhnNewPassword);
        Log.d("VHN Conform Password", vhnConPassword);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                changePasswordViews.hideProgress();
                changePasswordViews.changePasswordSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                changePasswordViews.hideProgress();
                changePasswordViews.changePasswordFailure(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode", vhnCode);
                params.put("vhnPassword", vhnPassword);
                params.put("vhnNewPassword", vhnNewPassword);
                params.put("vhnConPassword", vhnConPassword);

                Log.d("params--->", params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "admin" + ":" + "1234";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                HashMap<String, String> header = new HashMap<>();
//                header.put("Content-Type", "application/x-www-from-urlencoded; charset=utf-8");
                header.put("Authorization", "Basic " + base64EncodedCredentials);
                Log.d("Credentials ", "Basic " + base64EncodedCredentials.toString());

                return header;
            }

//            public String getBodyContentType() {
//                return "application/x-www-from-urlencoded; charset=utf-8";
//            }

            public int getMethod() {
                return Method.POST;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(activity).addToRequestQueue(request);
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);
    }

    @Override
    public void forgetPassword(final String vhnCode, final String vhnMobileNumber) {
        changePasswordViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.FORGOT_PASSWORD;
        Log.e("url--->", url);
        Log.e("VHN Code--->", vhnCode);
        Log.e("VHN MobileNumber--->", vhnMobileNumber);


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("forgot response--->", "success"+response);
                changePasswordViews.hideProgress();
                changePasswordViews.changePasswordSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("forgot error--->", error+"");

                changePasswordViews.hideProgress();
                changePasswordViews.changePasswordFailure(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode", vhnCode);
                params.put("vhnMobile", vhnMobileNumber);

                Log.e("params--->", params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "admin" + ":" + "1234";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                HashMap<String, String> header = new HashMap<>();
//                header.put("Content-Type", "application/x-www-from-urlencoded; charset=utf-8");
                header.put("Authorization", "Basic " + base64EncodedCredentials);
                Log.d("Credentials ", "Basic " + base64EncodedCredentials.toString());

                return header;
            }

//            public String getBodyContentType() {
//                return "application/x-www-from-urlencoded; charset=utf-8";
//            }

            public int getMethod() {
                return Method.POST;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(activity).addToRequestQueue(request);
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);

    }
}
