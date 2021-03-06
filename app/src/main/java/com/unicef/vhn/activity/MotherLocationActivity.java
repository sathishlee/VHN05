package com.unicef.vhn.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.LocationPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.LocationRequestModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.utiltiy.RoundedTransformation;
import com.unicef.vhn.view.LocationViews;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.fraction;

public class MotherLocationActivity extends FragmentActivity
        implements LocationViews, OnMapReadyCallback, View.OnClickListener {

    List<LocationRequestModel.Tracking> trackings;

    ProgressDialog progressDialog;

    LocationPresenter locationPresenter;
    LocationRequestModel locationRequestModel;

    Activity applicationContext;

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    LocationManager locationManager;
    android.location.LocationListener listener;
    PreferenceData preferenceData;
    private static final int overview = 0;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM = 201;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE_TO = 202;
    private String TAG = getClass().getSimpleName();
    private GoogleMap mMap;
    private String GOOGLE_PLACES_API_KEY = "AIzaSyD789ejb86QhaIBRPovLCtjW_XrrDAKdto";
    private List<Marker> markers;
    TextView txt_username, txt_picme_id, txt_call;
    String strLat, strLon, motherLatitude, motherLongitude, vhnLatitude, vhnLongitude, motherLocation, VhnLocation;
    String strMotherloc, strVHNloc, strmAdd, strvAdd, motherName, motherID, str_mPhoto, strMotherNo;
    Context context;
    ImageView cardview_image;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    CheckNetwork checkNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bindActivity();
        onClickListner();
    }

    FloatingActionButton get_directions;

    private void bindActivity() {

        applicationContext = this;

        progressDialog = new ProgressDialog(this);
        checkNetwork = new CheckNetwork(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait ...");

        if (!checkNetwork.isNetworkAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("You can't see this mother location!");
            builder.setCancelable(false);
            builder.setMessage("Please check internet connection");
            // Add the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
//                    dialog.dismiss();
                    finish();
                }
            });


// Create the AlertDialog
            AlertDialog dialog = builder.create();

            dialog.show();
        }
        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_picme_id = (TextView) findViewById(R.id.txt_picme_id);
        txt_call = (TextView) findViewById(R.id.txt_call);
        cardview_image = (ImageView) findViewById(R.id.cardview_image);

        preferenceData = new PreferenceData(this);
