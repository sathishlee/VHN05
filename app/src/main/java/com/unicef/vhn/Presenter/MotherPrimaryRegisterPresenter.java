package com.unicef.vhn.Presenter;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.fragment.mothers;
import com.unicef.vhn.interactor.MotherPrimaryRegisterInteractor;
import com.unicef.vhn.view.PrimaryRegisterViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class MotherPrimaryRegisterPresenter implements MotherPrimaryRegisterInteractor {

    private PrimaryRegisterViews primaryRegisterViews;
    private Activity activity;

    public MotherPrimaryRegisterPresenter(PrimaryRegisterViews primaryRegisterViews, Activity activity) {
        this.primaryRegisterViews = primaryRegisterViews;
        this.activity = activity;
    }


    @Override
    public void getAllMotherPrimaryRegistration(final String picmeId) {

        String url = Apiconstants.MOTHER_BASE_URL + Apiconstants.GET_MOTHER_PRIMARY_INFO;
       /* if (Apiconstants.BASE_URL.equalsIgnoreCase("http://192.168.100.222/thaimaiapp/api/vhn/Vhn/")){
                     url = "http://192.168.100.222/thaimaiapp/api/" + Apiconstants.GET_MOTHER_PRIMARY_INFO;
        }else {
            url = "http://218.248.44.77/thaimaiapp/api/" + Apiconstants.GET_MOTHER_PRIMARY_INFO;

        }*/
        Log.e("Log in check Url--->", url);
        Log.e("picmeId--->", picmeId);
        primaryRegisterViews.showProgress();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(MotherPrimaryRegisterPresenter.class.getSimpleName(), "Success response" + response);
                primaryRegisterViews.hideProgress();
                primaryRegisterViews.getAllMotherPrimaryRegisterSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(MotherPrimaryRegisterPresenter.class.getSimpleName(), "Success response" + error.toString());
                primaryRegisterViews.hideProgress();
                primaryRegisterViews.getAllMotherPrimaryRegisterFailure(error.toString());

            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("picmeId", picmeId);

                Log.e("params--->", params.toString());

                return params;
            }

            @RequiresApi(api = Build.VERSION_CODES.FROYO)
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
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);

    }
}
