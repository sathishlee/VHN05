package com.unicef.vhn.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.unicef.vhn.constant.AppConstants;

/**
 * Created by sathish on 3/20/2018.
 */

public class PreferenceData {
    SharedPreferences sharedPreferences;

    public PreferenceData(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
    }
    public SharedPreferences getPreference() {
        return sharedPreferences;
    }


    public void setLogin(boolean isLogin) {
        sharedPreferences.edit().putBoolean(AppConstants.IS_LOGIN, isLogin).commit();
    }

    public boolean getLogin() {
        return  sharedPreferences.getBoolean(AppConstants.IS_LOGIN, Boolean.parseBoolean(""));
    }

    public void storeUserInfo(String vhnName, String vhnCode, String vhnId) {
        sharedPreferences.edit().putString(AppConstants.VHN_ID,vhnId).commit();
        sharedPreferences.edit().putString(AppConstants.VHN_NAME,vhnName).commit();
        sharedPreferences.edit().putString(AppConstants.VHN_CODE,vhnCode).commit();

        Log.e("VHN_ID",sharedPreferences.getString(AppConstants.VHN_ID,""));
        Log.e("VHN_NAME",sharedPreferences.getString(AppConstants.VHN_NAME,""));
        Log.e("VHN_CODE",sharedPreferences.getString(AppConstants.VHN_CODE,""));

    }

    public void setDeviceId(String deviceId) {
        sharedPreferences.edit().putString(AppConstants.DEVICE_ID, deviceId).commit();
    }

    public String getDeviceId(){
        return sharedPreferences.getString(AppConstants.DEVICE_ID,"");
    }


    public String getVhnId(){
        return sharedPreferences.getString(AppConstants.VHN_ID,"");
    }  public String getVhnName(){
        return sharedPreferences.getString(AppConstants.VHN_NAME,"");
    }  public String getVhnCode(){
        return sharedPreferences.getString(AppConstants.VHN_CODE,"");
    }

    public void setTodayVisitCount(String strTodayVisitCount) {
        sharedPreferences.edit().putString(AppConstants.TODAY_VISIT_COUNT, strTodayVisitCount).commit();
    }
    public String getTodayVisitCount(){
        return sharedPreferences.getString(AppConstants.TODAY_VISIT_COUNT,"");
    }
}
