package com.unicef.vhn.model;

import java.util.List;

/**
 * Created by sathish on 3/20/2018.
 */

public class PNMotherListResponse {

    private List<VhnAN_Mothers_List> vhnAN_Mothers_List;
    private String message;
    private String status;

    public List<VhnAN_Mothers_List> getVhnAN_Mothers_List() {
        return vhnAN_Mothers_List;
    }

    public void setVhnAN_Mothers_List(List<VhnAN_Mothers_List> vhnAN_Mothers_List) {
        this.vhnAN_Mothers_List = vhnAN_Mothers_List;
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

    public static class VhnAN_Mothers_List {
        private String mLongitude;
        private String mLatitude;
        private String vhnId;
        private String mid;
        private String mPicmeId;
        private String mName;

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
}