package com.unicef.vhn.Presenter;

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
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.interactor.NewRegMotherInteractor;
import com.unicef.vhn.view.NewRegMotherView;
import com.unicef.vhn.view.NotificationViews;
import com.unicef.vhn.volleyservice.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class NewRegMotherPresenter implements NewRegMotherInteractor {
    Context context;
    NewRegMotherView newRegMotherView;

    public NewRegMotherPresenter(Context context, NewRegMotherView newRegMotherView) {
        this.context = context;
        this.newRegMotherView = newRegMotherView;
    }

    @Override
    public void getAllRegisterMother(final String vhnId) {
        String url = Apiconstants.BASE_URL + Apiconstants.NEW_REGISTERED_MOTHERS_LIST;
        Log.e("Url--->", url);
        Log.e("vhnId--->", vhnId);


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                newRegMotherView.hideProgress();

                newRegMotherView.showRegMotherSuccess(String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                notificationViews.hideProgress();
                newRegMotherView.showRegMotherError(error.toString());
            }
        }) {
            @RequiresApi(api = Build.VERSION_CODES.FROYO)
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
                params.put("vhnId", vhnId);

                Log.e("params--->", params.toString());

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
        VolleySingleton.getInstance(context).getRequestQueue().getCache().remove(url);

    }
}
