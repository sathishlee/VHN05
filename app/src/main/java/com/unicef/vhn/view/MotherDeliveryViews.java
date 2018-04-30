package com.unicef.vhn.view;

/**
 * Created by Suthishan on 20/1/2018.
 */

public interface MotherDeliveryViews {
    void showProgress();
    void hideProgress();
    void deliveryDetailsSuccess(String response);
    void deliveryDetailsFailure(String response);
}
