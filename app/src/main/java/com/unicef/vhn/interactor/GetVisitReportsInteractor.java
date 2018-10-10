package com.unicef.vhn.interactor;

/**
 * Created by Suthishan on 20/1/2018.
 */

public interface GetVisitReportsInteractor {

    void getallVisitReports(String picmeId, String mid);

    void getallPNVisitReports(String picmeId, String mid);

}
