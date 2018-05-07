package com.unicef.vhn.view;

/**
 * Created by sathish on 3/29/2018.
 */

public interface NotificationViews {
    void showProgress();
    void hideProgress();
    void NotificationResponseSuccess(String response);
    void NotificationResponseError(String response);

    void TodayVisitResponseSuccess(String response);
    void TodayVisitResponseError(String response);

    void NotificationCountSuccess(String respons);
    void NotificationCountError(String respons);
}
