package com.unicef.vhn.model;

import java.util.List;

/**
 * Created by sathish on 3/20/2018.
 */

public class PNMotherResponseModel {

    private List<VhnAN_Mothers_Tracking> vhnAN_Mothers_Tracking;
    private String message;
    private String status;

    public List<VhnAN_Mothers_Tracking> getVhnAN_Mothers_Tracking() {
        return vhnAN_Mothers_Tracking;
    }

    public void setVhnAN_Mothers_Tracking(List<VhnAN_Mothers_Tracking> vhnAN_Mothers_Tracking) {
        this.vhnAN_Mothers_Tracking = vhnAN_Mothers_Tracking;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class VhnAN_Mothers_Tracking {
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
    }
}
