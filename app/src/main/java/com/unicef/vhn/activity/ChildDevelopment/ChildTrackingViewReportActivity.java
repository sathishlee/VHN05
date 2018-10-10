package com.unicef.vhn.activity.ChildDevelopment;

import android.app.ProgressDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.CHILDDevlopmentPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.ChildDevelopmentAdapter.ChildDevelopmentReportAdapter;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.ChildDevelopmentModdel.*;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.CHILDDevlopmentViews;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChildTrackingViewReportActivity extends AppCompatActivity implements CHILDDevlopmentViews {
    ProgressDialog progressDialog;
    PreferenceData preferenceData;
    CheckNetwork checkNetwork;
    CHILDDevlopmentPresenter childDevlopmentPresenter;


    public static ArrayList<ChildDevQuestionModel> arrchildDevList, arrchildDevList1;
    ArrayList<ArrayList<ChildDevQuestionModel>> nested_arrchildDevList;
    ArrayList<OuterListModel> nested_childQuestionReportViewModel;
    ArrayList<ChildQuestionReportViewModel> arr_childQuestionReportViewModel;
    ChildQuestionReportViewModel childQuestionReportViewModel;

    OuterListforAllChildDevModel outerListModel1;
    ArrayList<OuterListforAllChildDevModel> nested_childQuestionReportViewModel1;
    ArrayList<ShowAllChildDevModel> arr_childQuestionReportViewModel1;
    ShowAllChildDevModel childQuestionReportViewModel1;

    OuterListModel outerListModel;
    ChildDevelopmentReportAdapter childDevQuestionAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_tracking_view_report);
        showActionBar();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        checkNetwork = new CheckNetwork(this);

        arrchildDevList = new ArrayList<>();
        nested_arrchildDevList = new ArrayList<>();

        nested_childQuestionReportViewModel = new ArrayList<>();
        arr_childQuestionReportViewModel = new ArrayList<>();

        nested_childQuestionReportViewModel1 = new ArrayList<>();
        arr_childQuestionReportViewModel1 = new ArrayList<>();

        childDevlopmentPresenter = new CHILDDevlopmentPresenter(ChildTrackingViewReportActivity.this, this);
        if (checkNetwork.isNetworkAvailable()) {
//            childDevlopmentPresenter.getAllChildDevelopmentRecords(preferenceData.getPicmeId(), preferenceData.getMId());
//            childDevlopmentPresenter.getAllChildDevelopmentRecords("133006750029","2");
            childDevlopmentPresenter.getAllChildDevelopmentRecords(AppConstants.MOTHER_PICME_ID , AppConstants.SELECTED_MID);
        } else {
            Toast.makeText(getApplicationContext(), "you are in offline", Toast.LENGTH_SHORT).show();
        }

        viewPager = findViewById(R.id.hre_viewpager);
//        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.hre_tabs);
        tabLayout.setupWithViewPager(viewPager);

        arrchildDevList = new ArrayList<>();
        arrchildDevList1 = new ArrayList<>();

        nested_arrchildDevList = new ArrayList<>();

    }
    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Child Tracking View Report");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
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
    public void getCurrentmonthSuccess(String response) {

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray njsonArray = jsonArray.getJSONArray(i);
                    outerListModel1 = new OuterListforAllChildDevModel();
                    for (int x = 0; x < njsonArray.length(); x++) {
                        JSONObject njsonObject = njsonArray.getJSONObject(x);
                        childQuestionReportViewModel1 = new ShowAllChildDevModel();
                        childQuestionReportViewModel1.setQuestionid(njsonObject.getString("questionid"));
                        childQuestionReportViewModel1.setAnswerid(njsonObject.getString("answerid"));
                        childQuestionReportViewModel1.setImageid(njsonObject.getString("imageid"));
                        childQuestionReportViewModel1.setMonth(njsonObject.getString("month"));
                        arr_childQuestionReportViewModel1.add(childQuestionReportViewModel1);

                    }
                    outerListModel1.setShowAllChildDevModel(arr_childQuestionReportViewModel1);
                    nested_childQuestionReportViewModel1.add(i, outerListModel1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        childDevlopmentPresenter.getAllQuestions();

    }

    @Override
    public void getCurrentmonthFailiure(String response) {

    }

    @Override
    public void getQuestionsSuccess(String response) {

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray njsonArray = jsonArray.getJSONArray(i);
                    outerListModel = new OuterListModel();

                    for (int x = 0; x < njsonArray.length(); x++) {
                        JSONObject njsonObject = njsonArray.getJSONObject(x);

                        childQuestionReportViewModel = new ChildQuestionReportViewModel();

                        childQuestionReportViewModel.setQuestion(njsonObject.getString("qustname"));
                        childQuestionReportViewModel.setMonth(njsonObject.getString("qustid"));
                        childQuestionReportViewModel.setAnswer(getAnser(njsonObject.getString("qustid"),njsonObject.getString("qustno")));
                        childQuestionReportViewModel.setPhoto(getImage(njsonObject.getString("qustid"),njsonObject.getString("qustno")));

                        arr_childQuestionReportViewModel.add(childQuestionReportViewModel);
                    }

                    outerListModel.setChildQuestionReportViewModels(arr_childQuestionReportViewModel);
                    nested_childQuestionReportViewModel.add(i, outerListModel);
                }

                childDevQuestionAdapter = new ChildDevelopmentReportAdapter(getApplicationContext(), nested_childQuestionReportViewModel);
                viewPager.setOffscreenPageLimit(nested_childQuestionReportViewModel.size());
                viewPager.setAdapter(childDevQuestionAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private String getImage(String month,String queno) {
        String strImage="";
        for (int m=0;m<nested_childQuestionReportViewModel1.size();m++){
            outerListModel1 =nested_childQuestionReportViewModel1.get(m);
            arr_childQuestionReportViewModel1 =outerListModel1.getShowAllChildDevModel();
            for (int n=0;n<arr_childQuestionReportViewModel1.size();n++){
                childQuestionReportViewModel1 = arr_childQuestionReportViewModel1.get(n);
                if (month.equalsIgnoreCase(childQuestionReportViewModel1.getMonth())){
                    if (queno.equalsIgnoreCase(childQuestionReportViewModel1.getQuestionid())){
                        if (childQuestionReportViewModel1.getAnswerid().equalsIgnoreCase("Yes")){
                            strImage =childQuestionReportViewModel1.getImageid();
                        }
                        else{
                            strImage ="";
                        }
                    }
                }
            }
        }
        return strImage;

    }

    private String getAnser(String month,String queno) {
        String strAns = "No";

        for (int m = 0; m < nested_childQuestionReportViewModel1.size(); m++) {
            outerListModel1 = nested_childQuestionReportViewModel1.get(m);
            arr_childQuestionReportViewModel1 = outerListModel1.getShowAllChildDevModel();
            for (int n = 0; n < arr_childQuestionReportViewModel1.size(); n++) {
                childQuestionReportViewModel1 = arr_childQuestionReportViewModel1.get(n);
                if (month.equalsIgnoreCase(childQuestionReportViewModel1.getMonth())) {
                    if (queno.equalsIgnoreCase(childQuestionReportViewModel1.getQuestionid())) {
                        strAns = childQuestionReportViewModel1.getAnswerid();
                    }
                }

            }
        }
        return strAns;

    }
        @Override
    public void getQuestionsFailiure(String response) {

    }

    @Override
    public void UpdateQuestinsSuccess(String response) {

    }

    @Override
    public void UpdateQuestinsFailiure(String response) {

    }
}
