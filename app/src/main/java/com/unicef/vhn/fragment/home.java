package com.unicef.vhn.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.HomePresenter;
import com.unicef.vhn.R;
//import com.unicef.vhn.activity.HighRiskListActivity;
import com.unicef.vhn.activity.ANTT1MothersList;
import com.unicef.vhn.activity.ANTT2MothersList;
import com.unicef.vhn.activity.MotherList.AllMotherListActivity;
import com.unicef.vhn.activity.MotherList.InfanentTreamListActivity;
import com.unicef.vhn.activity.NoInternetConnectionActivity;
import com.unicef.vhn.activity.PNHBNCDueListActivity;
import com.unicef.vhn.activity.SosAlertListActivity;
import com.unicef.vhn.activity.VhnProfile;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.realmDbModel.DashBoardRealmModel;
import com.unicef.vhn.service.PlayAudio;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.utiltiy.RoundedTransformation;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;


public class home extends Fragment implements MotherListsViews {
    ImageView img_mother_count, img_high_risk_count, img_infant_count, userImageProfile;
    public TextView txt_mother_count, txt_high_risk_count, txt_infants_count, txt_sos_count;
    Button but_an_mother_total_count, but_an_mother_high_risk_count, but_an_mother_pn_hbnc_totlal_count,
            but_an_mother_pn_hbnc_term_preterm_count;
    TextView txt_antt_1_due, txt_antt_2_due, txt_pnhbnc_due;

    private ViewFlipper mFlipper;

    TextView txt_vhn_name, txt_hsc, txt_phc, txt_block, txt_address;
    ProgressDialog pDialog;
    HomePresenter homePresenter;
    PreferenceData preferenceData;
    LinearLayout ll_sos_view;
    CheckNetwork checkNetwork;
    CardView profile;
    String str_mPhoto;
//    Ringtone ringtone;
    Context context;
    boolean isoffline = false;
//    TextView txt_no_internet;
    Realm realm;

    MediaPlayer mp;
    public static home newInstance() {
        home fragment = new home();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_new, container, false);
        realm = RealmController.with(getActivity()).getRealm(); // opens "myrealm.realm"

//        checkInterNetConnection();
        initUI(view);

        profile = (CardView) view.findViewById(R.id.user_profile_photo);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VhnProfile.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        img_mother_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "mother_count";
                AppConstants.MOTHER_LIST_TITLE = "All Mother List";
                if (!preferenceData.getFilterStatus()) {
//                if (preferenceData.getFilterStatus()) {
                    preferenceData.setFilterStatus(false);
                    preferenceData.setTermister("All");
                    preferenceData.setVillageName("All");
                    preferenceData.setHighRiskStatus(false);
                    preferenceData.setDescendingStatus(false);
                    preferenceData.setTermisterPosition(0);
                } else {
                    preferenceData.setFilterStatus(false);
                    preferenceData.setTermister("All");
                    preferenceData.setVillageName("All");
                    preferenceData.setHighRiskStatus(false);
                    preferenceData.setDescendingStatus(false);
                    preferenceData.setTermisterPosition(0);

//                    preferenceData.setFilterStatus(true);

                }
//                startActivity(new Intent(getActivity(), MotherHighRiskListActivity.class));
                startActivity(new Intent(getActivity(), AllMotherListActivity.class));

            }
        });

        ll_sos_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "sos_count";
                AppConstants.MOTHER_LIST_TITLE = "SOS List";
//                ringtone.stop();
                startActivity(new Intent(getActivity(), SosAlertListActivity.class));

            }
        });

        img_high_risk_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "risk_count";
                AppConstants.MOTHER_LIST_TITLE = "High Risk Mother List";

//                startActivity(new Intent(getActivity(), MotherHighRiskListActivity.class));
                startActivity(new Intent(getActivity(), AllMotherListActivity.class));

            }
        });

        img_infant_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "infant_count";
                AppConstants.MOTHER_LIST_TITLE = "Infant List";

