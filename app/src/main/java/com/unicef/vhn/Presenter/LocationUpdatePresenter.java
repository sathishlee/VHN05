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
import com.unicef.vhn.interactor.LocationUpdateIntractor;
import com.unicef.vhn.view.LocationUpdateViews;
import com.unicef.vhn.volleyservice.VolleySingleton;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathish on 3/14/2018.
 */

public class LocationUpdatePresenter implements LocationUpdateIntractor {
    private LocationUpdateViews view;
    private Activity activity;

    public LocationUpdatePresenter(Activity mainActivity, LocationUpdateViews view) {
        this.activity = mainActivity;
        this.view = view;
    }

    @Override
    public void uploadLocationToServer( final String vhnId,  final String latitude, final String longitude) {

        view.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.LOCATION_UPDATE;

        Log.d("Log in check Url--->",url);
        Log.d("Url--->",url);
        Log.d("vhnId--->",vhnId);
        Log.d("latitude--->",latitude);
        Log.d("longitude--->",longitude);

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                view.hideProgress();
                Log.d("success",response);

                view.locationUpdateSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                view.hideProgress();
                Log.d(" error",error.toString());

                view.locationUpdateFailiure(error.toString());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnId",vhnId);
                params.put("vLatitude",latitude);
                params.put("vLongitude",longitude);

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
        VolleySingleton.getInstance(activity).addToRequestQueue(strReq);
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);
    }


}
