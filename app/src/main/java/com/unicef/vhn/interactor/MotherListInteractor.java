package com.unicef.vhn.interactor;

/**
 * Created by sathish on 3/20/2018.
 */

public interface MotherListInteractor {
    void getPNMotherList(String vhnCode, String vhnId) ;
    void getSelectedMother(String vhnCode, String vhnId, String mid) ;

}
