package com.unicef.vhn.model;

import java.util.List;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class MotherMigrationResponseModel {

    private List<Vhn_migrated_mothers> vhn_migrated_mothers;
    private String message;
    private String status;

    public List<Vhn_migrated_mothers> getVhn_migrated_mothers() {
        return vhn_migrated_mothers;
    }

    public void setVhn_migrated_mothers(List<Vhn_migrated_mothers> vhn_migrated_mothers) {
        this.vhn_migrated_mothers = vhn_migrated_mothers;
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

    public static class Vhn_migrated_mothers {
        private String mtype;
        private String deleveryDate;
        private String mMotherMobile;
        private String subject;
        private String vLongitude;
        private String vLatitude;
        private String mLongitude;
        private String mLatitude;
        private String vhnId;
        private String mid;
        private String mPicmeId;
        private String mName;
        private String mPhoto;


        public String getMtype() {
            return mtype;
        }

        public void setMtype(String mtype) {
            this.mtype = mtype;
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

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
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

        public String getmPhoto(){return mPhoto;}
        public void setmPhoto(String mPhoto){this.mPhoto = mPhoto;}
    }
}
