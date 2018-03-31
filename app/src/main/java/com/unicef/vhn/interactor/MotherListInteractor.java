package com.unicef.vhn.interactor;

/**
 * Created by sathish on 3/20/2018.
 */

public interface MotherListInteractor {
    void getPNMotherList(String callUrl,String vhnCode, String vhnId) ;
    void getPNMotherRecordsList(String vhnCode, String vhnId) ;
    void getSelectedMother(String vhnCode, String vhnId, String mid) ;
    void getSelectedPNMother( String mid) ;
    void getSelectedSosMother(final String vhnId,final String vhnCode,final String sosId);
    void closeSosAlertSelectedMother(final String vhnId,final String vhnCode,final String sosId);


    void getTremAndPreTremMothersList(String vhnCode, String vhnId) ;

    void getTremAndPreTremMothers(String vhnId,String VhnCode, String mid) ;

    void getANTTMotherList(String vhnCode, String vhnId);

    void getANTT2MotherList(String vhnCode, String vhnId);

    void getImmunizationList(String vhnCode, String vhnId, String id);

    void getSelectedImmuMother(String vhnCode, String vhnId, String mid);


    void getPNHBNCDUEMotherList(String vhnCode, String vhnId,String tempCount);



}
