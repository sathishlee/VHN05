package com.unicef.vhn.interactor;

/**
 * Created by sathish on 3/29/2018.
 */

public interface NotificationInteractor {
    void getNotificationCount(String mid);
    void getTodayVisitCount(String vhnCode, String vhnId);
    void getNotificationList(String vhnCode, String vhnId);
    void getNotificationDetails(String mid);
}
