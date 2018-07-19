package com.unicef.vhn.model;

import java.util.List;

public class RemainderVisitResponseModel {

    private List<Remaindermothers> remaindermothers;
    private String message;
    private String status;

    public List<Remaindermothers> getRemaindermothers() {
        return remaindermothers;
    }

    public void setRemaindermothers(List<Remaindermothers> remaindermothers) {
        this.remaindermothers = remaindermothers;
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

    public static class Remaindermothers {
        private String mtype;
        private String mid;
        private String days;
        private String month;
        private String mMotherMobile;
        private String mLongitude;
        private String mLatitude;
        private String vLongitude;
        private String vLatitude;
        private String vhnId;
        private String mName;
        private String masterId;
        private String picmeId;
        private String mLMP;
        private String noteId;
        private String nextVisit;

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

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getMMotherMobile() {
            return mMotherMobile;
        }

        public void setMMotherMobile(String mMotherMobile) {
            this.mMotherMobile = mMotherMobile;
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

        public String getVhnId() {
            return vhnId;
        }

        public void setVhnId(String vhnId) {
            this.vhnId = vhnId;
        }

        public String getMName() {
            return mName;
        }

        public void setMName(String mName) {
            this.mName = mName;
        }

        public String getMasterId() {
            return masterId;
        }

        public void setMasterId(String masterId) {
            this.masterId = masterId;
        }

        public String getPicmeId() {
            return picmeId;
        }

        public void setPicmeId(String picmeId) {
            this.picmeId = picmeId;
        }

        public String getMLMP() {
            return mLMP;
        }

        public void setMLMP(String mLMP) {
            this.mLMP = mLMP;
        }

        public String getNoteId() {
            return noteId;
        }

        public void setNoteId(String noteId) {
            this.noteId = noteId;
        }

        public String getNextVisit() {
            return nextVisit;
        }

        public void setNextVisit(String nextVisit) {
            this.nextVisit = nextVisit;
        }
    }
}
