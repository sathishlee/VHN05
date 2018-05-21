package com.unicef.vhn.interactor;


public interface LoginInteractor {

    void login(
            String strVhnId,
            String strPassword,
            String strdeviceId
    );
}