//                startActivity(new Intent(getActivity(), TreamPreTreamListActivity.class));
                startActivity(new Intent(getActivity(), InfanentTreamListActivity.class));
            }
        });

        but_an_mother_total_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "an_mother_total_count";
                AppConstants.MOTHER_LIST_TITLE = "AN Mother List";

//                startActivity(new Intent(getActivity(), MotherHighRiskListActivity.class));
                startActivity(new Intent(getActivity(), AllMotherListActivity.class));
            }
        });

        but_an_mother_high_risk_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "high_risk_count";
                AppConstants.MOTHER_LIST_TITLE = "AN High Risk Mother List";

//                startActivity(new Intent(getActivity(), MotherHighRiskListActivity.class));
                startActivity(new Intent(getActivity(), AllMotherListActivity.class));

            }
        });
        but_an_mother_pn_hbnc_totlal_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "pn_hbnc_totlal_coun";
                AppConstants.MOTHER_LIST_TITLE = "PN/HBNC Mother List";

//                startActivity(new Intent(getActivity(), PNHBNCListActivity.class));
                startActivity(new Intent(getActivity(), AllMotherListActivity.class));
            }
        });
        but_an_mother_pn_hbnc_term_preterm_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "pn_hbnc_term_preterm_count";
                AppConstants.MOTHER_LIST_TITLE = "TERM/PRE TERM List";

//                startActivity(new Intent(getActivity(), TreamPreTreamListActivity.class));
                startActivity(new Intent(getActivity(), InfanentTreamListActivity.class));
            }
        });
        txt_antt_1_due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.ANTT_1_LIST = "TT1_List";
                AppConstants.ANTT_1_TITLE = "AN TT 1 Due List";

                startActivity(new Intent(getActivity(), ANTT1MothersList.class));
            }
        });

        txt_antt_2_due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.ANTT_2_LIST = "TT2_List";
                AppConstants.ANTT_2_TITLE = "AN TT 2 Due List";

                startActivity(new Intent(getActivity(), ANTT2MothersList.class));
            }
        });

        txt_pnhbnc_due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppConstants.ANTT_2_LIST="TT2_List";