//        this.trackings = trackings;

        locationPresenter = new LocationPresenter(MotherLocationActivity.this, this);
        locationPresenter.getMotherLocatin(preferenceData.getVhnCode(), preferenceData.getVhnId(), AppConstants.SELECTED_MID);

        findViewById(R.id.get_directions).setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mother Location");
        toolbar.setNavigationIcon(R.drawable.left_arrow_key);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onClickListner() {
        txt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strMotherNo.equalsIgnoreCase("null") || strMotherNo.length() < 10) {
//                    img_call_1.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                } else {
                    makeCall(strMotherNo);
                }

            }
        });
    }

    private void setupGoogleMapScreenSettings(GoogleMap mMap) {
        mMap.setBuildingsEnabled(true);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
        mUiSettings.setAllGesturesEnabled(true);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupGoogleMapScreenSettings(googleMap);

//        mMap.setMyLocationEnabled(true);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            Toast.makeText(MotherLocationActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, listener);


        // Add a marker in Sydney and move the camera

/*        double mlat = Double.parseDouble(motherLatitude);
        double mlong = Double.parseDouble(motherLongitude);
        LatLng latLng = new LatLng(mlat,mlong);
        mMap.addMarker(new MarkerOptions().position(latLng).title(strmAdd));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));*/
        get_directions = (FloatingActionButton) findViewById(R.id.get_directions);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.get_directions:
//                if (strmAdd.length() <= 0) {
//                    showMessage("Please pick from address");
//                    return;
//                }
//                if (strvAdd.length() <= 0) {
//                    showMessage("Please pick to address");
//                    return;
//                }
                if (view.equals(get_directions)) {
                    addMarkersToMaplatlng(motherLatitude, motherLongitude, vhnLatitude, vhnLongitude);
                    getResultLocation();
                } else {
                    addMotherLocation(motherLatitude, motherLongitude);
                }
//                getDirections();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean x = super.onOptionsItemSelected(item);
        finish();
        return x;
    }

    public void getDirections() {
        Log.e("Mlatitude--", motherLatitude);
        Log.e("Mlong--", motherLongitude);
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + motherLatitude + "," + motherLongitude);


        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(applicationContext.getPackageManager()) != null) {
            applicationContext.startActivity(mapIntent);
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent = new Intent(MotherLocationActivity.this, .class);
//        finish();
//        startActivity(intent);
//        return super.onOptionsItemSelected(item);
//    }
//    private void getResult() {
//        DirectionsResult results = getDirectionsDetails(mTvFrom.getText().toString(), mTvTo.getText().toString(), TravelMode.DRIVING);
//        if (results != null) {
//            addPolyline(results, mMap);
//            addMarkersToMap(results, mMap);
//            positionCamera(results.routes[overview], mMap);
//        }
//    }

    private void getResultLocation() {
        DirectionsResult directionsResult = getDirectionsDetails(strmAdd, strvAdd, TravelMode.DRIVING);

        Log.d("Mother---", strmAdd);
        Log.d("VHN--", strvAdd);
        if (directionsResult != null) {
            addPolyline(directionsResult, mMap);
//            addMarkersToMap(directionsResult, mMap);
            addMarkersToMap(directionsResult, mMap);

            positionCamera(directionsResult.routes[overview], mMap);
        }
    }

    public void addMarkerMap(DirectionsResult directionsResult, GoogleMap mMap) {
//        Marker markerSrc = mMap.addMarker(new MarkerOptions().position(new LatLng(directionsResult.routes[overview])))
    }

    private void addMarkersToMap(DirectionsResult directionsResult, GoogleMap mMap) {
       /* final Marker markerSrc = mMap.addMarker(new MarkerOptions().position(new LatLng(directionsResult.routes[overview].legs[overview].startLocation.lat, directionsResult.routes[overview].legs[overview].startLocation.lng)).title(directionsResult.routes[overview].legs[overview].startAddress));
        final Marker markerDes = mMap.addMarker(new MarkerOptions().position(new LatLng(directionsResult.routes[overview].legs[overview].endLocation.lat, directionsResult.routes[overview].legs[overview].endLocation.lng)).title(directionsResult.routes[overview].legs[overview].endAddress).snippet(getEndLocationTitle(directionsResult)));
        markers = new ArrayList<>();
        markers.add(markerSrc);
        markers.add(markerDes);*/

        final Marker markerSrc = mMap.addMarker(new MarkerOptions().position(
                new LatLng(directionsResult.routes[overview].legs[overview].startLocation.lat,
                        directionsResult.routes[overview].legs[overview].startLocation.lng))
                .title(directionsResult.routes[overview].legs[overview].startAddress)
                .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(MotherLocationActivity.this, R.drawable.girl))));
        final Marker markerDes = mMap.addMarker(new MarkerOptions().position(new LatLng(directionsResult.routes[overview]
                .legs[overview].endLocation.lat, directionsResult.routes[overview].legs[overview].endLocation.lng))
                .title(directionsResult.routes[overview].legs[overview].endAddress)
                .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(MotherLocationActivity.this, R.drawable.ic_nurse)))
                .snippet(getEndLocationTitle(directionsResult)));
        markers = new ArrayList<>();
        markers.add(markerSrc);
        markers.add(markerDes);

    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);
//        TextView txt_name = (TextView)marker.findViewById(R.id.name);
//        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    private void addMotherLocation(String motherLatitude, String motherLongitude) {
        double mlat = Double.parseDouble(motherLatitude);
        double mlong = Double.parseDouble(motherLongitude);

        final LatLng mlatlng = new LatLng(mlat, mlong);

        strmAdd = getCompleteAddressString(motherLatitude, motherLongitude);

        MarkerOptions mothermarker = new MarkerOptions().position(new LatLng(mlat, mlong))
                .title("Mother Name : " + motherName)
                .snippet("Piceme Id : " + motherID)
                .snippet("Address : " + strmAdd);

        strMotherloc = String.valueOf(mothermarker);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mlatlng, 12));
