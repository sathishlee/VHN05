package com.unicef.vhn.realmDbModel;

import io.realm.RealmObject;

public class ANTT1RealmModel extends RealmObject {

    private String mMotherMobile;
    private String mANTT1;
    private String mid;
    private String mPicmeId;
    private String mName;
    private String vhnId;

    public String getMMotherMobile() {
        return mMotherMobile;
    }

    public void setMMotherMobile(String mMotherMobile) {
        this.mMotherMobile = mMotherMobile;
    }

    public String getMANTT1() {
        return mANTT1;
    }

    public void setMANTT1(String mANTT1) {
        this.mANTT1 = mANTT1;
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

    public String getVhnId() {
        return vhnId;
    }

    public void setVhnId(String vhnId) {
        this.vhnId = vhnId;
    }

}
