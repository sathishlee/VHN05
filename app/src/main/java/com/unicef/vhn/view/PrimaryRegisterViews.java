package com.unicef.vhn.view;

/**
 * Created by Suthishan on 20/1/2018.
 */

public interface PrimaryRegisterViews {

    void showProgress();
    void hideProgress();
    void getAllMotherPrimaryRegisterSuccess(String response);

    void getAllMotherPrimaryRegisterFailure(String response);
}
