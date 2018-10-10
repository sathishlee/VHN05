package com.unicef.vhn.realmDbModel;

import io.realm.RealmObject;

public class PushNotificationListRealmModel extends RealmObject {
    String title;
    String body;
    String intime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }


}
