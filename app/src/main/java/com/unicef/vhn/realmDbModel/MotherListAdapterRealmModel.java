package com.unicef.vhn.realmDbModel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class MotherListAdapterRealmModel extends RealmObject{

    @SerializedName("id")
    private String id;
    @SerializedName("mLongitude")
    private String mLongitude;
    @SerializedName("mLatitude")
    private String mLatitude;
    @SerializedName("vhnId")
    private String vhnId;
    @SerializedName("mid")
    private String mid;
    @SerializedName("mPicmeId")
    private String mPicmeId;
    @SerializedName("mName")
    private String mName;
    @SerializedName("motherType")
    private String motherType;
    @SerializedName("vLatitude")
    private String vLatitude;
    @SerializedName("vLongitude")
    private String vLongitude;
    @SerializedName("mMotherMobile")
    private String mMotherMobile;
    @SerializedName("mPhoto")
    private String mPhoto;
    @SerializedName("pnId")
    private String pnId;
/*
    public MotherListAdapterRealmModel(String id, String mLongitude, String mLatitude, String vhnId, String mid, String mPicmeId, String mName, String motherType, String vLatitude, String vLongitude, String mMotherMobile, String mPhoto, String pnId) {
        this.id = id;
        this.mLongitude = mLongitude;
        this.mLatitude = mLatitude;
        this.vhnId = vhnId;
        this.mid = mid;
        this.mPicmeId = mPicmeId;
        this.mName = mName;
        this.motherType = motherType;
        this.vLatitude = vLatitude;
        this.vLongitude = vLongitude;
        this.mMotherMobile = mMotherMobile;
        this.mPhoto = mPhoto;
        this.pnId = pnId;
    }*/

    public String getmPhoto(){return mPhoto;}
    public void setmPhoto(String mPhoto){this.mPhoto = mPhoto;}


    public String getvLatitude(){return vLatitude;}
    public void setvLatitude(String vLatitude){this.vLatitude = vLatitude;}

    public String getmMotherMobile(){return mMotherMobile;}
    public void setmMotherMobile(String mMotherMobile){this.mMotherMobile = mMotherMobile;}

    public String getvLongitude(){return vLongitude;}
    public void setvLongitude(String vLongitude){this.vLongitude = vLongitude;}

    public String getMotherType() {
        return motherType;
    }

    public void setMotherType(String motherType) {
        this.motherType = motherType;
    }


    public String getPnId() {
        return pnId;
    }

    public void setPnId(String pnId) {
        this.pnId = pnId;
    }


    public String getMLongitude() {
        return mLongitude;
    }

    public void setMLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getMLatitude() {
        return mLatitude;
    }

    public void setMLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getVhnId() {
        return vhnId;
    }

    public void setVhnId(String vhnId) {
        this.vhnId = vhnId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMPicmeId() {
        return mPicmeId;
    }

    public void setMPicmeId(String mPicmeId) {
        this.mPicmeId = mPicmeId;
    }

    public String getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }
}