//        mothermarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        mothermarker.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(MotherLocationActivity.this, R.drawable.girl)));
        mMap.addMarker(mothermarker);
    }

    private void addMarkersToMaplatlng(String motherLatitude, String motherLongitude,
                                       String vhnLatitude, String vhnLongitude) {
        double mlat = Double.parseDouble(motherLatitude);
        double mlong = Double.parseDouble(motherLongitude);
        double vlat = Double.parseDouble(vhnLatitude);
        double vlong = Double.parseDouble(vhnLongitude);

        final LatLng mlatlng = new LatLng(mlat, mlong);
        strLat = String.valueOf(mlatlng);
        Log.d("Mother Location--->", strLat);

        final LatLng vlatlng = new LatLng(vlat, vlong);
        strLon = String.valueOf(vlatlng);
        Log.d("VHN Location--->", strLon);

        MarkerOptions mothermarker = new MarkerOptions().position(new LatLng(mlat, mlong)).title(strmAdd);
        MarkerOptions vhnmarker = new MarkerOptions().position(new LatLng(vlat, vlong)).title(preferenceData.getVhnName());

        strMotherloc = String.valueOf(mothermarker);
        strVHNloc = String.valueOf(vhnmarker);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mlatlng, 12));

//        LatLngBounds vhnmother = new LatLngBounds(new LatLng(mlat,mlong), new LatLng(vlat, vlong));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(vlatlng, 12));

        mothermarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        vhnmarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        // Add marker to map
        mMap.addMarker(mothermarker);
        mMap.addMarker(vhnmarker);

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(mlat, mlong, 1);
            List<Address> addresses1 = geocoder.getFromLocation(vlat, vlong, 1);

            if (addresses != null) {
                String maddress = addresses.get(0).getAddressLine(0);
                String mcity = addresses.get(0).getLocality();
                String mstate = addresses.get(0).getAdminArea();
                String mcountry = addresses.get(0).getCountryName();
                String mpostalcode = addresses.get(0).getPostalCode();
                String mknownname = addresses.get(0).getFeatureName();

                strmAdd = String.valueOf(maddress + mcity + mstate);

                Log.d("Mother Address-->", strmAdd);
            }

            if (addresses != null) {
                String vaddress = addresses1.get(0).getAddressLine(0);
                String vcity = addresses1.get(0).getLocality();
                String vstate = addresses1.get(0).getAdminArea();
                String vcountry = addresses1.get(0).getCountryName();
                String vpostalcode = addresses1.get(0).getPostalCode();
                String vknownname = addresses1.get(0).getFeatureName();

                strvAdd = String.valueOf(vaddress + vcity + vstate);

                Log.d("Mother Address-->", strvAdd);

            }

            if (strmAdd.equals(strvAdd)) {
                Toast.makeText(getApplicationContext(), "You are Nearer to Mother...", Toast.LENGTH_SHORT).show();
            }
