package com.unicef.vhn.realmDbModel;

import io.realm.RealmObject;

public class PNHBNCMotherListRealmModel extends RealmObject {
    private String mLongitude;
    private String mLatitude;
    private String vhnId;
    private String mid;
    private String mPicmeId;
    private String mName;
    private String motherType;
    private String vLatitude;
    private String vLongitude;
    private String mMotherMobile;
    private String mPhoto;
    private String mLMP;

    public String getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(String mLatitude) {
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

    public String getmPicmeId() {
        return mPicmeId;
    }

    public void setmPicmeId(String mPicmeId) {
        this.mPicmeId = mPicmeId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getMotherType() {
        return motherType;
    }

    public void setMotherType(String motherType) {
        this.motherType = motherType;
    }

    public String getvLatitude() {
        return vLatitude;
    }

    public void setvLatitude(String vLatitude) {
        this.vLatitude = vLatitude;
    }

    public String getvLongitude() {
        return vLongitude;
    }

    public void setvLongitude(String vLongitude) {
        this.vLongitude = vLongitude;
    }

    public String getmMotherMobile() {
        return mMotherMobile;
    }

    public void setmMotherMobile(String mMotherMobile) {
        this.mMotherMobile = mMotherMobile;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getmLMP() {
        return mLMP;
    }

    public void setmLMP(String mLMP) {
        this.mLMP = mLMP;
    }
}
