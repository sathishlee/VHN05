package com.unicef.vhn.activity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.NotificationPresenter;
import com.unicef.vhn.activity.MotherList.AllMotherListActivity;
import com.unicef.vhn.activity.MotherList.MigrationMotherListActivity;
import com.unicef.vhn.adapter.ViewPagerAdapter;
import com.unicef.vhn.application.MyApplication;
import com.unicef.vhn.broadcastReceiver.ConnectivityReceiver;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.R;
import com.unicef.vhn.fragment.home;
import com.unicef.vhn.fragment.mothers;
import com.unicef.vhn.fragment.risk;
import com.unicef.vhn.fragment.NotificationListFragment;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.utiltiy.RoundedTransformation;
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
    public static TextView notification_count;
    //   public static TextView txt_no_internet;
    String strTodayVisitCount = "0";
    int mCartItemCount = 10;

    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        txt_no_internet = (TextView)findViewById(R.id.txt_no_internet);
//        txt_no_internet.setVisibility(View.GONE);
        checkNetwork = new CheckNetwork(this);
        if (checkNetwork.isNetworkAvailable()) {
//            txt_no_internet.setVisibility(View.GONE);
        } else {
//            txt_no_internet.setVisibility(View.VISIBLE);
        }
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        preferenceData.setNotificationCount(strTodayVisitCount);
        notificationPresenter = new NotificationPresenter(this, this);
        notificationPresenter.getTodayVisitCount(preferenceData.getVhnCode(), preferenceData.getVhnId());

        // every 10 minut notification count api call
        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                if (checkNetwork.isNetworkAvailable()) {
                    notificationPresenter.getNotificationCount(preferenceData.getVhnId());
                } else {
                    strTodayVisitCount = preferenceData.getNotificationCount();
                }
            }
        };

// schedule the task to run starting now and then every hour...
        timer.schedule(hourlyTask, 0l, 500 * 60 * 60);   // 1000*10*60 every 10 minut

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.userImageProfile);
        String str_mPhoto = preferenceData.getphoto();

        if (TextUtils.isEmpty(str_mPhoto)) {
            imageView.setImageResource(R.drawable.girl);
        } else {
            Log.d("mphoto-->", Apiconstants.PHOTO_URL + str_mPhoto);

            Picasso.with(getApplicationContext())
                    .load(Apiconstants.PHOTO_URL + str_mPhoto)
                    .placeholder(R.drawable.girl)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .transform(new RoundedTransformation(90, 4))
                    .error(R.drawable.girl)
                    .into(imageView);


        }
        TextView vhnName = (TextView) headerView.findViewById(R.id.txt_username);
        vhnName.setText("VHN NAME : " + preferenceData.getVhnName().toUpperCase());
        TextView vhnId = (TextView) headerView.findViewById(R.id.vhn_id);
        vhnId.setText("VHN ID : " + preferenceData.getVhnId());
        navigationView.setNavigationItemSelectedListener(this);
        vhnName.setText(preferenceData.getVhnName());
        vhnId.setText(preferenceData.getVhnId());

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        setupNavigationView();

    }




    @Override
    public void onBackPressed() {/*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Are you Sure do you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            android.app.AlertDialog alert = builder.create();
            alert.show();
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
//        notification_count = (TextView) view.findViewById(R.id.notification_count);
//        notification_count.setVisibility(View.GONE);
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
    /*    if(notification_count != null){
            if(mCartItemCount == 0){
//                if (notification_count.getVisibility() != View.GONE){
//                    notification_count.setVisibility(View.GONE);
//                }
            }else{
//                notification_count.setText(String.valueOf(strTodayVisitCount));
                *//*if (notification_count.getVisibility() != View.VISIBLE){
//                    notification_count.setVisibility(View.VISIBLE);
                }*//*
            }
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_notification:
                notificationPresenter.getNotificationList(preferenceData.getVhnCode(), preferenceData.getVhnId());
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content,
                        NotificationListFragment.newInstance()).commit();
                return true;
            case R.id.action_help:
                preferenceData.setLogin(false);
                finish();
                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_LONG).show();
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.an_mothers) {
            AppConstants.GET_MOTHER_LIST_TYPE = "an_mother_total_count";
            AppConstants.MOTHER_LIST_TITLE = "AN Mother List";

            startActivity(new Intent(getApplicationContext(), AllMotherListActivity.class));
        }
        if (id == R.id.pn_hbnc_mothers) {
            AppConstants.GET_MOTHER_LIST_TYPE = "pn_hbnc_totlal_coun";
            AppConstants.MOTHER_LIST_TITLE = "PN/HBNC Mother List";

//            startActivity(new Intent(getApplicationContext(), PNHBNCListActivity.class));
            startActivity(new Intent(getApplicationContext(), AllMotherListActivity.class));

        } else if (id == R.id.immunization) {
            Intent i = new Intent(getApplicationContext(), ImmunizationListActivity.class);
            startActivity(i);
        } else if (id == R.id.alert) {
            Intent i = new Intent(getApplicationContext(), AlertActivity.class);
            startActivity(i);
        } else if (id == R.id.today_visit) {
            Intent i = new Intent(getApplicationContext(), VisitActivity.class);
//            Intent i = new Intent(getApplicationContext(), VisitActivityNew.class);
            startActivity(i);

        } else if (id == R.id.migration_mother) {
            Intent i = new Intent(getApplicationContext(), MotherMigrationNew.class);
//            Intent i = new Intent(getApplicationContext(),MigrationMotherListActivity.class);
            startActivity(i);
        } else if (id == R.id.change_language) {
            Intent i = new Intent(getApplicationContext(), ChangeLanguageActivity.class);
            startActivity(i);
        } else if (id == R.id.change_password) {
            if (checkNetwork.isNetworkAvailable()) {
                Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(i);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("You can't change your password!");
                builder.setMessage("Please check internet connection.");
                // Add the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.show();
            }

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
                AppConstants.ISQUERYFILTER=false;
                // Action to perform when Home Menu item is selected.
                selectedFragment = home.newInstance();
                break;

            case R.id.mothers:
                // Action to perform when Bag Menu item is selected.
//                AppConstants.isfromhome=0;
                selectedFragment = mothers.newInstance();
                break;

            case R.id.risk:
                // Action to perform when Bag Menu item is selected.
//                AppConstants.isfromhome=0;
                AppConstants.ISQUERYFILTER=false;
                selectedFragment = risk.newInstance();
                break;
        }
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, selectedFragment);
        transaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

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
                preferenceData.setTodayVisitCount(jsonObject.getString("visitCount"));
            } else {
                preferenceData.setTodayVisitCount("0");
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
            if (status.equalsIgnoreCase("1")) {
//                notification_count.setVisibility(View.VISIBLE);
                String strNotifyCount = jsonObject.getString("notificationCount");
                preferenceData.setNotificationCount(strNotifyCount);
                Log.d(MainActivity.class.getSimpleName(), "Notification Count-->" + strNotifyCount);
            } else {
                if (msg.equalsIgnoreCase("No Notification")) {
//                    notification_count.setVisibility(View.GONE);
                    Log.d(MainActivity.class.getSimpleName(), "Notification message-->" + msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void NotificationCountError(String respons) {

    }
}