//            if (addresses != null) {
//                Address returnedAddress = addresses.get(0);
//                StringBuilder strReturnedAddress = new StringBuilder("Mother Address:\n");
//
//                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
//                }
//                strmAdd = String.valueOf(strReturnedAddress);
//
//            }
//            if (addresses1 != null) {
//                Address address = addresses1.get(0);
//                StringBuilder stringBuilder = new StringBuilder("VHN Address:\n");
//                for(int i=0; i<address.getMaxAddressLineIndex(); i++) {
//                    stringBuilder.append(address.getAddressLine(i)).append("\n");
//                }
//                strvAdd = String.valueOf(stringBuilder);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;


    }

    private void positionCamera(DirectionsRoute route, GoogleMap mMap) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 150);
        mMap.animateCamera(cu);

    }

    private void addPolyline(DirectionsResult directionsResult, GoogleMap mMap) {
        mMap.clear();
        List<LatLng> decodedPath = PolyUtil.decode(directionsResult.routes[overview].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }

    private String getEndLocationTitle(DirectionsResult directionsResult) {
        return "Time :" + directionsResult.routes[overview].legs[overview].duration.humanReadable + " Distance :" + directionsResult.routes[overview].legs[overview].distance.humanReadable;
    }

    public GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext
                .setQueryRateLimit(3)
                .setApiKey(GOOGLE_PLACES_API_KEY)
                .setConnectTimeout(100, TimeUnit.SECONDS)
                .setReadTimeout(100, TimeUnit.SECONDS)
                .setWriteTimeout(100, TimeUnit.SECONDS);
    }


    private DirectionsResult getDirectionsDetails(String origin, String destination, TravelMode mode) {

//        Log.d("Origin",origin);
//        Log.d("Destination", destination);

        DateTime now = new DateTime();
        try {
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .await();
        } catch (ApiException e) {
            Log.e("DirectionsResult1", e.toString());
            e.printStackTrace();
            showMessage(e.getMessage());
            return null;
        } catch (InterruptedException e) {
            Log.e("DirectionsResult2", e.toString());

            e.printStackTrace();
            showMessage(e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("DirectionsResult", e.toString());

            e.printStackTrace();
            showMessage(e.getMessage());
            return null;
        }
    }

    private void pickLocation(int requestCode) {
        try {

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setCountry("IN")
                    .build();

            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, requestCode);
        } catch (GooglePlayServicesRepairableException e) {
            showMessage(e.getMessage());
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
            showMessage(e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            Log.i(TAG, "Place: " + place.getName());
//            setResultText(place, requestCode);
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);
            showMessage(status.getStatusMessage());
        }


    }

