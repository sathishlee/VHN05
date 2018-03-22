package com.unicef.vhn.Presenter;

import android.app.Activity;

import com.unicef.vhn.interactor.HomeInteractor;
import com.unicef.vhn.view.MotherListsViews;

/**
 * Created by sathish on 3/22/2018.
 */

public class HomePresenter implements HomeInteractor{

    public HomePresenter(Activity activity, MotherListsViews motherListsViews) {
        this.activity = activity;
        this.motherListsViews = motherListsViews;
    }

    Activity activity;
    MotherListsViews motherListsViews;
    @Override
    public void getDashBoard(String strVhnId, String strPassword) {

    }
}
