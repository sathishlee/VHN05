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
import com.unicef.vhn.interactor.PNMotherListInteractor;
import com.unicef.vhn.view.PNMotherListsViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathish on 3/20/2018.
 */

public class PNMotherListPresenter implements PNMotherListInteractor {

    Activity activity;
    PNMotherListsViews pnMotherListsViews;

    public PNMotherListPresenter(Activity activity, PNMotherListsViews pnMotherListsViews) {
        this.activity = activity;
        this.pnMotherListsViews = pnMotherListsViews;
    }

    @Override
    public void getPNMotherList(final String vhnCode, final String vhnId) {
        pnMotherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.MOTHER_DETAILS_LIST;
        Log.d("Log in check Url--->",url);
        Log.d("vhnCode--->",vhnCode);
        Log.d("vhnId--->",vhnId);
        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pnMotherListsViews.hideProgress();
                pnMotherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pnMotherListsViews.hideProgress();
                pnMotherListsViews.showLoginSuccess(error.toString());
            }
        }){



            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode",vhnCode);
                params.put("vhnId",vhnId);

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

    @Override
    public void getSelectedPNMother(final String vhnCode, final String vhnId, final String mid) {

        pnMotherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.MOTHER_DETAILS_TRACKING;
        Log.d("Log in check Url--->",url);
        Log.d("vhnCode--->",vhnCode);
        Log.d("vhnId--->",vhnId);
        Log.d("mid--->",mid);
        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pnMotherListsViews.hideProgress();
                pnMotherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pnMotherListsViews.hideProgress();
                pnMotherListsViews.showLoginSuccess(error.toString());
            }
        }){



            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode",vhnCode);
                params.put("vhnId",vhnId);
                params.put("mid",mid);

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
