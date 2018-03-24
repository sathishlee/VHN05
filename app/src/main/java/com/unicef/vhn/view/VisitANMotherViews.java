package com.unicef.vhn.view;

/**
 * Created by sathish on 3/23/2018.
 */

public interface VisitANMotherViews {
    void showProgress();
    void hideProgress();
    void showVisitRecordsSuccess(String response);
    void showVisitRecordsFailiur(String response);
}
