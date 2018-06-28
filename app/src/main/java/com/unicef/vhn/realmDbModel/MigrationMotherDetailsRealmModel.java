package com.unicef.vhn.realmDbModel;

import io.realm.RealmObject;

public class MigrationMotherDetailsRealmModel extends RealmObject {

    private String nextVisit;
    private int GSTAge;
    private String mHusbandName;
    private String mHusbandMobile;
    private String vLongitude;
    private String vLatitude;
    private String mLongitude;
    private String mLatitude;
    private String mEDD;
    private String mLMP;
    private String mMotherMobile;
    private String mWeight;
    private String mAge;
    private String mPicmeId;
    private String mid;
    private String mName;
    private String mRiskStatus;
    private String deleveryDate;
    private String mPhoto;

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }


    public String getDeleveryDate() {
        return deleveryDate;
    }

    public void setDeleveryDate(String deleveryDate) {
        this.deleveryDate = deleveryDate;
    }


    public String getNextVisit() {
        return nextVisit;
    }

    public void setNextVisit(String nextVisit) {
        this.nextVisit = nextVisit;
    }

    public int getGSTAge() {
        return GSTAge;
    }

    public void setGSTAge(int GSTAge) {
        this.GSTAge = GSTAge;
    }

    public String getMHusbandName() {
        return mHusbandName;
    }

    public void setMHusbandName(String mHusbandName) {
        this.mHusbandName = mHusbandName;
    }

    public String getMHusbandMobile() {
        return mHusbandMobile;
    }

    public void setMHusbandMobile(String mHusbandMobile) {
        this.mHusbandMobile = mHusbandMobile;
    }

    public String getVLongitude() {
        return vLongitude;
    }

    public void setVLongitude(String vLongitude) {
        this.vLongitude = vLongitude;
    }

    public String getVLatitude() {
        return vLatitude;
    }

    public void setVLatitude(String vLatitude) {
        this.vLatitude = vLatitude;
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

    public String getMEDD() {
        return mEDD;
    }

    public void setMEDD(String mEDD) {
        this.mEDD = mEDD;
    }

    public String getMLMP() {
        return mLMP;
    }

    public void setMLMP(String mLMP) {
        this.mLMP = mLMP;
    }

    public String getMMotherMobile() {
        return mMotherMobile;
    }

    public void setMMotherMobile(String mMotherMobile) {
        this.mMotherMobile = mMotherMobile;
    }

    public String getMWeight() {
        return mWeight;
    }

    public void setMWeight(String mWeight) {
        this.mWeight = mWeight;
    }

    public String getMAge() {
        return mAge;
    }

    public void setMAge(String mAge) {
        this.mAge = mAge;
    }

    public String getMPicmeId() {
        return mPicmeId;
    }

    public void setMPicmeId(String mPicmeId) {
        this.mPicmeId = mPicmeId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }

    public String getMRiskStatus() {
        return mRiskStatus;
    }

    public void setMRiskStatus(String mRiskStatus) {
        this.mRiskStatus = mRiskStatus;
    }
}

