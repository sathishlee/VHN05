package com.unicef.vhn.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.NotificationPresenter;
import com.unicef.vhn.application.MyApplication;
import com.unicef.vhn.broadcastReceiver.ConnectivityReceiver;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.R;
import com.unicef.vhn.fragment.home;
import com.unicef.vhn.fragment.mothers;
import com.unicef.vhn.fragment.risk;
import com.unicef.vhn.fragment.NotificationListFragment;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.NotificationViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener, NotificationViews {
    CheckNetwork checkNetwork;
    ProgressDialog pDialog;
    PreferenceData preferenceData;
    NotificationPresenter notificationPresenter;
    TextView notification_count;
    String strTodayVisitCount="0";
    int mCartItemCount = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkNetwork =new CheckNetwork(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData =new PreferenceData(this);
        notificationPresenter =new NotificationPresenter(this,this);
        notificationPresenter.getTodayVisitCount(preferenceData.getVhnCode(),preferenceData.getVhnId());

        // every 10 minut notification count api call
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                // your code here...
                if (checkNetwork.isNetworkAvailable()){
                    notificationPresenter.getNotificationCount(preferenceData.getVhnId());
                }else{
                    notification_count.setText(preferenceData.getNotificationCount());

                }
            }
        };

// schedule the task to run starting now and then every hour...
        timer.schedule (hourlyTask, 0l, 500*60*60);   // 1000*10*60 every 10 minut

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        setupNavigationView();

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        notificationPresenter.getTodayVisitCount(preferenceData.getVhnCode(),preferenceData.getVhnId());
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_notification);
        MenuItemCompat.setActionView(menuItem, R.layout.notification_count);
        View view = MenuItemCompat.getActionView(menuItem);
        notification_count = (TextView) view.findViewById(R.id.notification_count);
