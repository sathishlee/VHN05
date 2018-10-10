package com.unicef.vhn.interactor;

import android.graphics.Bitmap;

/**
 * Created by Suthishan on 20/1/2018.
 */

public interface ProfileInteractor {

    void uploadUserProfilePhoto(String vhnCode, String vhnId, Bitmap bitmap);

    void getVHNProfile(String vhnId, String vhnCode);

    void postVHNProfile(String vhnId, String vhnCode, String vhnAddress, String vhnMobile);
}
