package com.unicef.vhn.interactor;

/**
 * Created by sathish on 3/14/2018.
 */

public interface LocationUpdateIntractor {
      void uploadLocationToServer(String vhnId, String latitude, String longitude);

}
