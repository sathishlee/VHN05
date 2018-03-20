package com.unicef.vhn.view;

/**
 * Created by Suthishan on 20/1/2018.
 */

public interface PNMotherListsViews {
    void showProgress();
    void hideProgress();
    void showLoginSuccess(String response);
    void showLoginError(String string);

}
