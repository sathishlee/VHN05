package com.unicef.vhn.model;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ProfileResponseModel {

    private EditProfile EditProfile;
    private String message;
    private int status;

    public EditProfile getEditProfile() {
        return EditProfile;
    }

    public void setEditProfile(EditProfile EditProfile) {
        this.EditProfile = EditProfile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class EditProfile {
        private String hscName;
        private String vhnBlock;
        private String vhnDistrict;
        private String vhnTaluk;
        private String distCode;
        private String vhnAddress;
        private String vphoto;
        private String vhnCode;
        private String vhnName;
        private String vhnId;

        public String getHscName() {
            return hscName;
        }

        public void setHscName(String hscName) {
            this.hscName = hscName;
        }

        public String getVhnBlock() {
            return vhnBlock;
        }

        public void setVhnBlock(String vhnBlock) {
            this.vhnBlock = vhnBlock;
        }

        public String getVhnDistrict() {
            return vhnDistrict;
        }

        public void setVhnDistrict(String vhnDistrict) {
            this.vhnDistrict = vhnDistrict;
        }

        public String getVhnTaluk() {
            return vhnTaluk;
        }

        public void setVhnTaluk(String vhnTaluk) {
            this.vhnTaluk = vhnTaluk;
        }

        public String getDistCode() {
            return distCode;
        }

        public void setDistCode(String distCode) {
            this.distCode = distCode;
        }

        public String getVhnAddress() {
            return vhnAddress;
        }

        public void setVhnAddress(String vhnAddress) {
            this.vhnAddress = vhnAddress;
        }

        public String getVphoto() {
            return vphoto;
        }

        public void setVphoto(String vphoto) {
            this.vphoto = vphoto;
        }

        public String getVhnCode() {
            return vhnCode;
        }

        public void setVhnCode(String vhnCode) {
            this.vhnCode = vhnCode;
        }

        public String getVhnName() {
            return vhnName;
        }

        public void setVhnName(String vhnName) {
            this.vhnName = vhnName;
        }

        public String getVhnId() {
            return vhnId;
        }

        public void setVhnId(String vhnId) {
            this.vhnId = vhnId;
        }
    }
}
