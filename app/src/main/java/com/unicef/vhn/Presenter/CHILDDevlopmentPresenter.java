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
import com.unicef.vhn.interactor.CHILDDevlopmentInteractor;
import com.unicef.vhn.model.ChildDevelopmentModdel.ChildDevQuestionModel;
import com.unicef.vhn.view.CHILDDevlopmentViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CHILDDevlopmentPresenter implements CHILDDevlopmentInteractor {
    Activity activity;
    CHILDDevlopmentViews childDevlopmentViews;

    public CHILDDevlopmentPresenter(Activity activity, CHILDDevlopmentViews childDevlopmentViews) {
        this.activity = activity;
        this.childDevlopmentViews = childDevlopmentViews;
    }

    @Override
    public void getCurrentChildDevMonth(final String strPickmeId, final String strMid) {


    }

    @Override
    public void getAllChildDevelopmentRecords(String strPickmeId, final String strMid) {


        String url = Apiconstants.BASE_URL + Apiconstants.CHILD_DEVELOPMENT_GET_All_REPORT;
        Log.d(" Url--->", url);
        Log.d("strPickmeId --->", strPickmeId);
        Log.d("strMid--->", strMid);
        childDevlopmentViews.showProgress();

        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                childDevlopmentViews.hideProgress();
                Log.e("response",response);
                childDevlopmentViews.getCurrentmonthSuccess(String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                childDevlopmentViews.hideProgress();
                Log.e("error",error.toString());

                childDevlopmentViews.getCurrentmonthFailiure(error.toString());
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

//                params.put("picmeId",strPickmeId);

                params.put("mid",strMid);

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
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);


    }

    @Override
    public void getAllQuestions() {


        String url = Apiconstants.BASE_URL + Apiconstants.CHILD_DEVELOPMENT_GET_All_QUESTION_MASTER;
        Log.d(" Url--->", url);
//        Log.d("strPickmeId --->", strPickmeId);
//        Log.d("strMid--->", strMid);
        childDevlopmentViews.showProgress();

        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                childDevlopmentViews.hideProgress();
                Log.e("response",response);
                childDevlopmentViews.getQuestionsSuccess(String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                childDevlopmentViews.hideProgress();
                Log.e("error",error.toString());

                childDevlopmentViews.getQuestionsFailiure(error.toString());
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

//                params.put("picmeId",strPickmeId);

//                params.put("mid",strMid);

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
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);



    }

    @Override
    public void updateChildDevQuestions(String strPickmeId, String strMid, String strVhnId, String MtrmonthCheck, ArrayList<ChildDevQuestionModel> childDevQuestionModel) {

    }
}
