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
import com.unicef.vhn.interactor.MotherListInteractor;
import com.unicef.vhn.view.MotherListsViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathish on 3/20/2018.
 */

public class MotherListPresenter implements MotherListInteractor {

    Activity activity;
    MotherListsViews motherListsViews;

    public MotherListPresenter(Activity activity, MotherListsViews motherListsViews) {
        this.activity = activity;
        this.motherListsViews = motherListsViews;
    }

    @Override
    public void getPNMotherList(final String vhnCode, final String vhnId) {
        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.MOTHER_DETAILS_LIST;
        Log.d("Log in check Url--->",url);
        Log.d("vhnCode--->",vhnCode);
        Log.d("vhnId--->",vhnId);
        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(error.toString());
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
    public void getPNMotherRecordsList(final String vhnCode, final String vhnId) {

        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_MOTHERS_PN_RECORDS;
        Log.d("Log in check Url--->",url);
        Log.d("vhnCode--->",vhnCode);
        Log.d("vhnId--->",vhnId);
        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(error.toString());
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
    public void getSelectedMother(final String vhnCode, final String vhnId, final String mid) {

        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.MOTHER_DETAILS_TRACKING;
        Log.d("Log in check Url--->",url);
        Log.d("vhnCode--->",vhnCode);
        Log.d("vhnId--->",vhnId);
        Log.d("mid--->",mid);
        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SelectedMother","Success"+response);
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(error.toString());
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