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
import com.unicef.vhn.interactor.LoginInteractor;
import com.unicef.vhn.view.LoginViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathish on 3/20/2018.
 */

public class LoginPresenter implements LoginInteractor{
    private LoginViews view;
    private Activity activity;

    public LoginPresenter(Activity activity, LoginViews view) {
        this.activity = activity;
        this.view = view;
    }

    @Override
    public void login(final String strVhnId, final String strPassword, final String strdeviceId,
                      final String mobileCheck, final String vLatitude, final String vLongitude,
                      final String versionCode) {
        view.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.LOG_IN_CHECK;
        Log.e("Log in check Url--->",url);
        Log.e("strVhnId--->",strVhnId);
        Log.e("strPassword--->",strPassword);
        Log.e("Device Id-->",strdeviceId);
        Log.e("mobileCheck-->",mobileCheck);
        Log.e("vLatitude-->",vLatitude);
        Log.e("vLongitude-->",vLongitude);
        Log.e("versionCode-->",versionCode);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
view.hideProgress();
                Log.d("Log in success",response);
                view.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
view.hideProgress();
                Log.d("Log in Error",error.toString());
                view.showLoginError(error.toString());
            }
        }){


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode",strVhnId);
                params.put("vhnPassword",strPassword);
                params.put("deviceId",strdeviceId);
                params.put("mobileCheck",mobileCheck);
                params.put("vLatitude",vLatitude);
                params.put("vLongitude",vLongitude);
                params.put("appversion",versionCode);

                Log.e("params--->",params.toString());

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "admin" + ":" + "1234";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                HashMap<String, String> header = new HashMap<>();
//                header.put("Content-Type", "application/x-www-from-urlencoded; charset=utf-8");
                header.put("Authorization", "Basic " + base64EncodedCredentials);
                Log.d("Credentials ","Basic " +base64EncodedCredentials.toString());

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
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);
    }
}