//        notification_count.setText(String.valueOf(strTodayVisitCount));
        setupNotiCount();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    private void setupNotiCount() {
        if(notification_count != null){
            if(mCartItemCount == 0){
                if (notification_count.getVisibility() != View.GONE){
                    notification_count.setVisibility(View.GONE);
                }
            }else{
//                notification_count.setText(String.valueOf(strTodayVisitCount));
                if (notification_count.getVisibility() != View.VISIBLE){
                    notification_count.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_notification:
//                Toast.makeText(getApplicationContext(),"Notification Clicked", Toast.LENGTH_LONG).show();
                notificationPresenter.getNotificationList(preferenceData.getVhnCode(),preferenceData.getVhnId());
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content,
                        NotificationListFragment.newInstance()).commit();
                return true;
            case R.id.action_help:
                preferenceData.setLogin(false);
                finish();
                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();
                return true;
            default:
                super.onOptionsItemSelected(item);
        }return true;


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.an_mothers) {
            AppConstants.GET_MOTHER_LIST_TYPE="an_mother_total_count";
            AppConstants.MOTHER_LIST_TITLE="AN Mother List";

            startActivity(new Intent(getApplicationContext(), MotherListActivity.class));
//            Intent i = new Intent(getApplicationContext(), AnMotherListActivity.class);
//            startActivity(i);
        }
        if (id == R.id.pn_hbnc_mothers) {
            AppConstants.GET_MOTHER_LIST_TYPE="pn_hbnc_totlal_coun";
            AppConstants.MOTHER_LIST_TITLE="PN/HBNC Mother List";

            startActivity(new Intent(getApplicationContext(), PNHBNCListActivity.class));
//            Intent i = new Intent(getApplicationContext(), PNHBNCListActivity.class);
//            startActivity(i);
        }
        else if (id == R.id.immunization) {
            Intent i = new Intent(getApplicationContext(), ImmunizationListActivity.class);
            startActivity(i);
        }

        else if (id == R.id.alert) {
            Intent i = new Intent(getApplicationContext(), AlertActivity.class);
            startActivity(i);
        }

        else if (id == R.id.today_visit) {
            Intent i = new Intent(getApplicationContext(),VisitActivity.class);
            startActivity(i);

        }
        else if (id == R.id.migration_mother) {
            Intent i = new Intent(getApplicationContext(),MotherMigration.class);
            startActivity(i);
        }
        else if (id == R.id.change_language) {
            Intent i = new Intent(getApplicationContext(),ChangeLanguageActivity.class);
            startActivity(i);
        }
        else if (id == R.id.change_password) {
            Intent i = new Intent(getApplicationContext(),ChangePasswordActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(0));

            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            selectFragment(item);
                            return false;
                        }
                    });
        }
    }

    public void selectFragment(MenuItem item) {

        item.setChecked(true);
        Fragment selectedFragment = null;
        switch (item.getItemId()) {

            case R.id.home:
                // Action to perform when Home Menu item is selected.
                selectedFragment = home.newInstance();
                break;

            /*case R.id.mothers:
                // Action to perform when Bag Menu item is selected.
                selectedFragment =  mothers.newInstance();
                break;*/
            case R.id.mothers:
                // Action to perform when Bag Menu item is selected.
//                AppConstants.isfromhome=0;
                selectedFragment =  mothers.newInstance();
                break;

            case R.id.risk:
                // Action to perform when Bag Menu item is selected.
//                AppConstants.isfromhome=0;
                selectedFragment =  risk.newInstance();
                break;

                /*case R.id.infant:
                // Action to perform when Bag Menu item is selected.
                selectedFragment =  mothers.newInstance();
                break;*/



        }
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, selectedFragment);
        transaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkNetwork.isNetworkAvailable()){
            Log.w(MainActivity.class.getSimpleName(),"Is"+checkNetwork.isNetworkAvailable());
        }else{
            Log.w(MainActivity.class.getSimpleName(),"Is"+checkNetwork.isNetworkAvailable());
            startActivity(new Intent( getApplicationContext(),NoInternetConnectionActivity.class));
        }
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);


    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected)
    {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void showProgress() {
pDialog.show();
    }

    @Override
    public void hideProgress() {
pDialog.dismiss();
    }

    @Override
    public void NotificationResponseSuccess(String response) {

        /*Log.d(MainActivity.class.getSimpleName(), "Notification count response success" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("message");
//            strTodayVisitCount = jsonObject.getString("notificationCount");

            if (status.equalsIgnoreCase("1")) {
//                preferenceData.setTodayVisitCount(strTodayVisitCount);
//                notification_count.setText(jsonObject.getString("notificationCount"));
//                Log.d(MainActivity.class.getSimpleName(), "Notification Count-->" + strTodayVisitCount);

            } else {
                Log.d(MainActivity.class.getSimpleName(), "Notification messsage-->" + msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    */
    }

    @Override
    public void NotificationResponseError(String response) {

    }

    @Override
    public void TodayVisitResponseSuccess(String response) {
        Log.d(MainActivity.class.getSimpleName(), "Notification count response success" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("message");
             if (status.equalsIgnoreCase("1")) {
                 preferenceData.setNotificationCount(jsonObject.getString(""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void TodayVisitResponseError(String response) {

    }

    @Override
    public void NotificationCountSuccess(String response) {

        Log.d(MainActivity.class.getSimpleName(), "Notification count response success" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("message");
//            strTodayVisitCount = jsonObject.getString("notificationCount");

            if (status.equalsIgnoreCase("1")) {
                strTodayVisitCount = jsonObject.getString("notificationCount");

                preferenceData.setNotificationCount(strTodayVisitCount);

                notification_count.setText(jsonObject.getString("notificationCount"));
                Log.d(MainActivity.class.getSimpleName(), "Notification Count-->" + strTodayVisitCount);

            } else {
                Log.d(MainActivity.class.getSimpleName(), "Notification messsage-->" + msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void NotificationCountError(String respons) {

    }
}
