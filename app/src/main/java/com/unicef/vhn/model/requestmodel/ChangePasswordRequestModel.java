package com.unicef.vhn.model.requestmodel;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ChangePasswordRequestModel {
    private String vhnConPassword;
    private String vhnNewPassword;
    private String vhnPassword;
    private String vhnCode;

    public String getVhnConPassword() {
        return vhnConPassword;
    }

    public void setVhnConPassword(String vhnConPassword) {
        this.vhnConPassword = vhnConPassword;
    }

    public String getVhnNewPassword() {
        return vhnNewPassword;
    }

    public void setVhnNewPassword(String vhnNewPassword) {
        this.vhnNewPassword = vhnNewPassword;
    }

    public String getVhnPassword() {
        return vhnPassword;
    }

    public void setVhnPassword(String vhnPassword) {
        this.vhnPassword = vhnPassword;
    }

    public String getVhnCode() {
        return vhnCode;
    }

    public void setVhnCode(String vhnCode) {
        this.vhnCode = vhnCode;
    }
}
