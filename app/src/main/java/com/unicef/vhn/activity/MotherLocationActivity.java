package com.unicef.vhn.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
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
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.LocationPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.LocationRequestModel;
import com.unicef.vhn.view.LocationViews;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MotherLocationActivity extends FragmentActivity implements LocationViews, OnMapReadyCallback, View.OnClickListener {

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

    private String GOOGLE_PLACES_API_KEY = "AIzaSyCnzEjpN8svPol7UhuEf-3XBQt4kC-dkOA";

    private List<Marker> markers;

    TextView txt_username, txt_picme_id;

    String strLat,strLon, motherLatitude, motherLongitude, vhnLatitude, vhnLongitude;

    String strMotherloc, strVHNloc, strmAdd, strvAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bindActivity();

    }

    private void bindActivity() {

        applicationContext = this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait ...");

        txt_username = (TextView)findViewById(R.id.txt_username);
        txt_picme_id = (TextView)findViewById(R.id.txt_picme_id);

        preferenceData = new PreferenceData(this);
//        this.trackings = trackings;

        locationPresenter = new LocationPresenter(MotherLocationActivity.this, this);
        locationPresenter.getMotherLocatin(preferenceData.getVhnCode(),preferenceData.getVhnId(), AppConstants.SELECTED_MID);



        findViewById(R.id.get_directions).setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mother Location");
        toolbar.setNavigationIcon(R.drawable.left_arrow_key);
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
        LatLng latLng = new LatLng(13.00095, 80.2575);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Adyar, Chennai, Tamil Nadu"));


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
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
                getResultLocation();
//                getDirections();
                break;
        }
    }
    public void getDirections(){
        Log.e("Mlatitude--", motherLatitude);
        Log.e("Mlong--", motherLongitude);
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+motherLatitude+","+motherLongitude);


                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(applicationContext.getPackageManager()) != null) {
                    applicationContext. startActivity(mapIntent);
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

        Log.d("Mother---",strmAdd);
        Log.d("VHN--",strvAdd);
        if (directionsResult != null) {
            addPolyline(directionsResult, mMap);
//            addMarkersToMap(directionsResult, mMap);
            addMarkersToMap(directionsResult,mMap);
            positionCamera(directionsResult.routes[overview], mMap);
        }
    }

    public  void addMarkerMap(DirectionsResult directionsResult, GoogleMap mMap){
//        Marker markerSrc = mMap.addMarker(new MarkerOptions().position(new LatLng(directionsResult.routes[overview])))
    }

    private void addMarkersToMap(DirectionsResult directionsResult, GoogleMap mMap) {
        Marker markerSrc = mMap.addMarker(new MarkerOptions().position(new LatLng(directionsResult.routes[overview].legs[overview].startLocation.lat, directionsResult.routes[overview].legs[overview].startLocation.lng)).title(directionsResult.routes[overview].legs[overview].startAddress));
        Marker markerDes = mMap.addMarker(new MarkerOptions().position(new LatLng(directionsResult.routes[overview].legs[overview].endLocation.lat, directionsResult.routes[overview].legs[overview].endLocation.lng)).title(directionsResult.routes[overview].legs[overview].endAddress).snippet(getEndLocationTitle(directionsResult)));
        markers = new ArrayList<>();
        markers.add(markerSrc);
        markers.add(markerDes);
    }

    private void addMarkersToMaplatlng(String motherLatitude, String motherLongitude, String vhnLatitude, String vhnLongitude) {
        double mlat = Double.parseDouble(motherLatitude);
        double mlong = Double.parseDouble(motherLongitude);
        double vlat = Double.parseDouble(vhnLatitude);
        double vlong = Double.parseDouble(vhnLongitude);

        LatLng mlatlng = new LatLng(mlat,mlong);
        strLat = String.valueOf(mlatlng);

        Log.d("Mother Location--->",strLat);
        LatLng vlatlng = new LatLng(vlat,vlong);

        strLon = String.valueOf(vlatlng);
        Log.d("VHN Location--->",strLon);

        MarkerOptions mothermarker = new MarkerOptions().position(new LatLng(mlat,mlong)).title(AppConstants.SELECTED_MID);
        MarkerOptions vhnmarker = new MarkerOptions().position(new LatLng(vlat,vlong)).title(preferenceData.getVhnName());

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

            List<Address> addresses = geocoder.getFromLocation(mlat,mlong,1);

            List<Address> addresses1 = geocoder.getFromLocation(vlat,vlong,1);

            if(addresses !=null){
                String maddress = addresses.get(0).getAddressLine(0);
                String mcity =  addresses.get(0).getLocality();
                String mstate = addresses.get(0).getAdminArea();
                String mcountry = addresses.get(0).getCountryName();
                String mpostalcode = addresses.get(0).getPostalCode();
                String mknownname = addresses.get(0).getFeatureName();

                strmAdd = String.valueOf(maddress+mcity+mstate);
            }

            if(addresses !=null){
                String vaddress = addresses1.get(0).getAddressLine(0);
                String vcity =  addresses1.get(0).getLocality();
                String vstate = addresses1.get(0).getAdminArea();
                String vcountry = addresses1.get(0).getCountryName();
                String vpostalcode = addresses1.get(0).getPostalCode();
                String vknownname = addresses1.get(0).getFeatureName();

                strvAdd = String.valueOf(vaddress+vcity+vstate);
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
        }
        catch(IOException e){
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

        Log.d("Origin",origin);
        Log.d("Destination", destination);

        DateTime now = new DateTime();
        try {
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .await();
        } catch (ApiException e) {
            Log.e("DirectionsResult1",e.toString());
            e.printStackTrace();
            showMessage(e.getMessage());
            return null;
        } catch (InterruptedException e) {
            Log.e("DirectionsResult2",e.toString());

            e.printStackTrace();
            showMessage(e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("DirectionsResult",e.toString());

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
        AppConstants.SELECTED_MID="0";

        Log.e(MotherLocationActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            mMap.clear();
            if (status.equalsIgnoreCase("1")) {
                Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                JSONObject mJsnobject_tracking = mJsnobject.getJSONObject("tracking");
                txt_username.setText(mJsnobject_tracking.getString("mName"));
                txt_picme_id.setText(mJsnobject_tracking.getString("mPicmeId"));



                motherLatitude = mJsnobject_tracking.getString("mLatitude");
                motherLongitude = mJsnobject_tracking.getString("mLongitude");
                vhnLatitude = mJsnobject_tracking.getString("vLatitude");
                vhnLongitude = mJsnobject_tracking.getString("vLongitude");

                addMarkersToMaplatlng(motherLatitude, motherLongitude, vhnLatitude, vhnLongitude);

//                JSONArray routeArray = mJsnobject.getJSONArray("mother_location");
//                for (int i = 0; i < routeArray.length(); i++) {
//
//                    LatLng latLng1=new LatLng(Double.parseDouble(routeArray.getJSONObject(i).getString("mLatitude")),Double.parseDouble(routeArray.getJSONObject(i).getString("mLongitude")));
//                    mMap.addMarker(new MarkerOptions().position(latLng1).title("Mother Location"));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 12));
////                    addMarkersToMap(Double.parseDouble(routeArray.getJSONObject(i).getString("mLatitude")),
////                            Double.parseDouble(routeArray.getJSONObject(i).getString("mLongitude")));
//                }
//
//                JSONArray routeArray1 = mJsnobject.getJSONArray("vhn_location");
//                for (int i = 0; i < routeArray1.length(); i++) {
//                    LatLng latLng2=new LatLng(Double.parseDouble(routeArray1.getJSONObject(i).getString("vLatitude")),Double.parseDouble(routeArray.getJSONObject(i).getString("vLongitude")));
//                    mMap.addMarker(new MarkerOptions().position(latLng2).title("VHN Location"));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 12));
////                    addMarkersToMap(Double.parseDouble(routeArray.getJSONObject(i).getString("mLatitude")),
////                            Double.parseDouble(routeArray.getJSONObject(i).getString("mLongitude")));
//                }


            }else{
                Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLocationError(String string) {
        AppConstants.SELECTED_MID="0";

        Log.e(MotherLocationActivity.class.getSimpleName(), "Response success" + string);

    }
}
