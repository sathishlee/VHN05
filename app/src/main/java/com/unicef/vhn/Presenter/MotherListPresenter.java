package com.unicef.vhn.Presenter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.interactor.MotherListInteractor;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.realmDbModel.PNMMotherListRealmModel;
import com.unicef.vhn.view.MotherListsViews;
import com.unicef.vhn.volleyservice.VolleySingleton;
import io.realm.Realm;
import io.realm.RealmResults;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathish on 3/20/2018.
 */

public class MotherListPresenter implements MotherListInteractor {

    Context context;
    MotherListsViews motherListsViews;
    Realm realm;

    public MotherListPresenter(Context context, MotherListsViews motherListsViews, Realm realm) {
        this.context = context;
        this.motherListsViews = motherListsViews;
        this.realm = realm;
    }

    @Override
    public void getPNMotherList(String callurl, final String vhnCode, final String vhnId) {
        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + callurl;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
                try {
                    JSONObject mJsnobject = new JSONObject(response);
                    String status = mJsnobject.getString("status");
                    String message = mJsnobject.getString("message");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
                        RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = null;
                        motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).findAll();
                        Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.delete(PNMMotherListRealmModel.class);
                            }
                        });
                        PNMMotherListRealmModel pnmMotherListRealmModel;

                        if (jsonArray.length() != 0) {
                            realm.beginTransaction();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                pnmMotherListRealmModel = realm.createObject(PNMMotherListRealmModel.class);

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                pnmMotherListRealmModel.setMid(jsonObject.getString("mid"));
                                pnmMotherListRealmModel.setmName(jsonObject.getString("mName"));
                                pnmMotherListRealmModel.setmAge(jsonObject.getString("mAge"));
                                pnmMotherListRealmModel.setmPicmeId(jsonObject.getString("mPicmeId"));
                                pnmMotherListRealmModel.setVhnId(jsonObject.getString("vhnId"));
                                pnmMotherListRealmModel.setmMotherMobile(jsonObject.getString("mMotherMobile"));
                                pnmMotherListRealmModel.setMotherType(jsonObject.getString("motherType"));
                                pnmMotherListRealmModel.setmLatitude(jsonObject.getString("mLatitude"));
                                pnmMotherListRealmModel.setmLongitude(jsonObject.getString("mLongitude"));
                                pnmMotherListRealmModel.setmPhoto(jsonObject.getString("mPhoto"));
                                pnmMotherListRealmModel.setmRiskStatus(jsonObject.getString("mRiskStatus"));
                                pnmMotherListRealmModel.setmEDD(jsonObject.getString("mEDD"));
                                pnmMotherListRealmModel.setmHusbandName(jsonObject.getString("mHusbandName"));
                                pnmMotherListRealmModel.setmHusbandMobile(jsonObject.getString("mHusbandMobile"));
                                pnmMotherListRealmModel.setvLongitude(jsonObject.getString("vLongitude"));
                                pnmMotherListRealmModel.setvLatitude(jsonObject.getString("vLatitude"));
                                pnmMotherListRealmModel.setCurrentMonth(Integer.parseInt(jsonObject.getString("currentMonth")));
                                pnmMotherListRealmModel.setmLMP(jsonObject.getString("mLMP"));
                                pnmMotherListRealmModel.setmVillage(jsonObject.getString("mVillage"));
                                pnmMotherListRealmModel.setNextVisit(jsonObject.getString("nextVisit"));
                                pnmMotherListRealmModel.setGestAge(jsonObject.getString("gestAge"));
                                pnmMotherListRealmModel.setmWeight(jsonObject.getString("mWeight"));
                                pnmMotherListRealmModel.setDeleveryDate(jsonObject.getString("deleveryDate"));
                                pnmMotherListRealmModel.setdBirthDetails(jsonObject.getString("dBirthDetails"));
                                pnmMotherListRealmModel.setdBirthWeight(jsonObject.getString("dBirthWeight"));
                                pnmMotherListRealmModel.setMeturityWeek(jsonObject.getString("meturityWeek"));
                                pnmMotherListRealmModel.setPnVisit(jsonObject.getString("pnVisit"));
                                pnmMotherListRealmModel.setUserType(jsonObject.getString("userType"));

                            }
                            realm.commitTransaction();

                        }

                    }
