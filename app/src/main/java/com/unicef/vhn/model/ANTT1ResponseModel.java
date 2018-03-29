package com.unicef.vhn.model;

import java.util.List;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ANTT1ResponseModel {


    private List<TT1_List> TT1_List;
    private String message;
    private String status;

    public List<TT1_List> getTT1_List() {
        return TT1_List;
    }

    public void setTT1_List(List<TT1_List> TT1_List) {
        this.TT1_List = TT1_List;
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

    public static class TT1_List {
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
}
