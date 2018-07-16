package com.unicef.vhn.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.ProfilePresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.realmDbModel.DashBoardRealmModel;
import com.unicef.vhn.realmDbModel.VhnProfileRealmModel;
import com.unicef.vhn.realmDbModel.VisitListRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.ProfileViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_SETTINGS;


/**
 * Created by priyan on 2/6/2018.
 */

public class VhnProfile extends AppCompatActivity implements ProfileViews, View.OnClickListener {

    public static final String TITLE = "Profile";
    public static final String UPLOAD_IMAGE = "user_profile_photo";

    private static final int SELECT_FILE = 2;
    private static final int REQUEST_CAMERA = 3;


    CollapsingToolbarLayout toolbar_layout;
    ImageView user_profile_photo;

    Intent intent;
    Context context;
    ProgressDialog pDialog;
    ProfilePresenter profilePresenter;
    PreferenceData preferenceData;
    TextView user_name, txt_vhn_id, address, phc_name, tvNumber5, district_name, tvNumber1;
    EditText edt_user_name, edt_vhn_id, edt_address, edt_phc_name, edt_Number5, edt_district_name, edt_Number1;
    Button butSubmit,butCancel;
    ImageView img_camera, img_gallery;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int RequestPermissionCode = 7;


    String userChoosenTask, str_mPhoto;

