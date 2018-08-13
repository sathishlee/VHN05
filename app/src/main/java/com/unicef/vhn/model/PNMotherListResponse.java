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
/*new*/
    public static class VhnAN_Mothers_List {
        private String mLongitude;
        private String mLatitude;
        private String vhnId;
        private String mid;
        private String mPicmeId;
        private String mName;
        private String motherType;
        private String vLatitude;
        private String vLongitude;
        private String mMotherMobile;
        private String mPhoto;
        private String mLMP;
        private String mAge;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    private String userType;





    public VhnAN_Mothers_List(String mLongitude, String mLatitude, String vhnId, String mid, String mPicmeId, String mName, String motherType, String vLatitude, String vLongitude, String mMotherMobile, String mPhoto, String mLMP, String pnId, String mAge, String userType) {
        this.mLongitude = mLongitude;
        this.mLatitude = mLatitude;
        this.vhnId = vhnId;
        this.mid = mid;
        this.mPicmeId = mPicmeId;
        this.mName = mName;
        this.motherType = motherType;
        this.vLatitude = vLatitude;
        this.vLongitude = vLongitude;
        this.mMotherMobile = mMotherMobile;
        this.mPhoto = mPhoto;
        this.mLMP = mLMP;
        this.pnId = pnId;
        this.mAge = mAge;
        this.userType = userType;
    }

    public VhnAN_Mothers_List() {

    }
    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public String getmPhoto(){return mPhoto;}
        public void setmPhoto(String mPhoto){this.mPhoto = mPhoto;}


        public String getvLatitude(){return vLatitude;}
        public void setvLatitude(String vLatitude){this.vLatitude = vLatitude;}

        public String getmMotherMobile(){return mMotherMobile;}
        public void setmMotherMobile(String mMotherMobile){this.mMotherMobile = mMotherMobile;}

        public String getvLongitude(){return vLongitude;}
        public void setvLongitude(String vLongitude){this.vLongitude = vLongitude;}

        public String getMotherType() {
            return motherType;
        }

        public void setMotherType(String motherType) {
            this.motherType = motherType;
        }


        public String getPnId() {
            return pnId;
        }

        public void setPnId(String pnId) {
            this.pnId = pnId;
        }

        private String pnId;

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

        public String getMlmp(){
            return mLMP;
        }
        public void setMlmp(String mLMP){
            this.mLMP = mLMP;
        }
    }
}
