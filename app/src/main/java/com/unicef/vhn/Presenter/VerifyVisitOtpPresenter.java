package com.unicef.vhn.Presenter;

import android.app.VoiceInteractor;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.interactor.TodayVisitcloseInteractor;
import com.unicef.vhn.view.TodayVisitCloseViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class VerifyVisitOtpPresenter implements TodayVisitcloseInteractor {


    Context context;
    TodayVisitCloseViews views;

    public VerifyVisitOtpPresenter(Context context, TodayVisitCloseViews views) {
        this.context = context;
        this.views = views;
    }
    @Override
    public void getverifyOTP(final String noteId, final String visitOTP, final String mid) {
        String url = Apiconstants.BASE_URL + Apiconstants.TODAY_VISIT_CLOSED;
        Log.e(VerifyVisitOtpPresenter.class.getSimpleName(),"verify otp url"+url);
        Log.e(VerifyVisitOtpPresenter.class.getSimpleName(),"noteId"+noteId);
        Log.e(VerifyVisitOtpPresenter.class.getSimpleName(),"mid"+mid);
        Log.e(VerifyVisitOtpPresenter.class.getSimpleName(),"mid"+visitOTP);
views.showProgress();
        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(VerifyVisitOtpPresenter.class.getSimpleName(),"success response"+response);
                views.hideProgress();
                views.todayVisitCloseSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(VerifyVisitOtpPresenter.class.getSimpleName(),"error response"+error.toString());
views.hideProgress();
views.todayVisitCloseFailiur(error.toString());

            }
        }){


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("noteId", noteId);
                params.put("visitOTP", visitOTP);
                params.put("mid", mid);

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
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(context).getRequestQueue().getCache().remove(url);

    }
}
