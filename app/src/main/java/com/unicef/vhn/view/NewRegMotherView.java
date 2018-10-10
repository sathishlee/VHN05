package com.unicef.vhn.view;

public interface NewRegMotherView {
    void showProgress();
    void hideProgress();
    void showRegMotherSuccess(String loginResponseModel);
    void showRegMotherError(String string);
}
