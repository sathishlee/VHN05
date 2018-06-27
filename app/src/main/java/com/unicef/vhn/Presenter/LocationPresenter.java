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
import com.unicef.vhn.interactor.LocationInteractor;
import com.unicef.vhn.view.LocationViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class LocationPresenter implements LocationInteractor {

    Activity activity;
    LocationViews locationViews;

    public LocationPresenter(Activity activity, LocationViews locationViews){
        this.activity=activity;
        this.locationViews=locationViews;
    }

    @Override
    public void getMotherLocatin(final String vhnCode, final String vhnId, final String mid) {
        locationViews.showProgress();

        String url = Apiconstants.BASE_URL + Apiconstants.DIRECTION_URL;
        Log.d("Log in check Url--->",url);
        Log.d("vhnCode--->",vhnCode);
        Log.d("vhnId--->",vhnId);
        Log.d("mid--->",mid);
//        Log.d("mLongitude--->",mLongitude);
//        Log.d("vLatitude--->",vLatitude);
//        Log.d("vLongitude--->",vLongitude);

        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                locationViews.hideProgress();
                locationViews.showLocationSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                locationViews.hideProgress();
                locationViews.showLocationError(error.toString());
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
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);
    }
}
