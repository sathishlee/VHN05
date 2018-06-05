package com.unicef.vhn.interactor;

public interface ImmunizationInteractor  {
    void getImmunizationList(String vhnCode, String vhnId, String id);

    void getSelectedImmuMother(String vhnCode, String vhnId, String mid);
}
