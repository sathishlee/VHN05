package com.unicef.vhn.Presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.interactor.ProfileInteractor;
import com.unicef.vhn.utiltiy.VolleyMultipartRequest;
import com.unicef.vhn.view.ProfileViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ProfilePresenter implements ProfileInteractor {

private ProfileViews profileViews;
    private Activity activity;

    public ProfilePresenter(ProfileViews profileViews, Activity activity){

        this.profileViews = profileViews;
        this.activity = activity;

    }


    @Override
    public void uploadUserProfilePhoto(final String vhnCode, final String vhnId, final Bitmap bitmap) {
        profileViews.showProgress();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                Apiconstants.BASE_URL + Apiconstants.POST_UPLOAD_PROFILE_PHOTO,
                new Response.Listener<NetworkResponse>() {


                    @Override
                    public void onResponse(NetworkResponse response) {
                        profileViews.hideProgress();
                        profileViews.successUploadPhoto(response.toString());
//                        try {
//                            JSONObject obj = new JSONObject(new String(response.data));
//                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                profileViews.hideProgress();
                profileViews.errorUploadPhoto(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("vhnCode", vhnCode);
                params.put("vhnId", vhnId);
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

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("vphoto", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }


            private byte[] getFileDataFromDrawable(Bitmap bitmap) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
        };

        //adding the request to volley
//        Volley.newRequestQueue(this).add(volleyMultipartRequest);
        VolleySingleton.getInstance(activity).addToRequestQueue(volleyMultipartRequest);
    }

    @Override
    public void getVHNProfile(final String vhnCode, final String vhnId) {
        profileViews.showProgress();

        String url = Apiconstants.BASE_URL+Apiconstants.VHN_PROFILE;
        Log.d("VHNID-->",vhnId);
        Log.d("VHNCODE-->",vhnCode);
        Log.d("URL-->",url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Log in success",response);
                profileViews.hideProgress();
                profileViews.successViewProfile(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Log in Error",error.toString());
                profileViews.hideProgress();
                profileViews.errorViewProfile(error.toString());
            }
        }){


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vhnCode",vhnCode);
                params.put("vhnId",vhnId);

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
    }

}

