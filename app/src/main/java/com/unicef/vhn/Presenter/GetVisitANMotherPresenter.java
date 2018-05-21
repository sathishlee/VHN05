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
import com.unicef.vhn.interactor.VisitANMotherInteractor;
import com.unicef.vhn.view.VisitANMotherViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathish on 3/23/2018.
 */

public class GetVisitANMotherPresenter implements VisitANMotherInteractor {
    Activity activity;
    VisitANMotherViews visitANMotherViews;

    public GetVisitANMotherPresenter(Activity activity, VisitANMotherViews visitANMotherViews) {
        this.activity = activity;
        this.visitANMotherViews = visitANMotherViews;
    }

    @Override
    public void getVisitANMotherRecords(final String vhnCode, final String vhnId, final String mid) {
        visitANMotherViews.showProgress();
//        String url = Apiconstants.BASE_URL + Apiconstants.POST_VIST_HEALTH_RECORD;
//        String url = Apiconstants.BASE_URL + Apiconstants.POST_VIST_HEALTH_RECORD_PICME;
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_MOTHERS_AN_RECORDS;

        Log.d("Log in check Url--->", url);
        Log.d("vhnCode--->", vhnCode);
        Log.d("vhnId--->", vhnId);
        Log.d("Mid--->", mid);
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                visitANMotherViews.hideProgress();
                visitANMotherViews.showVisitRecordsSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                visitANMotherViews.hideProgress();
                visitANMotherViews.showVisitRecordsFailiur(error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode", vhnCode);
                params.put("vhnId", vhnId);
                params.put("mid", mid);

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
        VolleySingleton.getInstance(activity).addToRequestQueue(strReq);

    }

    @Override
    public void getVisitPNMotherRecords(final String vhnCode, final String vhnId, final String mid) {
        visitANMotherViews.showProgress();
//        String url = Apiconstants.BASE_URL + Apiconstants.POST_VIST_HEALTH_RECORD;
//        String url = Apiconstants.BASE_URL + Apiconstants.POST_VIST_HEALTH_RECORD_PICME;
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_MOTHERS_PN_VISIT_RECORDS;

        Log.d("Log in check Url--->", url);
        Log.d("vhnCode--->", vhnCode);
        Log.d("vhnId--->", vhnId);
        Log.d("Mid--->", mid);
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                visitANMotherViews.hideProgress();
                visitANMotherViews.showVisitRecordsSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                visitANMotherViews.hideProgress();
                visitANMotherViews.showVisitRecordsFailiur(error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("vhnCode",vhnCode);
//                params.put("vhnId",vhnId);
                params.put("mid", mid);

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
        VolleySingleton.getInstance(activity).addToRequestQueue(strReq);


    }
}