//    private void setResultText(Place place, int requestCode) {
//        switch (requestCode) {
//            case PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM:
//
////                mTvFrom.setText(place.getAddress());
//                break;
//            case PLACE_AUTOCOMPLETE_REQUEST_CODE_TO:
//
//                mTvTo.setText(place.getAddress());
//                break;
//        }
//    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void showLocationSuccess(String response) {
//        AppConstants.SELECTED_MID="0";

        Log.e(MotherLocationActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            mMap.clear();
            if (status.equalsIgnoreCase("1")) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                JSONObject mJsnobject_tracking = mJsnobject.getJSONObject("tracking");
                txt_username.setText(mJsnobject_tracking.getString("mName"));
                txt_picme_id.setText(mJsnobject_tracking.getString("mPicmeId"));
                motherName = mJsnobject_tracking.getString("mName");
                motherID = mJsnobject_tracking.getString("mPicmeId");
                strMotherNo = mJsnobject_tracking.getString("mMotherMobile");

                str_mPhoto = mJsnobject_tracking.getString("mPhoto");
                Picasso.with(context)
                        .load(Apiconstants.MOTHER_PHOTO_URL + str_mPhoto)
                        .placeholder(R.drawable.girl)
                        .fit()
                        .centerCrop()
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .transform(new RoundedTransformation(90, 4))
                        .error(R.drawable.girl)
                        .into(cardview_image);

                motherLocation = mJsnobject_tracking.getString("mLocation");
                VhnLocation = mJsnobject_tracking.getString("vLocation");

                if (motherLocation.equalsIgnoreCase("0")) {
                    Toast.makeText(getApplicationContext(), "Mother Location Not Found...!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                if (motherLocation.equalsIgnoreCase("1")) {
                    motherLatitude = mJsnobject_tracking.getString("mLatitude");
                    motherLongitude = mJsnobject_tracking.getString("mLongitude");
                    addMotherLocation(motherLatitude, motherLongitude);
                }
                if (VhnLocation.equalsIgnoreCase("1")) {
                    vhnLatitude = mJsnobject_tracking.getString("vLatitude");
                    vhnLongitude = mJsnobject_tracking.getString("vLongitude");
//                    addMarkersToMaplatlng(motherLatitude,motherLongitude,vhnLatitude,vhnLongitude);
                } else {
                    Toast.makeText(getApplicationContext(), VhnLocation, Toast.LENGTH_SHORT).show();
                    finish();
                }

                /*JSONObject mJsonObject = mJsnobject.getJSONObject("mother_location");
                motherLatitude = mJsonObject.getString("mLatitude");
                motherLongitude = mJsonObject.getString("mLongitude");
                JSONObject vJsonObject = mJsnobject.getJSONObject("vhn_location");
                vhnLatitude = vJsonObject.getString("vLatitude");
                vhnLongitude = vJsonObject.getString("vLongitude");*/


                /*JSONArray routeArray = mJsnobject.getJSONArray("mother_location");
                for (int i = 0; i < routeArray.length(); i++) {

                    LatLng latLng1=new LatLng(Double.parseDouble(routeArray.getJSONObject(i).getString("mLatitude")),Double.parseDouble(routeArray.getJSONObject(i).getString("mLongitude")));
                    mMap.addMarker(new MarkerOptions().position(latLng1).title("Mother Location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 12));
                    addMarkersToMap(Double.parseDouble(routeArray.getJSONObject(i).getString("mLatitude")),
                            Double.parseDouble(routeArray.getJSONObject(i).getString("mLongitude")));
                }

                JSONArray routeArray1 = mJsnobject.getJSONArray("vhn_location");
                for (int i = 0; i < routeArray1.length(); i++) {
                    LatLng latLng2=new LatLng(Double.parseDouble(routeArray1.getJSONObject(i).getString("vLatitude")),Double.parseDouble(routeArray.getJSONObject(i).getString("vLongitude")));
                    mMap.addMarker(new MarkerOptions().position(latLng2).title("VHN Location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 12));
                    addMarkersToMap(Double.parseDouble(routeArray.getJSONObject(i).getString("mLatitude")),
                            Double.parseDouble(routeArray.getJSONObject(i).getString("mLongitude")));
                }*/


            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLocationError(String string) {
        AppConstants.SELECTED_MID = "0";

        Log.e(MotherLocationActivity.class.getSimpleName(), "Response success" + string);

    }

    private void makeCall(String strMotherNo) {
        Toast.makeText(getApplicationContext(), strMotherNo, Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            Log.i(MothersDetailsActivity.class.getSimpleName(), "CALL permission has already been granted. Displaying camera preview.");

            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + strMotherNo)));

        }
    }

    private void requestCallPermission() {
        Log.i(MothersDetailsActivity.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            Log.i(MothersDetailsActivity.class.getSimpleName(), "Displaying camera permission rationale to provide additional context.");
            Toast.makeText(this, "Displaying camera permission rationale to provide additional context.", Toast.LENGTH_SHORT).show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    MAKE_CALL_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    dial.setEnabled(true);
                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationPresenter.getMotherLocatin(preferenceData.getVhnCode(), preferenceData.getVhnId(), AppConstants.SELECTED_MID);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(MotherLocationActivity.this, MainActivity.class);
        finish();
//        startActivity(intent);
    }

    private String getCompleteAddressString(String latitude, String longitude) {
        String strAdd = "";
        double dlatitude = Double.parseDouble(latitude);
        double dlongitude = Double.parseDouble(longitude);
        Log.w("dlatitude", dlatitude + "");
        Log.w("dlongitude", dlongitude + "");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            if (checkNetwork.isNetworkAvailable()) {
                List<Address> addresses = geocoder.getFromLocation(dlatitude, dlongitude, 1);

                if (addresses != null) {
//                Address returnedAddress = addresses.get(0);
                    String maddress = addresses.get(0).getAddressLine(0);
                    StringBuilder strReturnedAddress = new StringBuilder("");

                /*for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }*/
                    strAdd = String.valueOf(maddress);
//                strAdd = strReturnedAddress.toString();
//                Log.w(TAG, "My Current loction address"+strReturnedAddress.toString());
//                Log.w(TAG, "My Current loction address--->"+returnedAddress.getSubAdminArea().toString());
                } else {
                    Log.w(TAG, "My Current loction address--->" + "No Address returned!");
                }
            } else {
                strAdd = String.valueOf("null");


            }


        } catch (Exception e) {
            e.printStackTrace();
//            Log.w(TAG,"My Current loction address--->"+ "Canont get Address!");
        }
        return strAdd;
    }
}
