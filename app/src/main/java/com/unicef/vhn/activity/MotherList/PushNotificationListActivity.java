package com.unicef.vhn.activity.MotherList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.unicef.vhn.R;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.adapter.PushNotificationListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.realmDbModel.ANMotherListRiskRealmModel;
import com.unicef.vhn.realmDbModel.PushNotificationListRealmModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class PushNotificationListActivity extends AppCompatActivity {
RecyclerView recv_notification_list;
TextView  txt_no_records_found;
List<PushNotificationListRealmModel> notifyList;
    PushNotificationListRealmModel pushNotificationListRealmModel;
    PushNotificationListAdapter adapter;
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.activity_push_notification_list);
        recv_notification_list =(RecyclerView)findViewById(R.id.recv_notification_list);
        txt_no_records_found = (TextView)findViewById(R.id.txt_no_records_found);
        notifyList=new ArrayList<>();
        adapter = new PushNotificationListAdapter(notifyList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PushNotificationListActivity.this);
        recv_notification_list.setLayoutManager(mLayoutManager);
        recv_notification_list.setItemAnimator(new DefaultItemAnimator());
        recv_notification_list.setAdapter(adapter);

        loadList();
    }

    private void loadList() {
        realm.beginTransaction();
        RealmResults<PushNotificationListRealmModel> results =realm.where(PushNotificationListRealmModel.class).findAll();
        for (int i=0;i<results.size();i++){
            pushNotificationListRealmModel = new PushNotificationListRealmModel();
            Log.w(AllMotherListActivity.class.getSimpleName(), "ANMotherListRealmModel:--------" + i);

            PushNotificationListRealmModel model = results.get(i);
            pushNotificationListRealmModel.setTitle(model.getTitle());
            pushNotificationListRealmModel.setBody(model.getBody());
            pushNotificationListRealmModel.setIntime(model.getIntime());
            notifyList.add(pushNotificationListRealmModel);
            adapter.notifyDataSetChanged();
        }
        realm.commitTransaction();
    }
}
