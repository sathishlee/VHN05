package com.unicef.vhn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class PNMotherVisitListActivity extends AppCompatActivity {
    LinearLayout postnatal_mother;
    CardView pn_visit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnmother_visit_list);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("PN Mother Visits");

        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);

        CardView pn_visit = (CardView) findViewById(R.id.pn_visit);

        pn_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PNMotherVisitListActivity.this, PNMotherVisitListDetailsActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(PNMotherVisitListActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
