package com.unicef.vhn.interactor;

/**
 * Created by Suthishan on 20/1/2018.
 */

public interface ChangePasswordInteractor {
    void changePassword(String vhnCode, String vhnPassword, String vhnNewPassword, String vhnConPassword);
}
