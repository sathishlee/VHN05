package com.unicef.vhn.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.unicef.vhn.broadcastReceiver.ConnectivityReceiver;

import com.unicef.vhn.utiltiy.LocaleHelper;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sathish on 3/25/2018.
 */

//public class MyApplication extends Application {
public class MyApplication extends MultiDexApplication {
    private static MyApplication mInstance;

    //@Override
//protected void attachBaseContext(Context base) { super.attachBaseContext(base); Multidex.install(this); }
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("ThaimaiVhn.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


    @Override
    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
        super.attachBaseContext(LocaleHelper.setLocale(base, "en"));
        MultiDex.install(this);
    }
}
