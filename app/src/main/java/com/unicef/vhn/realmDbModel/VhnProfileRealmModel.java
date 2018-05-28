package com.unicef.vhn.realmDbModel;

import io.realm.RealmObject;

public class VhnProfileRealmModel extends RealmObject {

        private String vhnMobile;
        private String hscName;
        private String vhnBlock;
        private String vhnDistrict;
        private String distCode;
        private String vhnAddress;
        private String vphoto;
        private String vhnCode;
        private String vhnName;
        private String vhnId;

    public String getVhnMobile() {
        return vhnMobile;
    }

    public void setVhnMobile(String vhnMobile) {
        this.vhnMobile = vhnMobile;
    }

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
