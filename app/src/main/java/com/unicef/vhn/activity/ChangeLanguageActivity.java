package com.unicef.vhn.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.R;
import com.unicef.vhn.utiltiy.LocaleHelper;

import java.util.Locale;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ChangeLanguageActivity extends AppCompatActivity {
    Locale mylocale;
    TextView tam, eng;
    PreferenceData preferenceData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        showActionBar();
        initUi();
    }

    public void initUi() {
        tam = (TextView) findViewById(R.id.tam);
        eng = (TextView) findViewById(R.id.eng);
        preferenceData = new PreferenceData(this);

        tam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeLanguageActivity.this);
                builder.setMessage("Are you Sure do you want to Change Language?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setLanguage("ta");
                                preferenceData.setSharePrefrenceLocale("ta");
                                LocaleHelper.setLocale(getApplicationContext(), "ta");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeLanguageActivity.this);
                builder.setMessage("Are you Sure do you want to Change Language?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setLanguage("en");
                                preferenceData.setSharePrefrenceLocale("en");
                                LocaleHelper.setLocale(ChangeLanguageActivity.this, "en");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }

    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Select Language");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected void setLanguage(String language) {
        mylocale = new Locale(language);
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.locale = mylocale;
        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        getApplicationContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        ChangeLanguageActivity.this.getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        resources.updateConfiguration(conf, dm);
        Intent refreshIntent = new Intent(ChangeLanguageActivity.this, MainActivity.class);
        finish();
        startActivity(refreshIntent);
        Log.d("Language--->", language);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();
        return super.onOptionsItemSelected(item);
    }
}
