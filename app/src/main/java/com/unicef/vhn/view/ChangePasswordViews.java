package com.unicef.vhn.view;

/**
 * Created by Suthishan on 20/1/2018.
 */

public interface ChangePasswordViews {
    void showProgress();
    void hideProgress();
    void changePasswordSuccess(String response);
    void changePasswordFailure(String response);

}
