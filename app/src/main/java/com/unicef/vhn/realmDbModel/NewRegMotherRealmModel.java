package com.unicef.vhn.realmDbModel;

import io.realm.RealmObject;

public class NewRegMotherRealmModel extends RealmObject {
    private String datetime;
    private String regid;
    private String mlat;
    private String mlong;
    private String vhnId;
    private String dCode;
    private String bkid;
    private String deviceToken;
    private String phcid;
    private String status;
    private String mHusbandMobile;
    private String mHusbandName;
    private String mDOB;
    private String mMobileNumber;
    private String mName;

    public String getRegid() {
        return regid;
    }

    public void setRegid(String regid) {
        this.regid = regid;
    }

    public String getMlat() {
        return mlat;
    }

    public void setMlat(String mlat) {
        this.mlat = mlat;
    }

    public String getMlong() {
        return mlong;
    }

    public void setMlong(String mlong) {
        this.mlong = mlong;
    }

    public String getVhnId() {
        return vhnId;
    }

    public void setVhnId(String vhnId) {
        this.vhnId = vhnId;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    public String getBkid() {
        return bkid;
    }

    public void setBkid(String bkid) {
        this.bkid = bkid;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getPhcid() {
        return phcid;
    }

    public void setPhcid(String phcid) {
        this.phcid = phcid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getmHusbandMobile() {
        return mHusbandMobile;
    }

    public void setmHusbandMobile(String mHusbandMobile) {
        this.mHusbandMobile = mHusbandMobile;
    }

    public String getmHusbandName() {
        return mHusbandName;
    }

    public void setmHusbandName(String mHusbandName) {
        this.mHusbandName = mHusbandName;
    }

    public String getmDOB() {
        return mDOB;
    }

    public void setmDOB(String mDOB) {
        this.mDOB = mDOB;
    }

    public String getmMobileNumber() {
        return mMobileNumber;
    }

    public void setmMobileNumber(String mMobileNumber) {
        this.mMobileNumber = mMobileNumber;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
