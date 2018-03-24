package com.unicef.vhn.model;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class LocationRequestModel {


    private Vhn_location vhn_location;
    private Mother_location mother_location;
    private Tracking tracking;
    private String message;
    private String status;

    public Vhn_location getVhn_location() {
        return vhn_location;
    }

    public void setVhn_location(Vhn_location vhn_location) {
        this.vhn_location = vhn_location;
    }

    public Mother_location getMother_location() {
        return mother_location;
    }

    public void setMother_location(Mother_location mother_location) {
        this.mother_location = mother_location;
    }

    public Tracking getTracking() {
        return tracking;
    }

    public void setTracking(Tracking tracking) {
        this.tracking = tracking;
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

    public static class Vhn_location {
        private String vLongitude;
        private String vLatitude;

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
    }

    public static class Mother_location {
        private String mLongitude;
        private String mLatitude;

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
    }

    public static class Tracking {
        private String vLongitude;
        private String vLatitude;
        private String mLongitude;
        private String mLatitude;
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
