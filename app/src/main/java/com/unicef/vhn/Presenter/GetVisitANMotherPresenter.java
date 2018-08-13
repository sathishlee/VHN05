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
import com.unicef.vhn.fragment.GetVisitReportsPresenter;
import com.unicef.vhn.interactor.VisitANMotherInteractor;
import com.unicef.vhn.view.VisitANMotherViews;
import com.unicef.vhn.volleyservice.VolleySingleton;
import io.realm.Realm;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathish on 3/23/2018.
 */

public class GetVisitANMotherPresenter implements VisitANMotherInteractor {
    String TAG = GetVisitANMotherPresenter.class.getSimpleName();
    Context activity;
    VisitANMotherViews visitANMotherViews;
    Realm realm;

    public GetVisitANMotherPresenter(Context activity, VisitANMotherViews visitANMotherViews, Realm realm) {
        Log.e(TAG,"Api name  --> GetVisitANMotherPresenter cons");

        this.activity = activity;
        this.visitANMotherViews = visitANMotherViews;
        this.realm = realm;
    }

    @Override
    public void getVisitANMotherRecords(final String vhnCode, final String vhnId, final String mid) {
        visitANMotherViews.showProgress();

        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_MOTHERS_AN_RECORDS;
        Log.e(TAG, "AN Report -----> "+url+"\n vhnCode"+ vhnCode +"\n vhnId"+vhnId+ "\n mid"+mid);

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(GetVisitReportsPresenter.class.getSimpleName(), "ANMother Records  Response success" + response);

                visitANMotherViews.hideProgress();
                visitANMotherViews.showANVisitRecordsSuccess(response);
/*
                try {
                    JSONObject mJsnobject = new JSONObject(response);
                    String status = mJsnobject.getString("status");
                    String message = mJsnobject.getString("message");
                    if (status.equalsIgnoreCase("1")) {

                        if (realm.isInTransaction()) {
                            realm.cancelTransaction();
                        }
                        JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
                        RealmResults<ANMVisitRealmModel> motherListAdapterRealmModel = realm.where(ANMVisitRealmModel.class).findAll();
                        Log.e(mothers.class.getSimpleName(), "already anm visit data available is size" + motherListAdapterRealmModel.size() + "");

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Log.e(mothers.class.getSimpleName(), "delete ANMother visit realm records");

                                realm.delete(ANMVisitRealmModel.class);
                            }
                        });

                        if (jsonArray.length() != 0) {
                            Log.e(mothers.class.getSimpleName(), "beginTransaction ANMother visit realm records");
                            ANMVisitRealmModel mhealthRecordResponseModel;

                            realm.beginTransaction();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.e(mothers.class.getSimpleName(), " ANMVisitRealmModel realm talbe create");

                                mhealthRecordResponseModel = realm.createObject(ANMVisitRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.e(mothers.class.getSimpleName(), " ANMVisitRealmModel get data from api call" + i + "th visit details" + jsonObject.getString("picmeId"));
                                Log.e(mothers.class.getSimpleName(), " ANMVisitRealmModel get data from api call" + i + "th visit details" + jsonObject.getString("vid"));

                                mhealthRecordResponseModel.setVDate(jsonObject.getString("vDate"));
                                mhealthRecordResponseModel.setVFacility(jsonObject.getString("vFacility"));
                                //              mhealthRecordResponseModel.setMLongitude(jsonObject.getString("mLongitude"));
                                //              mhealthRecordResponseModel.setMLatitude(jsonObject.getString("mLatitude"));
                                mhealthRecordResponseModel.setMotherStatus(jsonObject.getString("motherStatus"));
                                mhealthRecordResponseModel.setMotherCloseDate(jsonObject.getString("motherCloseDate"));
                                mhealthRecordResponseModel.setMRiskStatus(jsonObject.getString("mRiskStatus"));
                                mhealthRecordResponseModel.setMEDD(jsonObject.getString("mEDD"));
                                mhealthRecordResponseModel.setMLMP(jsonObject.getString("mLMP"));
                                mhealthRecordResponseModel.setPhcId(jsonObject.getString("phcId"));
                                mhealthRecordResponseModel.setAwwId(jsonObject.getString("awwId"));
                                mhealthRecordResponseModel.setVhnId(jsonObject.getString("vhnId"));
                                mhealthRecordResponseModel.setMasterId(jsonObject.getString("masterId"));
                                mhealthRecordResponseModel.setVTSH(jsonObject.getString("vTSH"));
                                mhealthRecordResponseModel.setUsgPlacenta(jsonObject.getString("usgPlacenta"));
                                mhealthRecordResponseModel.setUsgLiquor(jsonObject.getString("usgLiquor"));
                                mhealthRecordResponseModel.setUsgGestationSac(jsonObject.getString("usgGestationSac"));
                                mhealthRecordResponseModel.setUsgFetus(jsonObject.getString("usgFetus"));
                                mhealthRecordResponseModel.setVAlbumin(jsonObject.getString("vAlbumin"));
                                mhealthRecordResponseModel.setVUrinSugar(jsonObject.getString("vUrinSugar"));
                                mhealthRecordResponseModel.setVGTT(jsonObject.getString("vGTT"));
                                mhealthRecordResponseModel.setVPPBS(jsonObject.getString("vPPBS"));
                                mhealthRecordResponseModel.setVFBS(jsonObject.getString("vFBS"));
                                mhealthRecordResponseModel.setVRBS(jsonObject.getString("vRBS"));
                                mhealthRecordResponseModel.setVFHS(jsonObject.getString("vFHS"));
                                mhealthRecordResponseModel.setVHemoglobin(jsonObject.getString("vHemoglobin"));
                                mhealthRecordResponseModel.setVBodyTemp(jsonObject.getString("vBodyTemp"));
                                mhealthRecordResponseModel.setVPedalEdemaPresent(jsonObject.getString("vPedalEdemaPresent"));
                                mhealthRecordResponseModel.setVFundalHeight(jsonObject.getString("vFundalHeight"));
                                mhealthRecordResponseModel.setVEnterWeight(jsonObject.getString("vEnterWeight"));
                                mhealthRecordResponseModel.setVEnterPulseRate(jsonObject.getString("vEnterPulseRate"));
                                mhealthRecordResponseModel.setVClinicalBPDiastolic(jsonObject.getString("vClinicalBPDiastolic"));
                                mhealthRecordResponseModel.setVClinicalBPSystolic(jsonObject.getString("vClinicalBPSystolic"));
//                mhealthRecordResponseModel.setVAnyComplaintsOthers(jsonObject.getString("vAnyComplaintsOthers"));
                                mhealthRecordResponseModel.setVAnyComplaints(jsonObject.getString("vAnyComplaints"));
//                mhealthRecordResponseModel.setVFacilityOthers(jsonObject.getString("vFacilityOthers"));
                                mhealthRecordResponseModel.setVtypeOfVisit(jsonObject.getString("vtypeOfVisit"));
                                mhealthRecordResponseModel.setPicmeId(jsonObject.getString("picmeId"));
                                mhealthRecordResponseModel.setMid(jsonObject.getString("mid"));
                                mhealthRecordResponseModel.setVisitId(jsonObject.getString("visitId"));
                                mhealthRecordResponseModel.setVDate(jsonObject.getString("vDate"));
                                mhealthRecordResponseModel.setVid(jsonObject.getString("vid"));

//                        motherPrimaryRegisterPresenter.getAllMotherPrimaryRegistration(motherListAdapterRealmModel.get(i).getPicmeId());

                            }
                            realm.commitTransaction();
                            Log.e(mothers.class.getSimpleName(), "beginTransaction ANMother visit realm records");

                        } else {
                            Log.e(mothers.class.getSimpleName(), "ANMother visit records NOt Found");

                        }
                    } else {
                        Log.e(mothers.class.getSimpleName(), "ANMother visit records" + message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                visitANMotherViews.hideProgress();
                visitANMotherViews.showANVisitRecordsFailiur(error.toString());
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
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);
    }

    @Override
    public void getVisitPNMotherRecords(final String vhnCode, final String vhnId, final String mid) {
        visitANMotherViews.showProgress();
        String url = Apiconstants.BASE_URL + Apiconstants.DASH_BOARD_MOTHERS_PN_VISIT_RECORDS;
        Log.e(TAG,"PN Report ----->"+ url+"\n vhnCode"+ vhnCode +"\n vhnId"+vhnId+ "\n mid"+mid);


        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                visitANMotherViews.hideProgress();
                visitANMotherViews.showPNVisitRecordsSuccess(response);
// Do Not Delete this code, this code stored  pn visit records into realm db
                /*try {
                    JSONObject mJsnobject = new JSONObject(response);
                    String status = mJsnobject.getString("status");
                    String message = mJsnobject.getString("message");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = mJsnobject.getJSONArray("pnMothersVisit");
                        RealmResults<PNMVisitRealmModel> motherListAdapterRealmModel = realm.where(PNMVisitRealmModel.class).findAll();

                        if (realm.isInTransaction()) {
                            realm.cancelTransaction();
                        }

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                realm.delete(PNMVisitRealmModel.class);
                            }
                        });

                        if (jsonArray.length() != 0) {

                            PNMVisitRealmModel pnmVisitRealmModel;

                            realm.beginTransaction();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                pnmVisitRealmModel = realm.createObject(PNMVisitRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database
                                Log.e(mothers.class.getSimpleName(), "PNMVisitRealmModel visit realm table created");

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                pnmVisitRealmModel.setMAge(jsonObject.getString("mAge"));
                                pnmVisitRealmModel.setMWeight(jsonObject.getString("mWeight"));
                                pnmVisitRealmModel.setPnId(jsonObject.getString("pnId"));
                                pnmVisitRealmModel.setMid(jsonObject.getString("mid"));
                                pnmVisitRealmModel.setPicmeId(jsonObject.getString("picmeId"));
                                pnmVisitRealmModel.setPnVisitNo(jsonObject.getString("pnVisitNo"));
                                pnmVisitRealmModel.setPnDueDate(jsonObject.getString("pnDueDate"));
                                pnmVisitRealmModel.setPnCareProvidedDate(jsonObject.getString("pnCareProvidedDate"));
                                pnmVisitRealmModel.setPnPlace(jsonObject.getString("pnPlace"));
                                pnmVisitRealmModel.setPnAnyComplaints(jsonObject.getString("pnAnyComplaints"));
                                pnmVisitRealmModel.setPnBPSystolic(jsonObject.getString("pnBPSystolic"));
                                pnmVisitRealmModel.setPnPulseRate(jsonObject.getString("pnPulseRate"));
                                pnmVisitRealmModel.setPnTemp(jsonObject.getString("pnTemp"));
                                pnmVisitRealmModel.setPnEpistomyTear(jsonObject.getString("pnEpistomyTear"));
                                pnmVisitRealmModel.setPnPVDischarge(jsonObject.getString("pnPVDischarge"));
                                pnmVisitRealmModel.setPnBreastFeedingReason(jsonObject.getString("pnBreastFeedingReason"));
                                pnmVisitRealmModel.setPnBreastExamination(jsonObject.getString("pnBreastExamination"));
                                pnmVisitRealmModel.setPnOutCome(jsonObject.getString("pnOutCome"));
                                pnmVisitRealmModel.setCWeight(jsonObject.getString("cWeight"));
                                pnmVisitRealmModel.setCTemp(jsonObject.getString("cTemp"));
                                pnmVisitRealmModel.setCUmbilicalStump(jsonObject.getString("cUmbilicalStump"));
                                pnmVisitRealmModel.setCCry(jsonObject.getString("cCry"));
                                pnmVisitRealmModel.setCEyes(jsonObject.getString("cEyes"));
                                pnmVisitRealmModel.setCSkin(jsonObject.getString("cSkin"));
                                pnmVisitRealmModel.setCBreastFeeding(jsonObject.getString("cBreastFeeding"));
                                pnmVisitRealmModel.setCBreastFeedingReason(jsonObject.getString("cBreastFeedingReason"));
                                pnmVisitRealmModel.setCOutCome(jsonObject.getString("cOutCome"));

//                        motherDeliveryPresenter.deliveryDetails(jsonObject.getString("picmeId"),jsonObject.getString("mid"));

                            }
                            realm.commitTransaction();
                            Log.e(mothers.class.getSimpleName(), "commitTransaction ANMother visit realm records");

                        } else {
                            Log.e(mothers.class.getSimpleName(), "showPNVisitRecordsSuccess RECORD NOT FOUND");

                        }
                    } else {
                        Log.e(mothers.class.getSimpleName(), "showPNVisitRecords success" + message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PN Visit report error", "error" + error);

//                visitANMotherViews.hideProgress();
                visitANMotherViews.showPNVisitRecordsFailiur(error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("vhnCode", vhnCode);
                params.put("vhnId", vhnId);
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
                Log.e("Credentials ", "Basic " + base64EncodedCredentials.toString());

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
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);

    }
}
