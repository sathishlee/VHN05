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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.ProfilePresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.view.ProfileViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    ImageView img_camera, img_gallery;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int RequestPermissionCode = 7;


    String userChoosenTask, str_mPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);
        if (CheckingPermissionIsEnabledOrNot()) {
            Toast.makeText(VhnProfile.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        String itemTitle = getIntent().getStringExtra(TITLE);
        toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar_layout.setTitle(itemTitle);
        toolbar_layout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
    }

    private void onClickListner() {
        user_profile_photo.setOnClickListener(this);
    }

    private void initUI() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        context = VhnProfile.this;

        profilePresenter = new ProfilePresenter(VhnProfile.this, this);

        profilePresenter.getVHNProfile(preferenceData.getVhnCode(), preferenceData.getVhnId());
        user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        user_name = (TextView) findViewById(R.id.user_name);
        txt_vhn_id = (TextView) findViewById(R.id.txt_vhn_id);
        address = (TextView) findViewById(R.id.address);
        phc_name = (TextView) findViewById(R.id.phc_name);
        tvNumber5 = (TextView) findViewById(R.id.tvNumber5);
        district_name = (TextView) findViewById(R.id.district_name);
        tvNumber1 = (TextView) findViewById(R.id.tvNumber1);


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
        inflater.inflate(R.menu.main, menu);
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
            if (status.equalsIgnoreCase("1")) {
                JSONObject editprofile = jsonObject.getJSONObject("EditProfile");

                user_name.setText(editprofile.getString("vhnName"));
                txt_vhn_id.setText(editprofile.getString("vhnCode"));
                address.setText(editprofile.getString("vhnAddress"));
                phc_name.setText(editprofile.getString("hscName"));
                district_name.setText(editprofile.getString("vhnDistrict"));
                tvNumber1.setText(editprofile.getString("vhnMobile"));
                tvNumber5.setText(editprofile.getString("vhnBlock"));

                str_mPhoto = editprofile.getString("vphoto");

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

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
