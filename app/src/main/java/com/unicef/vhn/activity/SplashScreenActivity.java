package com.unicef.vhn.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.unicef.vhn.BuildConfig;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.LocationUpdatePresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.service.LocationMonitoringService;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.LocationUpdateViews;

public class SplashScreenActivity extends AppCompatActivity implements LocationUpdateViews {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private boolean mAlreadyStartedService = false;

    private static int SPLASH_TIME_OUT = 3000;


    LocationUpdatePresenter locationUpdatePresenter;
    PreferenceData preferenceData;
    RelativeLayout rel_splash_screen;

    GoogleApiClient googleApiClient = null;

    int deviceApi = Build.VERSION.SDK_INT;
CheckNetwork checkNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        checkNetwork =new CheckNetwork(this);
        if (checkNetwork.isNetworkAvailable()){
            Toast.makeText(getApplicationContext(),"Internet connection is available",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"Internet connection is not available",Toast.LENGTH_LONG).show();

        }
        rel_splash_screen = (RelativeLayout) findViewById(R.id.rel_splash_screen);
        rel_splash_screen.setVisibility(View.VISIBLE);
        locationUpdatePresenter = new LocationUpdatePresenter(this, this);
        preferenceData = new PreferenceData(this);
        checkAPiVersion();


        startStep1();

        if (mAlreadyStartedService) {
            if (!preferenceData.getLogin()) {
                stopService(new Intent(this, LocationMonitoringService.class));
                mAlreadyStartedService = false;
            }
        }
        if (preferenceData.getLogin()) {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String latitude = intent.getStringExtra(AppConstants.EXTRA_LATITUDE);
                            String longitude = intent.getStringExtra(AppConstants.EXTRA_LONGITUDE);
                            String mylocaton = latitude + "\t" + longitude;
                            if (latitude != null && longitude != null) {
                                locationUpdatePresenter.uploadLocationToServer(preferenceData.getVhnId(), latitude, longitude);
                            }
                        }
                    }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
            );


        }

    }

    private void checkAPiVersion() {
        if (deviceApi <= Build.VERSION_CODES.KITKAT) {
            startActivity(new Intent(this, LowerVersionActivity.class));
            finish();
        } else {
            startStep1();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        startStep1();
    }

    /**
     * Step 1: Check Google Play services
     */
    private void startStep1() {
        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {
            //Passing null to indicate that it is executing for the first time.
            startStep2(null);

        } else {
            Toast.makeText(getApplicationContext(), R.string.no_google_playservice_available, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Step 2: Check & Prompt Internet connection
     */
    private Boolean startStep2(DialogInterface dialog) {
        Log.e(SplashScreenActivity.class.getSimpleName(),"startStep2");
        if (preferenceData.getLogin()) {
            Log.e(SplashScreenActivity.class.getSimpleName(),"Log in status "+preferenceData.getLogin());
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                if (preferenceData.getLogin()) {
                    startStep3();
                    return false;
                } else {
                    promptInternetConnect();
                    return true;

                }
            }


            if (dialog != null) {
                dialog.dismiss();
            }

            //Yes there is active internet connection. Next check Location is granted by user or not.

            if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
                startStep3();
            } else {  //No user has not granted the permissions yet. Request now.
                requestPermissions();
            }
            return true;

        }
        Log.e(SplashScreenActivity.class.getSimpleName(),"startStep"+ true);

        return true;
    }


    /**
     * Show A Dialog with button to refresh the internet state.
     */
    private void promptInternetConnect() {
Log.e(SplashScreenActivity.class.getSimpleName(),"promptInternetConnect show dailog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_alert_no_intenet);
        builder.setMessage(R.string.msg_alert_no_internet);
//
        String positiveText = getString(R.string.btn_label_refresh);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
//                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(SplashScreenActivity.class.getSimpleName(),"refresh on click");


        //Block the Application Execution until user grants the permissions
                        if (startStep2(dialog)) {
                            Log.e(SplashScreenActivity.class.getSimpleName(),"startStep2(dialog) " +startStep2(dialog));


//        if (startStep2(null)) {

            //Now make sure about location permission.
            if (checkPermissions()) {
                Log.e(SplashScreenActivity.class.getSimpleName(),"checkPermissions "+ checkPermissions());

                //Step 2: Start the Location Monitor Service
                //Everything is there to start the service.
                startStep3();
            } else if (!checkPermissions()) {
                Log.e(SplashScreenActivity.class.getSimpleName(),"checkPermissions "+ checkPermissions());
                requestPermissions();
            }
        }
//        }
                    }
                });
//
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Step 3: Start the Location Monitor Service
     */
    private void startStep3() {
        Log.e(SplashScreenActivity.class.getSimpleName(),"startStep3 called");
//        rel_splash_screen.setVisibility(View.VISIBLE);
        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.

        if (!mAlreadyStartedService) {
            //Start location sharing service to app server.........
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);

            mAlreadyStartedService = true;
            //Ends................................................
        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (preferenceData.getLogin()) {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    /**
     * Return the availability of GooglePlayServices
     */
    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionState3 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int permissionState4 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permissionState5 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionState6 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionState7 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionState8 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED &&
                permissionState3 == PackageManager.PERMISSION_GRANTED && permissionState4 == PackageManager.PERMISSION_GRANTED
                && permissionState5 == PackageManager.PERMISSION_GRANTED && permissionState6 == PackageManager.PERMISSION_GRANTED
                && permissionState7 == PackageManager.PERMISSION_GRANTED && permissionState8 == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Start permissions requests.
     */
    private void requestPermissions() {
        Log.e(SplashScreenActivity.class.getSimpleName(),"requestPermissions ");

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        boolean shouldProvideRationale3 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.INTERNET);
        boolean shouldProvideRationale4 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA);
        boolean shouldProvideRationale5 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.SEND_SMS);
        boolean shouldProvideRationale6 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean shouldProvideRationale7 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean shouldProvideRationale8 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CALL_PHONE);


        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale || shouldProvideRationale2 || shouldProvideRationale3 || shouldProvideRationale4
                || shouldProvideRationale5 || shouldProvideRationale6 || shouldProvideRationale7 || shouldProvideRationale8) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(SplashScreenActivity.this,
                                    new String[]
                                            {
                                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                                    Manifest.permission.INTERNET,
                                                    Manifest.permission.CAMERA,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.CALL_PHONE,
                                                    Manifest.permission.SEND_SMS

                                            },
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(this,
                    new String[]
                            {
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.INTERNET,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.SEND_SMS,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CALL_PHONE
                            },
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If img_user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "Permission granted, updates requested, starting location updates");
                startStep3();

            } else {
                // Permission denied.

                // Notify the img_user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the img_user for permission (device policy or "Never ask
                // again" prompts). Therefore, a img_user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }


    @Override
    public void onDestroy() {


        //Stop location sharing service to app server.........
        stopService(new Intent(this, LocationMonitoringService.class));
        mAlreadyStartedService = false;
        //Ends................................................


        super.onDestroy();
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void locationUpdateSuccess(String loginResponseModel) {
        Log.d(TAG, "success--->" + loginResponseModel);
    }

    @Override
    public void locationUpdateFailiure(String string) {
        Log.d(TAG, "Error--->" + string);


    }

    @Override
    public void getNearbyHospitalSuccess(String loginResponseModel) {

    }

    @Override
    public void getNearbyHospitalFailiure(String string) {

    }
}
