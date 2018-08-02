package com.unicef.vhn.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;


import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.utiltiy.MyFirebaseMessagingService;

import io.realm.Realm;


public class RealmController extends Application {
    private static RealmController instance;
    private final Realm realm;


    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    public static RealmController with(MyFirebaseMessagingService myFirebaseMessagingService) {
        if (instance == null) {
            instance = new RealmController(myFirebaseMessagingService.getApplication());
        }
        return instance;

    }


}
