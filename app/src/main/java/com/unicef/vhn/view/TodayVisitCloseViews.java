package com.unicef.vhn.view;

public interface TodayVisitCloseViews {
    void showProgress();
    void hideProgress();
    void todayVisitCloseSuccess(String response);
    void todayVisitCloseFailiur(String response);
}
