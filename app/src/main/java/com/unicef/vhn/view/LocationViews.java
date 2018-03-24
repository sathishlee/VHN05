package com.unicef.vhn.view;

/**
 * Created by Suthishan on 20/1/2018.
 */

public interface LocationViews {
    void showProgress();
    void hideProgress();
    void showLocationSuccess(String response);
    void showLocationError(String string);
}
