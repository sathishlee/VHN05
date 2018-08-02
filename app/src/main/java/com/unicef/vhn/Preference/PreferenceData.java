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
    private boolean highRiskStatus;
    private boolean descendingStatus;

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
        return sharedPreferences.getBoolean(AppConstants.IS_LOGIN, Boolean.parseBoolean(""));
    }

    public void storeUserInfo(String vhnName, String vhnCode, String vhnId, String vphoto) {

        sharedPreferences.edit().putString(AppConstants.VHN_ID,vhnId).commit();
        sharedPreferences.edit().putString(AppConstants.VHN_NAME,vhnName).commit();
        sharedPreferences.edit().putString(AppConstants.VHN_CODE,vhnCode).commit();
        sharedPreferences.edit().putString(AppConstants.VHN_PHOTO,vphoto).commit();


        Log.e("VHN_ID", sharedPreferences.getString(AppConstants.VHN_ID, ""));
        Log.e("VHN_NAME", sharedPreferences.getString(AppConstants.VHN_NAME, ""));
        Log.e("VHN_CODE", sharedPreferences.getString(AppConstants.VHN_CODE, ""));

    }

    public void setPicmeId(String mPicmeId) {
        sharedPreferences.edit().putString(AppConstants.PICME_ID, mPicmeId).commit();
    }

    public String getPicmeId() {
        return sharedPreferences.getString(AppConstants.PICME_ID, "");
    }

    public void setMId(String mid) {
        sharedPreferences.edit().putString(AppConstants.M_ID, mid).commit();
    }

    public String getMId() {
        return sharedPreferences.getString(AppConstants.M_ID, "");
    }

    public void setDeviceId(String deviceId) {
        sharedPreferences.edit().putString(AppConstants.DEVICE_ID, deviceId).commit();
    }

    public String getDeviceId() {
        return sharedPreferences.getString(AppConstants.DEVICE_ID, "");
    }

    public String getVhnPhoto(){
        return sharedPreferences.getString(AppConstants.VHN_PHOTO,"");
    }
    public void setVhnPhoto(String vPhoto){
        sharedPreferences.edit().putString(AppConstants.VHN_PHOTO, vPhoto).commit();
    }

    public String getVhnId() {
        return sharedPreferences.getString(AppConstants.VHN_ID, "");
    }

    public String getVhnName() {
        return sharedPreferences.getString(AppConstants.VHN_NAME, "");
    }

    public String getVhnCode() {
        return sharedPreferences.getString(AppConstants.VHN_CODE, "");
    }

    public void setTodayVisitCount(String strTodayVisitCount) {
        sharedPreferences.edit().putString(AppConstants.TODAY_VISIT_COUNT, strTodayVisitCount).commit();
    }

    public void setNotificationCount(String strTodayVisitCount) {
        sharedPreferences.edit().putString(AppConstants.NOTIFICATION_COUNT, strTodayVisitCount).commit();
    }

    public void setTodayvisitCount(String strTodayVisitCount) {
        sharedPreferences.edit().putString(AppConstants.TODAY_VISIT_COUNT, strTodayVisitCount).commit();
    }

    public String getTodayVisitCount() {
        return sharedPreferences.getString(AppConstants.TODAY_VISIT_COUNT, "");
    }

    public String getNotificationCount() {
        return sharedPreferences.getString(AppConstants.NOTIFICATION_COUNT, "");
    }

    public void storeDid(String strDid) {
        sharedPreferences.edit().putString(AppConstants.DELIVERY_ID, strDid).commit();
    }


    public void setHighRiskStatus(boolean highRiskStatus) {
        sharedPreferences.edit().putBoolean( AppConstants.ISHIGHRISK, highRiskStatus).commit();

    } public boolean getHighRiskStatus() {
        return sharedPreferences.getBoolean(AppConstants.ISHIGHRISK, Boolean.parseBoolean(""));

    }

    public void setDescendingStatus(boolean descendingStatus) {
        sharedPreferences.edit().putBoolean(AppConstants.ISDECENDING, descendingStatus).commit();

    }
    public boolean getDescendingStatus() {
        return sharedPreferences.getBoolean(AppConstants.ISDECENDING, Boolean.parseBoolean(""));
     }

     public void setVillageName(String villageName) {
        sharedPreferences.edit().putString(AppConstants.VILLAGENAME, villageName).commit();

    }
    public String getVillageName() {
        return sharedPreferences.getString(AppConstants.VILLAGENAME,  "");
     }

     public void setTermister(String villageName) {
        sharedPreferences.edit().putString(AppConstants.TERMISTER, villageName).commit();

    }
    public String getTermister() {
        return sharedPreferences.getString(AppConstants.TERMISTER, "");
     }

    public void setTermisterPosition(int termisterPosition) {
        sharedPreferences.edit().putString(AppConstants.TERMISTER_POSITION, String.valueOf(termisterPosition)).commit();
    }
    public int getTermisterPosition() {
        return Integer.parseInt(sharedPreferences.getString(AppConstants.TERMISTER_POSITION, ""));
    }

    public void setVillageNamePosition(int villageNamePosition) {
        sharedPreferences.edit().putString(AppConstants.VILLAGENAME_POSITION, String.valueOf(villageNamePosition)).commit();


    }
    public int getVillageNamePosition() {
        return Integer.parseInt(sharedPreferences.getString(AppConstants.VILLAGENAME_POSITION,""));

    }

    public String getphoto() {
        return sharedPreferences.getString(AppConstants.VHN_PHOTO, "");

    }

    public void setPhoto(String vphoto) {
        sharedPreferences.edit().putString(AppConstants.VHN_PHOTO, vphoto).commit();
    }

    public void setFilterStatus(boolean isfillter) {
        sharedPreferences.edit().putBoolean("isfillter", isfillter).commit();

    }
    public boolean getFilterStatus() {
                return sharedPreferences.getBoolean("isfillter", Boolean.parseBoolean(""));

    }


    public void setSharePrefrenceLocale(String locale) {
        sharedPreferences.edit().putString(AppConstants.LANGUAGE,locale).commit();
    }

    public String getSharePrefrenceLocale() {
        return sharedPreferences.getString(AppConstants.LANGUAGE,"");

    }
}
