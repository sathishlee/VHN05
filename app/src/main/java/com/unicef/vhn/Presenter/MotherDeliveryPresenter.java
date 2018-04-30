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
import com.unicef.vhn.interactor.MotherDeliveryInteractor;
import com.unicef.vhn.view.MotherDeliveryViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class MotherDeliveryPresenter implements MotherDeliveryInteractor {

    Activity context;
    MotherDeliveryViews views;

    public MotherDeliveryPresenter (Activity context, MotherDeliveryViews views){
        this.context = context;
        this.views = views;
    }

    @Override
    public void deliveryDetails(final String strPicmeid, final String strMid) {
        String url = Apiconstants.MOTHER_BASE_URL + Apiconstants.DELIVERY_DETAILS;
        Log.d("Log in check Url--->", url);
        views.showProgress();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                views.deliveryDetailsSuccess(response);
                views.hideProgress();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                views.hideProgress();
                views.deliveryDetailsFailure(error.toString());
            }
        })
        {


            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                String credentials ="admin"+":"+"1234";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(),Base64.DEFAULT);
                HashMap<String,String> header = new HashMap<>();
//                header.put("Content-Type","application/x-www-from-urlencoded; charset=utf-8");
                header.put("Authorization","Basic "+base64EncodedCredentials);
                return header;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("dpicmeId",strPicmeid);
                params.put("mid",strMid);

                Log.d("params--->",params.toString());

                return params;
            }
            //            public String getBodyContentType(){
//                return "application/x-www-from-urlencoded; charset=utf-8";
//            }
            public int getMethod(){
                return Method.POST;
            }
        };


        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(strReq);

    }
}
