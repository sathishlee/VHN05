package com.unicef.vhn.model;

import java.util.List;

/**
 * Created by sathish on 3/23/2018.
 */

public class SOSListResponse {

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
        private String vhnId;
        private String motherType;
        private String sosStatus;
        private String mid;
        private String mRiskStatus;
        private String sosId;
        private String mPicmeId;
        private String mName;
        private String motherType;

        public String getMotherType() {
            return motherType;
        }

        public void setMotherType(String motherType) {
            this.motherType = motherType;
        }



        public String getVhnId() {
            return vhnId;
        }

        public String getMotherType(){
            return motherType;
        }
        public void setMotherType(String motherType){
            this.motherType = motherType;
        }

        public void setVhnId(String vhnId) {
            this.vhnId = vhnId;
        }

        public String getSosStatus() {
            return sosStatus;
        }

        public void setSosStatus(String sosStatus) {
            this.sosStatus = sosStatus;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getMRiskStatus() {
            return mRiskStatus;
        }

        public void setMRiskStatus(String mRiskStatus) {
            this.mRiskStatus = mRiskStatus;
        }

        public String getSosId() {
            return sosId;
        }

        public void setSosId(String sosId) {
            this.sosId = sosId;
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
