package com.unicef.vhn.interactor;

/**
 * Created by sathish on 3/20/2018.
 */

public interface PNMotherListInteractor {
    void getPNMotherList(String vhnCode, String vhnId) ;
    void getSelectedPNMother(String vhnCode, String vhnId,String mid) ;

}
