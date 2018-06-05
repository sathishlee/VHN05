package com.unicef.vhn.view;

public interface ImmunizationViews {
    void showProgress();
    void hideProgress();
    void getImmunizationListSuccess(String response);
    void getImmunizationListError(String string);
    void callMotherDetailsApi();
    void getImmunizationDetailsSuccess(String response);
    void getImmunizationDetailsError(String string);
}