//                                    motherListsViews.hideProgress();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginError(error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode", vhnCode);
                params.put("vhnId", vhnId);

                Log.e("params--->", params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "admin" + ":" + "1234";
                String base64EncodedCredentials = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                    base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                }
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

    @Override
    public void getPNMotherRecordsList(final String vhnCode, final String vhnId) {

        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_MOTHERS_PN_RECORDS;
        Log.d("Log in check Url--->", url);
        Log.d("vhnCode--->", vhnCode);
        Log.d("vhnId--->", vhnId);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode", vhnCode);
                params.put("vhnId", vhnId);

                Log.d("params--->", params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "admin" + ":" + "1234";
                String base64EncodedCredentials = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                    base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                }
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

    @Override
    public void getSelectedMother(final String vhnCode, final String vhnId, final String mid) {

        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.MOTHER_DETAILS_TRACKING;
        Log.e("Log in check Url--->", url);
        Log.e("vhnCode--->", vhnCode);
        Log.e("vhnId--->", vhnId);
        Log.e("mid--->", mid);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("SelectedMother", "Success" + response);
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SelectedMother", "error" + error.toString());

                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(error.toString());
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
                String base64EncodedCredentials = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                    base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                }
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

    @Override
    public void getSelectedPNMother(final String mid) {
        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.VHN_MOTHER_DELVERY_INFO;
//        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_MOTHERS_AN_RECORDS;
        Log.e("Log in check Url--->", url);
        Log.e("mid--->", mid);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("SelectedMother", "Success" + response);
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SelectedMother", "Success" + error);

                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mid", mid);

                Log.e("params--->", params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "admin" + ":" + "1234";
                String base64EncodedCredentials = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                    base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                }
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

    @Override
    public void getSelectedSosMother(final String vhnId, final String vhnCode, final String sosId) {


        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_SOS_DETAILS;
        Log.e("Log in check Url--->", url);
        Log.e("vhnId--->", vhnId);
        Log.e("vhnCode--->", vhnCode);
        Log.e("sosId--->", sosId);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SelectedMother", "Success" + response);
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginError(error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnId", vhnId);
                params.put("vhnCode", vhnCode);
                params.put("sosId", sosId);

                Log.d("params--->", params.toString());

                return params;
            }

            @RequiresApi(api = Build.VERSION_CODES.FROYO)
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

    @Override
    public void closeSosAlertSelectedMother(final String vhnId, final String vhnCode, final String sosId) {


        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_SOS_MOTHER__DETAILS_CLOSED;
        Log.d("Log in check Url--->", url);
        Log.d("vhnId--->", vhnId);
        Log.d("vhnCode--->", vhnCode);
        Log.d("sosId--->", sosId);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SelectedMother", "Success" + response);
                motherListsViews.hideProgress();
                motherListsViews.showAlertClosedSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                motherListsViews.showAlertClosedError(error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnId", vhnId);
                params.put("vhnCode", vhnCode);
                params.put("sosId", sosId);

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
        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(context).getRequestQueue().getCache().remove(url);

    }

    @Override
    public void getTremAndPreTremMothersList(final String vhnCode, final String vhnId) {

        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_MOTHERS_TREM_PRE_TREM_MOTHERS_LIST;
        Log.e("Log in check Url--->", url);
        Log.e("vhnCode--->", vhnCode);
        Log.e("vhnId--->", vhnId);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                motherListsViews.hideProgress();
                Log.e("response --->", response);

                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                Log.e("error --->", error.toString());

                motherListsViews.showLoginSuccess(error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode", vhnCode);
                params.put("vhnId", vhnId);

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

    @Override
    public void getTremAndPreTremMothers(final String vhnId, final String VhnCode, final String mid) {

        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_MOTHERS_TREM_PRE_TREM_MOTHERS;
        Log.d("Log in check Url--->", url);
        Log.d("mid--->", mid);
        Log.d("vhnId--->", vhnId);
        Log.d("VhnCode--->", VhnCode);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SelectedMother", "Success" + response);
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mid", mid);
                params.put("vhnCode", VhnCode);
                params.put("vhnId", vhnId);

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
        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(context).getRequestQueue().getCache().remove(url);
    }

    @Override
    public void getANTTMotherList(final String vhnCode, final String vhnId) {

        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_MOTHERS_AN_TT1_DUELIST;
        Log.d("Log in check Url--->", url);
        Log.d("vhnId--->", vhnId);
        Log.d("VhnCode--->", vhnCode);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SelectedMother", "Success" + response);
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode", vhnCode);
                params.put("vhnId", vhnId);
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
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(context).getRequestQueue().getCache().remove(url);
    }

    @Override
    public void getANTT2MotherList(final String vhnCode, final String vhnId) {

        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_MOTHERS_AN_TT2_DUELIST;
        Log.e("Log in check Url--->", url);
        Log.d("vhnId--->", vhnId);
        Log.d("VhnCode--->", vhnCode);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SelectedMother", "Success" + response);
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode", vhnCode);
                params.put("vhnId", vhnId);

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
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(context).getRequestQueue().getCache().remove(url);
    }

    @Override
    public void getPNHBNCDUEMotherList(final String vhnCode, final String vhnId, final String tempCount) {

        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_MOTHERS_PN_HBNC_DUELIST;
        Log.d("Log in check Url--->", url);
        Log.d("vhnId--->", vhnId);
        Log.d("VhnCode--->", vhnCode);
        Log.d("tempCount--->", tempCount);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SelectedMother", "Success" + response);
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode", vhnCode);
                params.put("vhnId", vhnId);
                params.put("tempCount", "1");

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
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(context).getRequestQueue().getCache().remove(url);
    }

    @Override
    public void getMigratedMothersList(final String vhnCode, final String vhnId) {
        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.MIGRATED_MOTHERS_LIST;
        Log.d("Log in check Url--->", url);
        Log.d("vhnId--->", vhnId);
        Log.d("VhnCode--->", vhnCode);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SelectedMother", "Success" + response);
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode", vhnCode);
                params.put("vhnId", vhnId);

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
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(context).getRequestQueue().getCache().remove(url);
    }


    @Override
    public void getMigratedMothersList1(final String vhnCode, final String vhnId) {
        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.MIGRATED_MOTHERS_LIST_1;
        Log.d("Log in check Url--->", url);
        Log.d("vhnId--->", vhnId);
        Log.d("VhnCode--->", vhnCode);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SelectedMother", "Success" + response);
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                motherListsViews.hideProgress();
                motherListsViews.showLoginSuccess(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode", vhnCode);
                params.put("vhnId", vhnId);

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
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(context).getRequestQueue().getCache().remove(url);
    }


}