//                AppConstants.ANTT_2_TITLE="AN TT 2 Due List";

                startActivity(new Intent(getActivity(), PNHBNCDueListActivity.class));
            }
        });


        return view;

    }

    private void checkInterNetConnection() {
        checkNetwork = new CheckNetwork(getActivity());
        if (checkNetwork.isNetworkAvailable()) {
            Log.w(home.class.getSimpleName(), "Is" + checkNetwork.isNetworkAvailable());
        } else {
            Log.w(home.class.getSimpleName(), "Is" + checkNetwork.isNetworkAvailable());
            startActivity(new Intent(getActivity(), NoInternetConnectionActivity.class));
        }
    }

    private void initUI(View view) {


        mp = MediaPlayer.create(getActivity(), R.raw.ambulance_alert);
        mp.pause();
        checkNetwork = new CheckNetwork(getActivity());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(getActivity());
        homePresenter = new HomePresenter(getActivity(), this);
        context = getActivity();

        userImageProfile = (ImageView) view.findViewById(R.id.userImageProfile);

        img_mother_count = (ImageView) view.findViewById(R.id.img_mother_count);

//        txt_no_internet = view.findViewById(R.id.txt_no_internet);
//        txt_no_internet.setVisibility(View.GONE);

        ll_sos_view = view.findViewById(R.id.ll_sos_view);
        txt_vhn_name = view.findViewById(R.id.txt_vhn_name);
        txt_hsc = view.findViewById(R.id.txt_hsc);
        txt_phc = view.findViewById(R.id.txt_phc);
        txt_block = view.findViewById(R.id.txt_block);
        txt_address = view.findViewById(R.id.txt_address);
        txt_mother_count = view.findViewById(R.id.txt_mother_count);
        txt_high_risk_count = view.findViewById(R.id.txt_high_risk_count);
        txt_infants_count = view.findViewById(R.id.txt_infants_count);
        txt_sos_count = view.findViewById(R.id.txt_sos_count);

        img_mother_count = (ImageView) view.findViewById(R.id.img_mother_count);
        img_high_risk_count = (ImageView) view.findViewById(R.id.img_high_risk_count);
        img_infant_count = (ImageView) view.findViewById(R.id.img_infant_count);

        but_an_mother_total_count = view.findViewById(R.id.but_an_mother_total_count);
        but_an_mother_high_risk_count = view.findViewById(R.id.but_an_mother_high_risk_count);
        but_an_mother_pn_hbnc_totlal_count = view.findViewById(R.id.but_an_mother_pn_hbnc_totlal_count);
//        but_an_mother_pn_hbnc_totlal_count = view.findViewById(R.id.but_an_mother_pn_hbnc_totlal_count);
        but_an_mother_pn_hbnc_term_preterm_count = view.findViewById(R.id.but_an_mother_pn_hbnc_term_preterm_count);

        txt_antt_1_due = view.findViewById(R.id.txt_antt_1_due);
        txt_antt_2_due = view.findViewById(R.id.txt_antt_2_due);
        txt_pnhbnc_due = view.findViewById(R.id.txt_pnhbnc_due);

        if (checkNetwork.isNetworkAvailable()) {
            homePresenter.getDashBoard(preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
//            Log.w(home.class.getSimpleName(), "Is" + checkNetwork.isNetworkAvailable());
//            startActivity(new Intent(getActivity(), NoInternetConnectionActivity.class));
            isoffline = true;
        }
        mFlipper = ((ViewFlipper) view.findViewById(R.id.flipper));
        if (isoffline) {
//            txt_no_internet.setVisibility(View.GONE);
            showOfflineData();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Record Not Found");
            builder.create();
        }
    }

    private void showOfflineData() {
        Log.e(home.class.getSimpleName(), "your app is now OFF LINE");
        realm.beginTransaction();
        RealmResults<DashBoardRealmModel> userInfoRealmResult = realm.where(DashBoardRealmModel.class).findAll();
        for (int i = 0; i < userInfoRealmResult.size(); i++) {
            DashBoardRealmModel model = userInfoRealmResult.get(i);

            Log.e("getMothersCount", model.getMothersCount() + "");
            Log.e("getRiskMothersCount", model.getRiskMothersCount() + "");
            Log.e("getInfantCount", model.getInfantCount() + "");
            Log.e("getSosCount", model.getSosCount() + "");
            txt_mother_count.setText(model.getMothersCount() + "");
            txt_high_risk_count.setText(model.getRiskMothersCount() + "");
            txt_infants_count.setText(model.getInfantCount() + "");
            txt_sos_count.setText(model.getSosCount() + "");
            if (model.getSosCount() == 0) {
//                mp.stop();
                Intent objIntent = new Intent(getActivity(), PlayAudio.class);
                getActivity().stopService(objIntent);
            } else {
                mFlipper.startFlipping();
                mFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                mFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
//                mp.start();
                Intent objIntent = new Intent(getActivity(), PlayAudio.class);
                getActivity().startService(objIntent);
            }
            txt_antt_1_due.setText(model.getANTT1() + "");
            txt_antt_2_due.setText(model.getANTT2() + "");
            txt_pnhbnc_due.setText(model.getPnhbncCount() + "");

            but_an_mother_total_count.setText("Total: " + model.getANMothersCount() + "");
            but_an_mother_high_risk_count.setText("High Risk: " + model.getANMotherRiskCount() + "");
            but_an_mother_pn_hbnc_totlal_count.setText("Total: " + model.getPNMotherCount() + "");
            but_an_mother_pn_hbnc_term_preterm_count.setText("Term/Preterm: " + model.getTermsCount() + "");

//            JSONObject mJsnobject_phcDetails = mJsnobject.getJSONObject("phcDetails");
//                JSONObject mJsnobject_phcDetails = mJsnobject.getJSONObject("phcDetails");
            txt_vhn_name.setText(model.getVhnName());
            txt_phc.setText(model.getPhcName());
            txt_hsc.setText(model.getFacilityName());
            txt_block.setText(model.getBlock());
            txt_address.setText(model.getDistrict());

            str_mPhoto = model.getVphoto();
            if (TextUtils.isEmpty(str_mPhoto)) {
                userImageProfile.setImageResource(R.drawable.ic_nurse);
            } else {
                Picasso.with(context)
                        .load(Apiconstants.PHOTO_URL + str_mPhoto)
                        .placeholder(R.drawable.ic_nurse)
                        .fit()
                        .centerCrop()
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .transform(new RoundedTransformation(90, 4))
                        .error(R.drawable.ic_nurse)
                        .into(userImageProfile);


            }
        }

        realm.commitTransaction();

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
    public void showLoginSuccess(String response) {
        Log.e(home.class.getSimpleName(), "Response success" + response);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");

            if (status.equalsIgnoreCase("1")) {
                txt_mother_count.setText(mJsnobject.getString("mothersCount"));
                txt_high_risk_count.setText(mJsnobject.getString("riskMothersCount"));
                txt_infants_count.setText(mJsnobject.getString("infantCount"));
                txt_sos_count.setText(mJsnobject.getString("sosCount"));

                if (mJsnobject.getString("sosCount").equalsIgnoreCase("0")) {
//                    ringtone.stop();
                }
                 else{
//                    mFlipper.startFlipping();
//                    mFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
//                    mFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
//                    ringtone.play();

                }

                txt_antt_1_due.setText(mJsnobject.getString("ANTT1"));
                txt_antt_2_due.setText(mJsnobject.getString("ANTT2"));
                txt_pnhbnc_due.setText(mJsnobject.getString("pnhbncCount"));


                but_an_mother_total_count.setText("Total: " + mJsnobject.getString("ANMothersCount"));
                but_an_mother_high_risk_count.setText("High Risk: " + mJsnobject.getString("ANMotherRiskCount"));
                but_an_mother_pn_hbnc_totlal_count.setText("Total: " + mJsnobject.getString("PNMotherCount"));
                but_an_mother_pn_hbnc_term_preterm_count.setText("Term/Preterm: " + mJsnobject.getString("termsCount"));

                RealmResults<DashBoardRealmModel> DashBoardRealmResult = realm.where(DashBoardRealmModel.class).findAll();
                Log.e("Realm size ---->", DashBoardRealmResult.size() + "");
                if (DashBoardRealmResult.size() != 0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(DashBoardRealmModel.class);
                        }
                    });
                } else {
                    Log.e("Realm size  ---->", DashBoardRealmResult.size() + "");
                }
                Log.e("After Realm size  ---->", DashBoardRealmResult.size() + "");


                //create new realm Table
                realm.beginTransaction();       //create or open
                DashBoardRealmModel dashBoardRealmModel = realm.createObject(DashBoardRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database
                dashBoardRealmModel.setMothersCount(Integer.parseInt(mJsnobject.getString("mothersCount")));
                dashBoardRealmModel.setRiskMothersCount(Integer.parseInt(mJsnobject.getString("riskMothersCount")));
                dashBoardRealmModel.setInfantCount(Integer.parseInt(mJsnobject.getString("infantCount")));
                dashBoardRealmModel.setSosCount(Integer.parseInt(mJsnobject.getString("sosCount")));
                dashBoardRealmModel.setANTT1(Integer.parseInt(mJsnobject.getString("ANTT1")));
                dashBoardRealmModel.setANTT2(Integer.parseInt(mJsnobject.getString("ANTT2")));
                dashBoardRealmModel.setPnhbncCount(Integer.parseInt(mJsnobject.getString("pnhbncCount")));

                dashBoardRealmModel.setANMothersCount(Integer.parseInt(mJsnobject.getString("ANMothersCount")));
                dashBoardRealmModel.setANMotherRiskCount(Integer.parseInt(mJsnobject.getString("ANMotherRiskCount")));
                dashBoardRealmModel.setPNMotherCount(Integer.parseInt(mJsnobject.getString("PNMotherCount")));
                dashBoardRealmModel.setTermsCount(Integer.parseInt(mJsnobject.getString("termsCount")));

                JSONObject jobj__phcDetails = mJsnobject.getJSONObject("phcDetails");
                dashBoardRealmModel.setVhnName(jobj__phcDetails.getString("vhnName"));
                dashBoardRealmModel.setPhcName(jobj__phcDetails.getString("phcName"));
                dashBoardRealmModel.setFacilityName(jobj__phcDetails.getString("facilityName"));
                dashBoardRealmModel.setBlock(jobj__phcDetails.getString("block"));
                dashBoardRealmModel.setDistrict(jobj__phcDetails.getString("District"));
                if (jobj__phcDetails.getString("vphoto").equalsIgnoreCase("null")) {
                    dashBoardRealmModel.setVphoto("");
                }
                dashBoardRealmModel.setVphoto(jobj__phcDetails.getString("vphoto"));
                preferenceData.setPhoto(jobj__phcDetails.getString("vphoto"));

                realm.commitTransaction(); //close table

                JSONObject mJsnobject_phcDetails = mJsnobject.getJSONObject("phcDetails");
//                JSONObject mJsnobject_phcDetails = mJsnobject.getJSONObject("phcDetails");
                txt_vhn_name.setText(mJsnobject_phcDetails.getString("vhnName"));
                txt_phc.setText(mJsnobject_phcDetails.getString("phcName"));
                txt_hsc.setText(mJsnobject_phcDetails.getString("facilityName"));
                txt_block.setText(mJsnobject_phcDetails.getString("block"));
                txt_address.setText(mJsnobject_phcDetails.getString("District"));

                if (mJsnobject_phcDetails.getString("vphoto").equalsIgnoreCase("null")) {
                    str_mPhoto = "";
                    preferenceData.setVhnPhoto("");
                } else {
                    str_mPhoto = mJsnobject_phcDetails.getString("vphoto");
                    preferenceData.setVhnPhoto(mJsnobject_phcDetails.getString("vphoto"));


                }
                Log.d("vphoto-->", Apiconstants.PHOTO_URL + str_mPhoto);


                if (!TextUtils.isEmpty(str_mPhoto)) {
                    Picasso.with(context)
                            .load(Apiconstants.PHOTO_URL + str_mPhoto)
                            .placeholder(R.drawable.ic_nurse)
                            .fit()
                            .centerCrop()
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .transform(new RoundedTransformation(90, 4))
                            .error(R.drawable.ic_nurse)
                            .into(userImageProfile);
                } else {
                    userImageProfile.setImageResource(R.drawable.ic_nurse);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setValueToUI();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void showLoginError(String response) {
        Log.e(home.class.getSimpleName(), "Response Error" + response);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(response);
        builder.create();
    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }

    private void setValueToUI() {
        Log.e(home.class.getSimpleName(), "your app is now ON LINE");

        realm.beginTransaction();
        RealmResults<DashBoardRealmModel> userInfoRealmResult = realm.where(DashBoardRealmModel.class).findAll();
        for (int i = 0; i < userInfoRealmResult.size(); i++) {
            DashBoardRealmModel model = userInfoRealmResult.get(i);

            Log.e("getMothersCount", model.getMothersCount() + "");
            Log.e("getRiskMothersCount", model.getRiskMothersCount() + "");
            Log.e("getInfantCount", model.getInfantCount() + "");
            Log.e("getSosCount", model.getSosCount() + "");
            txt_mother_count.setText(model.getMothersCount() + "");
            txt_high_risk_count.setText(model.getRiskMothersCount() + "");
            txt_infants_count.setText(model.getInfantCount() + "");
            txt_sos_count.setText(model.getSosCount() + "");
//            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//            Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), uri);
            if (model.getSosCount() == 0) {
//                ringtone.stop();
//                mp.stop();

                Intent objIntent = new Intent(getActivity(), PlayAudio.class);
                if(  getActivity().startService(objIntent) != null) {
                    Toast.makeText(getActivity(), "Service is already running", Toast.LENGTH_SHORT).show();
                    getActivity().stopService(objIntent);
                }
                else {
                    Toast.makeText(getActivity(), "There is no service running, starting service..", Toast.LENGTH_SHORT).show();
                }
//                getActivity().stopService(objIntent);
            }

            else {
                mFlipper.startFlipping();
                mFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                mFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
//                mp = MediaPlayer.create(getActivity(), R.raw.ambulance_alert);
//                mp.start();
//                ringtone.play();
                Intent objIntent = new Intent(getActivity(), PlayAudio.class);

                if(  getActivity().startService(objIntent) != null) {
                    Toast.makeText(getActivity(), "Service is already running", Toast.LENGTH_SHORT).show();
//                    getActivity().stopService(objIntent);
                }
                else {
                    getActivity().startService(objIntent);
                    Toast.makeText(getActivity(), "There is no service running, starting service..", Toast.LENGTH_SHORT).show();
                }
            }
            txt_antt_1_due.setText(model.getANTT1() + "");
            txt_antt_2_due.setText(model.getANTT2() + "");
            txt_pnhbnc_due.setText(model.getPnhbncCount() + "");

            but_an_mother_total_count.setText("Total: " + model.getANMothersCount() + "");
            but_an_mother_high_risk_count.setText("High Risk: " + model.getANMotherRiskCount() + "");
            but_an_mother_pn_hbnc_totlal_count.setText("Total: " + model.getPNMotherCount() + "");
            but_an_mother_pn_hbnc_term_preterm_count.setText("Term/Preterm: " + model.getTermsCount() + "");

//            JSONObject mJsnobject_phcDetails = mJsnobject.getJSONObject("phcDetails");
//                JSONObject mJsnobject_phcDetails = mJsnobject.getJSONObject("phcDetails");
            txt_vhn_name.setText(model.getVhnName());
            txt_phc.setText(model.getPhcName());
            txt_hsc.setText(model.getFacilityName());
            txt_block.setText(model.getBlock());
            txt_address.setText(model.getDistrict());

            str_mPhoto = model.getVphoto();
            Log.d("vphoto-->", Apiconstants.PHOTO_URL + str_mPhoto);

            /*Picasso.with(context)
                    .load(Apiconstants.PHOTO_URL + str_mPhoto)
                    .placeholder(R.drawable.ic_nurse)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .transform(new RoundedTransformation(90, 4))
                    .error(R.drawable.ic_nurse)
                    .into(userImageProfile);*/
        }

        realm.commitTransaction();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent objIntent = new Intent(getActivity(), PlayAudio.class);
        if(  getActivity().startService(objIntent) != null) {
            Toast.makeText(getActivity(), "Service is already running", Toast.LENGTH_SHORT).show();
            getActivity().stopService(objIntent);
        }
        else {
            Toast.makeText(getActivity(), "There is no service running, starting service..", Toast.LENGTH_SHORT).show();
        }
    }
    /*  @Override
      public void onDestroy() {
          super.onDestroy();
          realm.close();
      }

      @Override
      public void onResume() {
          super.onResume();
          realm = RealmController.with(getActivity()).getRealm(); // opens "myrealm.realm"

      }*/
    @Override
    public void onResume() {
        super.onResume();
//      realm = RealmController.with(getActivity()).getRealm(); // opens "myrealm.realm"
//        AppConstants.ISQUERYFILTER=false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppConstants.ISHOMEALREDYCLICKED=true;

    }
}
