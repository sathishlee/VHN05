package com.unicef.vhn.realmDbModel;

import java.util.List;

import io.realm.RealmObject;

public class PNMMotherListRealmModel extends RealmObject {
    private String mRiskStatus;
    private String mEDD;
    private String mHusbandName;
    private String vLongitude;
    private String vLatitude;
    private String mVillage;
    private int currentMonth;
    private String mLMP;
    private String motherType;
    private String mLongitude;
    private String mLatitude;
    private String vhnId;
    private String mMotherMobile;
    private String mid;
    private String mPicmeId;
    private String mName;
    private String mPhoto;
    private String deleveryDate;
    private String dBirthDetails;
    private String pnVisit;
    private String dBirthWeight;
    private String meturityWeek;
    private String gestAge;
    private String mWeight;


    public String getmHusbandMobile() {
        return mHusbandMobile;
    }

    public void setmHusbandMobile(String mHusbandMobile) {
        this.mHusbandMobile = mHusbandMobile;
    }

    private String mHusbandMobile;

    public String getNextVisit() {
        return nextVisit;
    }

    public void setNextVisit(String nextVisit) {
        this.nextVisit = nextVisit;
    }

    private String nextVisit;

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getmRiskStatus() {
        return mRiskStatus;
    }

    public void setmRiskStatus(String mRiskStatus) {
        this.mRiskStatus = mRiskStatus;
    }

    public String getmEDD() {
        return mEDD;
    }

    public void setmEDD(String mEDD) {
        this.mEDD = mEDD;
    }

    public String getmHusbandName() {
        return mHusbandName;
    }

    public void setmHusbandName(String mHusbandName) {
        this.mHusbandName = mHusbandName;
    }

    public String getvLongitude() {
        return vLongitude;
    }

    public void setvLongitude(String vLongitude) {
        this.vLongitude = vLongitude;
    }

    public String getvLatitude() {
        return vLatitude;
    }

    public void setvLatitude(String vLatitude) {
        this.vLatitude = vLatitude;
    }

    public String getmVillage() {
        return mVillage;
    }

    public void setmVillage(String mVillage) {
        this.mVillage = mVillage;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    public String getmLMP() {
        return mLMP;
    }

    public void setmLMP(String mLMP) {
        this.mLMP = mLMP;
    }

    public String getMotherType() {
        return motherType;
    }

    public void setMotherType(String motherType) {
        this.motherType = motherType;
    }

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

    public String getmMotherMobile() {
        return mMotherMobile;
    }

    public void setmMotherMobile(String mMotherMobile) {
        this.mMotherMobile = mMotherMobile;
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


    public String getDeleveryDate() {
        return deleveryDate;
    }

    public void setDeleveryDate(String deleveryDate) {
        this.deleveryDate = deleveryDate;
    }

    public String getdBirthDetails() {
        return dBirthDetails;
    }

    public void setdBirthDetails(String dBirthDetails) {
        this.dBirthDetails = dBirthDetails;
    }

    public String getPnVisit() {
        return pnVisit;
    }

    public void setPnVisit(String pnVisit) {
        this.pnVisit = pnVisit;
    }

    public String getdBirthWeight() {
        return dBirthWeight;
    }

    public void setdBirthWeight(String dBirthWeight) {
        this.dBirthWeight = dBirthWeight;
    }

    public String getMeturityWeek() {
        return meturityWeek;
    }

    public void setMeturityWeek(String meturityWeek) {
        this.meturityWeek = meturityWeek;
    }

    public String getGestAge() {
        return gestAge;
    }

    public void setGestAge(String gestAge) {
        this.gestAge = gestAge;
    }

    public String getmWeight() {
        return mWeight;
    }

    public void setmWeight(String mWeight) {
        this.mWeight = mWeight;
    }
}
