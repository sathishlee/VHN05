package com.unicef.vhn.model;

import java.util.List;

/**
 * Created by sathish on 3/29/2018.
 */

public class PNHBNCDueListModel {

    private List<VPNHBNC_List> vPNHBNC_List;
    private String message;
    private String status;

    public List<VPNHBNC_List> getVPNHBNC_List() {
        return vPNHBNC_List;
    }

    public void setVPNHBNC_List(List<VPNHBNC_List> vPNHBNC_List) {
        this.vPNHBNC_List = vPNHBNC_List;
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

    public static class VPNHBNC_List {
        private String mobile;
        private String picmeId;
        private String motherName;
        private String visit7;
        private String visit6;
        private String visit5;
        private String visit4;
        private String visit3;
        private String visit2;
        private String visit1;
        private String mPhoto;


        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPicmeId() {
            return picmeId;
        }

        public void setPicmeId(String picmeId) {
            this.picmeId = picmeId;
        }

        public String getMotherName() {
            return motherName;
        }

        public void setMotherName(String motherName) {
            this.motherName = motherName;
        }

        public String getVisit7() {
            return visit7;
        }

        public void setVisit7(String visit7) {
            this.visit7 = visit7;
        }

        public String getVisit6() {
            return visit6;
        }

        public void setVisit6(String visit6) {
            this.visit6 = visit6;
        }

        public String getVisit5() {
            return visit5;
        }

        public void setVisit5(String visit5) {
            this.visit5 = visit5;
        }

        public String getVisit4() {
            return visit4;
        }

        public void setVisit4(String visit4) {
            this.visit4 = visit4;
        }

        public String getVisit3() {
            return visit3;
        }

        public void setVisit3(String visit3) {
            this.visit3 = visit3;
        }

        public String getVisit2() {
            return visit2;
        }

        public void setVisit2(String visit2) {
            this.visit2 = visit2;
        }

        public String getVisit1() {
            return visit1;
        }

        public void setVisit1(String visit1) {
            this.visit1 = visit1;
        }

        public String getmPhoto() {
            return mPhoto;
        }

        public void setmPhoto(String mPhoto) {
            this.mPhoto = mPhoto;
        }
    }
}
