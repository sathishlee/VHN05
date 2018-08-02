package com.unicef.vhn.Presenter;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.interactor.HomeInteractor;
import com.unicef.vhn.realmDbModel.DashBoardRealmModel;
import com.unicef.vhn.view.MotherListsViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sathish on 3/22/2018.
 */

public class HomePresenter implements HomeInteractor {
    String TAG = HomePresenter.class.getSimpleName();
    Activity activity;
    MotherListsViews motherListsViews;
    Realm realm;
    PreferenceData preferenceData;

    public HomePresenter(Activity activity, MotherListsViews motherListsViews, Realm realm) {
        this.activity = activity;
        this.motherListsViews = motherListsViews;
        this.realm = RealmController.with(activity).getRealm();
        preferenceData = new PreferenceData(activity);
    }


    @Override
    public void getDashBoard(final String vhnCode, final String vhnId) {
        motherListsViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mJsnobject = new JSONObject(response);
                    String status = mJsnobject.getString("status");
                    String message = mJsnobject.getString("message");
                    if (status.equalsIgnoreCase("1")) {
                        RealmResults<DashBoardRealmModel> DashBoardRealmResult = realm.where(DashBoardRealmModel.class).findAll();
                        if (DashBoardRealmResult.size() != 0) {
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(DashBoardRealmModel.class);
                                }
                            });
                        } else {
                            Log.e(TAG, "Realm size  ---->" + DashBoardRealmResult.size() + "");
                        }
                        realm.beginTransaction();
                        DashBoardRealmModel dashBoardRealmModel = realm.createObject(DashBoardRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database
                        dashBoardRealmModel.setMothersCount(Integer.parseInt(mJsnobject.getString("mothersCount")));
//                        dashBoardRealmModel.setImmCount(Integer.parseInt(mJsnobject.getString("immCount")));
                        dashBoardRealmModel.setRiskMothersCount(Integer.parseInt(mJsnobject.getString("riskMothersCount")));
                        dashBoardRealmModel.setInfantCount(Integer.parseInt(mJsnobject.getString("immCount")));
                        dashBoardRealmModel.setSosCount(Integer.parseInt(mJsnobject.getString("sosCount")));
                        dashBoardRealmModel.setANTT1(Integer.parseInt(mJsnobject.getString("ANTT1")));
                        dashBoardRealmModel.setANTT2(Integer.parseInt(mJsnobject.getString("ANTT2")));
                        dashBoardRealmModel.setPnhbncCount(Integer.parseInt(mJsnobject.getString("pnhbncCount")));

                        dashBoardRealmModel.setANMothersCount(Integer.parseInt(mJsnobject.getString("ANMothersCount")));
                        dashBoardRealmModel.setANMotherRiskCount(Integer.parseInt(mJsnobject.getString("ANMotherRiskCount")));
                        dashBoardRealmModel.setPNMotherCount(Integer.parseInt(mJsnobject.getString("PNMotherCount")));
                        dashBoardRealmModel.setTermsCount(Integer.parseInt(mJsnobject.getString("termsCount")));

                        JSONObject jobj__phcDetails = mJsnobject.getJSONObject("phcDetails");
                        dashBoardRealmModel.setVhnName(jobj__phcDetails.getString("vhnName"));
                        dashBoardRealmModel.setPhcName(jobj__phcDetails.getString("phcName"));
                        dashBoardRealmModel.setFacilityName(jobj__phcDetails.getString("facilityName"));
                        dashBoardRealmModel.setBlock(jobj__phcDetails.getString("block"));
                        dashBoardRealmModel.setDistrict(jobj__phcDetails.getString("District"));
                        if (jobj__phcDetails.getString("vphoto").equalsIgnoreCase("null")) {
                            dashBoardRealmModel.setVphoto("");
                        }
                        dashBoardRealmModel.setVphoto(jobj__phcDetails.getString("vphoto"));
                        preferenceData.setPhoto(jobj__phcDetails.getString("vphoto"));

                        realm.commitTransaction();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);
    }
}
