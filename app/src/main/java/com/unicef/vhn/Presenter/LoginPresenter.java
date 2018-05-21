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
    public void login(final String strVhnId, final String strPassword, final String strdeviceId, final String mobileCheck, final String vLatitude, final String vLongitude) {
        view.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.LOG_IN_CHECK;
        Log.d("Log in check Url--->",url);
        Log.d("strVhnId--->",strVhnId);
        Log.d("strPassword--->",strPassword);
        Log.d("Device Id-->",strdeviceId);
        Log.d("mobileCheck-->",mobileCheck);
        Log.d("vLatitude-->",vLatitude);
        Log.d("vLongitude-->",vLongitude);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Log in success",response);
                view.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

                Log.d("params--->",params.toString());

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
    }
}
