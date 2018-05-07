package com.unicef.vhn.Presenter;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.interactor.NotificationInteractor;
import com.unicef.vhn.view.NotificationViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathish on 3/29/2018.
 */

public class NotificationPresenter implements NotificationInteractor {

    Context context;
    NotificationViews notificationViews;

    public NotificationPresenter(Context context, NotificationViews notificationViews) {
        this.context = context;
        this.notificationViews = notificationViews;
    }

    @Override
    public void getNotificationCount(final String vhnId) {

        String url = Apiconstants.BASE_URL + Apiconstants.POST_NOTIFICATION_COUNT;
        Log.d("Url--->", url);
        Log.d("mid--->", vhnId);
        notificationViews.showProgress();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                notificationViews.hideProgress();
                notificationViews.NotificationCountSuccess(String.valueOf(response));
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                notificationViews.hideProgress();
                notificationViews.NotificationCountError(error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "admin" + ":" + "1234";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                HashMap<String, String> header = new HashMap<>();
                //                header.put("Content-Type", "application/x-www-from-urlencoded; charset=utf-8");
                header.put("Authorization", "Basic " + base64EncodedCredentials);
//                header.put("Content-Type", "application/json; charset=utf-8");
                Log.d("Credentials ", "Basic " + base64EncodedCredentials.toString());

                return header;
            }
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnId",vhnId);

                Log.d("params--->",params.toString());

                return params;
            }

            //            public String getBodyContentType() {
            //                return "application/x-www-from-urlencoded; charset=utf-8";
            //            }

            public int getMethod() {
                return Method.POST;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void getTodayVisitCount(final String vhnCode, final String vhnId) {


        String url = Apiconstants.BASE_URL + Apiconstants.CURRENT_VISIT_COUNT;
        Log.d("Url--->", url);
        Log.d("vhnCode--->", vhnCode);
        Log.d("vhnId--->", vhnId);
        notificationViews.showProgress();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                notificationViews.hideProgress();
                notificationViews.TodayVisitResponseSuccess(String.valueOf(response));
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                notificationViews.hideProgress();
                notificationViews.TodayVisitResponseError(error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "admin" + ":" + "1234";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                HashMap<String, String> header = new HashMap<>();
                //                header.put("Content-Type", "application/x-www-from-urlencoded; charset=utf-8");
                header.put("Authorization", "Basic " + base64EncodedCredentials);
//                header.put("Content-Type", "application/json; charset=utf-8");
                Log.d("Credentials ", "Basic " + base64EncodedCredentials.toString());

                return header;
            }
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnId",vhnId);
//                params.put("vhnCode",vhnCode);

                Log.d("params--->",params.toString());

                return params;
            }

            //            public String getBodyContentType() {
            //                return "application/x-www-from-urlencoded; charset=utf-8";
            //            }

            public int getMethod() {
                return Method.POST;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);

    }

    @Override
    public void getNotificationList(final String vhnCode,final String vhnId) {


        String url = Apiconstants.BASE_URL + Apiconstants.POST_NOTIFICATION_LIST;
        Log.d("Url--->", url);
        Log.d("vhnCode--->", vhnCode);
        Log.d("vhnId--->", vhnId);
        notificationViews.showProgress();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                notificationViews.hideProgress();
                notificationViews.NotificationResponseSuccess(String.valueOf(response));
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                notificationViews.hideProgress();
                notificationViews.NotificationResponseError(error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "admin" + ":" + "1234";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                HashMap<String, String> header = new HashMap<>();
                //                header.put("Content-Type", "application/x-www-from-urlencoded; charset=utf-8");
                header.put("Authorization", "Basic " + base64EncodedCredentials);
//                header.put("Content-Type", "application/json; charset=utf-8");
                Log.d("Credentials ", "Basic " + base64EncodedCredentials.toString());

                return header;
            }
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode",vhnCode);
                params.put("vhnId",vhnId);

                Log.d("params--->",params.toString());

                return params;
            }

            //            public String getBodyContentType() {
            //                return "application/x-www-from-urlencoded; charset=utf-8";
            //            }

            public int getMethod() {
                return Method.POST;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void getNotificationDetails(String mid) {

    }
}
