package com.unicef.vhn.view;

/**
 * Created by Suthishan on 20/1/2018.
 */

public interface ProfileViews {

    void showProgress();

    void hideProgress();

    void successViewProfile(String response);

    void errorViewProfile(String response);

    void successUploadPhoto(String response);

    void errorUploadPhoto(String response);
}