    CheckNetwork checkNetwork;
    Realm realm;
    boolean isOffline;
    VhnProfileRealmModel vhnProfileRealmModel;
    FloatingActionButton fab;
    //LinearLayout viewProfile,viewProfileEdit;
boolean isEditProfile=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.layout_profile);
        if (CheckingPermissionIsEnabledOrNot()) {
            Toast.makeText(VhnProfile.this, "All Permissions Granted Successfully",
                    Toast.LENGTH_LONG).show();
        }

        // If, If permission is not enabled then else condition will execute.
        else {

            //Calling method to enable permission.
            RequestMultiplePermission();

        }
        initActivityTransitions();
        showActionBar();
        initUI();
        onClickListner();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetwork.isNetworkAvailable()) {

                    edt_address.setVisibility(View.VISIBLE);
                    address.setVisibility(View.GONE);
                    edt_Number1.setVisibility(View.VISIBLE);
                    tvNumber1.setVisibility(View.GONE);
                    butSubmit.setVisibility(View.VISIBLE);
                    butCancel.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getApplicationContext(),"You can't edit your profile,\n No Internert connection",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordAudioPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean SendSMSPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean GetAccountsPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && RecordAudioPermission && SendSMSPermission && GetAccountsPermission) {

//                        Toast.makeText(ImageSelectedActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
//                        Toast.makeText(ImageSelectedActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(VhnProfile.this, new String[]
                {CAMERA,
                        RECORD_AUDIO,
                        SEND_SMS,
                        GET_ACCOUNTS,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,
                        INTERNET,
                        WRITE_SETTINGS}, RequestPermissionCode);

    }

    private boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        int FivePermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int SixPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int SevenPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int EightPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_SETTINGS);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FivePermissionResult == PackageManager.PERMISSION_GRANTED &&
                SixPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SevenPermissionResult == PackageManager.PERMISSION_GRANTED &&
                EightPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    private void showActionBar() {
        ViewCompat.setTransitionName(findViewById(R.id.app_bar), UPLOAD_IMAGE);
        supportPostponeEnterTransition();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void onClickListner() {
        user_profile_photo.setOnClickListener(this);
        butSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_address.getText().toString().isEmpty() && edt_Number1.getText().toString().isEmpty()
                        && edt_Number1.getText().toString().length()!=10) {
                    profilePresenter.postVHNProfile(preferenceData.getVhnId(), preferenceData.getVhnCode(),
                            edt_address.getText().toString(), edt_Number1.getText().toString());
                }else{
                    if (edt_address.getText().toString().isEmpty()){
                        edt_address.setError("enter address");
                    }  else if (edt_Number1.getText().toString().isEmpty()){
                        edt_Number1.setError("enter mobile number");
                    }else if (edt_Number1.getText().toString().length()!=10){
                        edt_Number1.setError("enter valid mobile number");
                    }
                }
            }
        });
        butCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_address.setVisibility(View.GONE);
                address.setVisibility(View.VISIBLE);
                edt_Number1.setVisibility(View.GONE);
                tvNumber1.setVisibility(View.VISIBLE);
                butSubmit.setVisibility(View.GONE);
                butCancel.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initUI() {
        checkNetwork = new CheckNetwork(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        context = VhnProfile.this;

        profilePresenter = new ProfilePresenter(VhnProfile.this, this);
        if (checkNetwork.isNetworkAvailable()) {
            profilePresenter.getVHNProfile(preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            isOffline = false;
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        butSubmit = (Button) findViewById(R.id.but_submit);
        butCancel = (Button) findViewById(R.id.but_cancel);
        user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        user_name = (TextView) findViewById(R.id.user_name);
        txt_vhn_id = (TextView) findViewById(R.id.txt_vhn_id);
        address = (TextView) findViewById(R.id.address);
        phc_name = (TextView) findViewById(R.id.phc_name);
        tvNumber5 = (TextView) findViewById(R.id.tvNumber5);
        district_name = (TextView) findViewById(R.id.district_name);
        tvNumber1 = (TextView) findViewById(R.id.tvNumber1);

        edt_address= (EditText) findViewById(R.id.edaddress);
        edt_Number1= (EditText) findViewById(R.id.edNumber1);

        if (isOffline) {
            setValuesToUI();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Record Not Found");
            builder.create();
        }


    }


    @Override
    public void onBackPressed() {
        intent = new Intent(VhnProfile.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

  /*  private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.primary_dark);
        int primary = getResources().getColor(R.color.primary);
        toolbar_layout.setContentScrimColor(palette.getMutedColor(primary));
        toolbar_layout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground((FloatingActionButton) findViewById(R.id.fab), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.accent));

        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }
*/

    @Override
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.hide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }

    @Override
    public void successViewProfile(String response) {
        Log.d("ProfileActivity success", response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("message");
            RealmResults<VhnProfileRealmModel> motherListAdapterRealmModel = realm.where(VhnProfileRealmModel.class).findAll();
            Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(VhnProfileRealmModel.class);
                }
            });
            if (status.equalsIgnoreCase("1")) {
                realm.beginTransaction();
                vhnProfileRealmModel = realm.createObject(VhnProfileRealmModel.class);

                JSONObject editprofile = jsonObject.getJSONObject("EditProfile");


                vhnProfileRealmModel.setVhnId(editprofile.getString("vhnId"));
                vhnProfileRealmModel.setVhnName(editprofile.getString("vhnName"));
                vhnProfileRealmModel.setVhnCode(editprofile.getString("vhnCode"));
                vhnProfileRealmModel.setVphoto(editprofile.getString("vphoto"));
                vhnProfileRealmModel.setVhnAddress(editprofile.getString("vhnAddress"));
                vhnProfileRealmModel.setDistCode(editprofile.getString("distCode"));
                vhnProfileRealmModel.setVhnDistrict(editprofile.getString("vhnDistrict"));
                vhnProfileRealmModel.setVhnBlock(editprofile.getString("vhnBlock"));
                vhnProfileRealmModel.setHscName(editprofile.getString("hscName"));
                vhnProfileRealmModel.setVhnMobile(editprofile.getString("vhnMobile"));


                realm.commitTransaction();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValuesToUI();
    }

    private void setValuesToUI() {
        realm.beginTransaction();

        RealmResults<VhnProfileRealmModel> vhnProfileRealmModelRealmResults = realm.where(VhnProfileRealmModel.class).findAll();

        for (int i = 0; i < vhnProfileRealmModelRealmResults.size(); i++) {

            VhnProfileRealmModel model = vhnProfileRealmModelRealmResults.get(i);

            user_name.setText(model.getVhnName());
            txt_vhn_id.setText(model.getVhnId());
            address.setText(model.getVhnAddress());
            edt_address.setText(model.getVhnAddress());
            phc_name.setText(model.getHscName());
            district_name.setText(model.getVhnDistrict());
            tvNumber1.setText(model.getVhnMobile());
            edt_Number1.setText(model.getVhnMobile());
            tvNumber5.setText(model.getVhnBlock());

            str_mPhoto = model.getVphoto();

            Log.d("vphoto-->", Apiconstants.PHOTO_URL + str_mPhoto);

            Picasso.with(context)
                    .load(Apiconstants.PHOTO_URL + str_mPhoto)
                    .placeholder(R.drawable.ln_logo)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.ln_logo)
                    .into(user_profile_photo);
        }
        realm.commitTransaction();
    }


    @Override
    public void errorViewProfile(String response) {


    }

    @Override
    public void successUploadPhoto(String response) {
        Log.d("Image upload success", response);
        pDialog.dismiss();
    }

    @Override
    public void errorUploadPhoto(String response) {
        Log.d("Image upload error", response);
        pDialog.dismiss();
    }

    @Override
    public void successUploadProfile(String response) {
        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(),VhnProfile.class));
    }

    @Override
    public void errorUploadProfile(String response) {
        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.user_profile_photo:
                selectImage();
                break;

        }

    }

    private void selectImage() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setTitle("Upload Photo");
        bottomSheetDialog.show();


        bottomSheetDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                Log.d("BottomSheetVideoBtn", "called");
                return false;
            }
        });

        img_camera = (ImageView) view.findViewById(R.id.img_camera);
        img_gallery = (ImageView) view.findViewById(R.id.img_gallery);

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();

            }
        });

        img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryIntent();

            }
        });
//        bottomSheetDialog.hide();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

     /*   if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                //displaying selected image to imageview
                user_profile_photo.setImageBitmap(bitmap);

                //calling the method uploadUserProfilePhoto to upload image
                uploadUserProfilePhoto(bitmap);
                Log.d("bitmap",bitmap.getByteCount()+"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        user_profile_photo.setImageBitmap(thumbnail);
        profilePresenter.uploadUserProfilePhoto(preferenceData.getVhnCode(), preferenceData.getVhnId(), thumbnail);

    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        user_profile_photo.setImageBitmap(bm);
        profilePresenter.uploadUserProfilePhoto(preferenceData.getVhnCode(), preferenceData.getVhnId(), bm);
    }

    private void uploadBitmap(Bitmap bitmap) {

    }


}
