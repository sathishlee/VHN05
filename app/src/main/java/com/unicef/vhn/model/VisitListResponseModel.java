package com.unicef.vhn.model;

import java.util.List;

/**
 * Created by sathish on 3/31/2018.
 */

public class VisitListResponseModel {

    private List<Vhn_current_visits> vhn_current_visits;
    private String message;
    private String status;

    public List<Vhn_current_visits> getVhn_current_visits() {
        return vhn_current_visits;
    }

    public void setVhn_current_visits(List<Vhn_current_visits> vhn_current_visits) {
        this.vhn_current_visits = vhn_current_visits;
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

    public static class Vhn_current_visits {

        private String mid;
        private String deleveryDate;
        private String mMotherMobile;
        private String mName;
        private String mLMP;
        private String mLongitude;
        private String mLatitude;
        private String vLongitude;
        private String vLatitude;
        private String picmeId;
        private String vhnId;
        private String nextVisit;
        private String motherType;
        private String mtype;

        private String noteId;
        private String masterId;
        private String month;


        public String getNoteId() {
            return noteId;
        }

        public void setNoteId(String noteId) {
            this.noteId = noteId;
        }

        public String getMasterId() {
            return masterId;
        }

        public void setMasterId(String masterId) {
            this.masterId = masterId;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }


        public String getMtype() {
            return mtype;
        }

        public void setMtype(String mtype) {
            this.mtype = mtype;
        }



        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getDeleveryDate() {
            return deleveryDate;
        }

        public void setDeleveryDate(String deleveryDate) {
            this.deleveryDate = deleveryDate;
        }

        public String getMMotherMobile() {
            return mMotherMobile;
        }

        public void setMMotherMobile(String mMotherMobile) {
            this.mMotherMobile = mMotherMobile;
        }

        public String getMName() {
            return mName;
        }

        public void setMName(String mName) {
            this.mName = mName;
        }

        public String getMLMP() {
            return mLMP;
        }

        public void setMLMP(String mLMP) {
            this.mLMP = mLMP;
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

        public String getPicmeId() {
            return picmeId;
        }

        public void setPicmeId(String picmeId) {
            this.picmeId = picmeId;
        }

        public String getVhnId() {
            return vhnId;
        }

        public void setVhnId(String vhnId) {
            this.vhnId = vhnId;
        }

        public String getNextVisit() {
            return nextVisit;
        }

        public void setNextVisit(String nextVisit) {
            this.nextVisit = nextVisit;
        }

        public String getMotherType() {
            return motherType;
        }

        public void setMotherType(String motherType) {
            this.motherType = motherType;
        }
    }
}
