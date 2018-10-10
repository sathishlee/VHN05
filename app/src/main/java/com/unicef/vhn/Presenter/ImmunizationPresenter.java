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
import com.unicef.vhn.interactor.ImmunizationInteractor;
import com.unicef.vhn.view.ImmunizationViews;
import com.unicef.vhn.view.MotherListsViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class ImmunizationPresenter implements ImmunizationInteractor {
    Activity activity;
    ImmunizationViews motherListsViews;

    public ImmunizationPresenter(Activity activity, ImmunizationViews motherListsViews) {
        this.activity = activity;
        this.motherListsViews = motherListsViews;
    }

    @Override
    public void getImmunizationList(final String vhnCode, final String vhnId, final String id) {


        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.IMMUNIZATION_LIST;
        Log.e("Log in check Url--->", url);
        Log.e("vhnId--->", vhnId);
        Log.e("VhnCode--->", vhnCode);
        Log.e("id--->", id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(ImmunizationPresenter.class.getSimpleName(), "Success" + response);
                motherListsViews.hideProgress();
                motherListsViews.getImmunizationListSuccess(response);
                motherListsViews.callMotherDetailsApi();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(ImmunizationPresenter.class.getSimpleName(), "error" + error);
                motherListsViews.hideProgress();
                motherListsViews.getImmunizationListError(error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode", vhnCode);
                params.put("vhnId", vhnId);
                params.put("id", id);

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

            public int getMethod() {
                return Method.POST;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);
    }

    @Override
    public void getSelectedImmuMother(final String vhnCode, final String vhnId, final String mid) {

        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.IMMUNIZATION_LIST;
        Log.e("Log in check Url--->", url);
        Log.e("vhnId--->", vhnId);
        Log.e("VhnCode--->", vhnCode);
        Log.e("mid--->", mid);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("SelectedMother", "Success" + response);
                motherListsViews.hideProgress();
                motherListsViews.getImmunizationDetailsSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SelectedMother", "error" + error);

                motherListsViews.hideProgress();
                motherListsViews.getImmunizationDetailsError(error.toString());
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

            public int getMethod() {
                return Method.POST;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);
    }
}
