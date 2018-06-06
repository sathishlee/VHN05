package com.unicef.vhn.model;

import java.util.List;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ANTT2ResponseModel {


    private List<TT2_List> TT2_List;
    private String message;
    private String status;

    public List<TT2_List> getTT2_List() {
        return TT2_List;
    }

    public void setTT2_List(List<TT2_List> TT2_List) {
        this.TT2_List = TT2_List;
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

    public static class TT2_List {
        private String mMotherMobile;
        private String mANTT2;
        private String mid;
        private String mPicmeId;
        private String mName;
        private String vhnId;
        private String mPhoto;


        public String getMMotherMobile() {
            return mMotherMobile;
        }

        public void setMMotherMobile(String mMotherMobile) {
            this.mMotherMobile = mMotherMobile;
        }

        public String getMANTT2() {
            return mANTT2;
        }

        public void setMANTT2(String mANTT2) {
            this.mANTT2 = mANTT2;
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

        public String getmPhoto(){return mPhoto;}
        public void setmPhoto(String mPhoto){this.mPhoto = mPhoto;}
    }
}

