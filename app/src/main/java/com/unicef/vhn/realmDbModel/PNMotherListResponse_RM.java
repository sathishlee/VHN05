package com.unicef.vhn.realmDbModel;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by sathish on 3/20/2018.
 */

public class PNMotherListResponse_RM extends RealmObject {

    private String message;

    private String status;
    /*private RealmList vhnAN_Mothers_List_RM;

    public RealmList getVhnAN_Mothers_List_RM() {
        return vhnAN_Mothers_List_RM;
    }

    public void setVhnAN_Mothers_List_RM(RealmList vhnAN_Mothers_List_RM) {
        vhnAN_Mothers_List_RM = vhnAN_Mothers_List_RM;
    }
*/


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
