package com.unicef.vhn.activity;


import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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

import com.unicef.vhn.application.MyApplication;
import com.unicef.vhn.broadcastReceiver.ConnectivityReceiver;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.R;
import com.unicef.vhn.fragment.home;
import com.unicef.vhn.fragment.mothers;
import com.unicef.vhn.fragment.risk;
import com.unicef.vhn.fragment.NotificationListFragment;
import com.unicef.vhn.utiltiy.CheckNetwork;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {
    CheckNetwork checkNetwork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkNetwork =new CheckNetwork(this);
/*if (checkNetwork.isNetworkAvailable()){
    Log.w(MainActivity.class.getSimpleName(),"Is"+checkNetwork.isNetworkAvailable());
}else{
    Log.w(MainActivity.class.getSimpleName(),"Is"+checkNetwork.isNetworkAvailable());
    startActivity(new Intent( getApplicationContext(),NoInternetConnectionActivity.class));
}*/
        // Manually checking internet connection
        checkConnection();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupNavigationView();

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            Log.v("MAIN IS INTERNET",message);

            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            Log.v("MAIN IS INTERNET",message);

            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_notification:
//                Toast.makeText(getApplicationContext(),"Notification Clicked", Toast.LENGTH_LONG).show();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content,
                        NotificationListFragment.newInstance()).commit();
                return true;
            case R.id.action_help:
                Toast.makeText(getApplicationContext(),"Help Menu",Toast.LENGTH_LONG).show();
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
//            Intent i = new Intent(getApplicationContext(), ImmunizationListActivity.class);
//            startActivity(i);
        }

        else if (id == R.id.today_visit) {

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
        showSnack(isConnected);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



}
