package com.unicef.vhn.view;

/**
 * Created by sathish on 3/23/2018.
 */

public interface VisitANMotherViews {
    void showProgress();
    void hideProgress();
    void showANVisitRecordsSuccess(String response);
    void showANVisitRecordsFailiur(String response);

    void showPNVisitRecordsSuccess(String response);
    void showPNVisitRecordsFailiur(String response);
}
