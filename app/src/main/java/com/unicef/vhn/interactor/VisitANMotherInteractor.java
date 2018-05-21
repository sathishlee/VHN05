package com.unicef.vhn.interactor;

/**
 * Created by sathish on 3/23/2018.
 */

public interface VisitANMotherInteractor {

    void getVisitANMotherRecords(String vhnCode, String vhnId, String mid);

    void getVisitPNMotherRecords(String vhnCode, String vhnId, String mid);

}
