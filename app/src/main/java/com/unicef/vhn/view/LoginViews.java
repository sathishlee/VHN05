package com.unicef.vhn.view;

/**
 * Created by Suthishan on 20/1/2018.
 */

public interface LoginViews {
    void showProgress();
    void hideProgress();
    void showLoginSuccess(String loginResponseModel);
    void showLoginError(String string);

}
