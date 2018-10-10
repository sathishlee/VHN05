package com.unicef.vhn.realmDbModel;

import io.realm.RealmObject;

public class MotherRiskListRealm extends RealmObject {

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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    private String userType;


    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }


    public String getvLatitude() {
        return vLatitude;
    }

    public void setvLatitude(String vLatitude) {
        this.vLatitude = vLatitude;
    }

    public String getmMotherMobile() {
        return mMotherMobile;
    }

    public void setmMotherMobile(String mMotherMobile) {
        this.mMotherMobile = mMotherMobile;
    }

    public String getvLongitude() {
        return vLongitude;
    }

    public void setvLongitude(String vLongitude) {
        this.vLongitude = vLongitude;
    }

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

    private String pnId;

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

    public String getMlmp() {
        return mLMP;
    }

    public void setMlmp(String mLMP) {
        this.mLMP = mLMP;
    }
}