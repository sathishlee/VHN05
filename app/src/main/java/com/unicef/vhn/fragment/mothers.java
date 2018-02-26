package com.unicef.vhn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.unicef.vhn.AppConstent;
import com.unicef.vhn.MothersDetailsActivity;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.MoviesAdapter;
import com.unicef.vhn.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by priyan on 2/3/2018.
 */

public class mothers extends Fragment implements View.OnClickListener {
    private List<Movie> movieList;
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    Movie movie;
    //    EditText edt_search;
//    CardView card_mother;


    Spinner sp_mother_type,sp_village;

    ArrayList<String> mothers_type_list;

    LinearLayout ll_trimester, ll_dose, ll_edd;
    TextView txt_trim1, txt_trim2, txt_trim3;
    TextView txt_dose1, txt_dose2;
    TextView txt_edd1, txt_edd2, txt_edd3;
    SearchView edt_search;

    public static mothers newInstance() {
        mothers fragment = new mothers();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mothers, container, false);
        movieList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
//        edt_search = view.findViewById(R.id.edt_search);
        sp_mother_type = (Spinner) view.findViewById(R.id.sp_mother_type);
        sp_village = (Spinner) view.findViewById(R.id.sp_village);
        sp_village.setVisibility(GONE);
        ll_trimester = (LinearLayout) view.findViewById(R.id.ll_trimester);
        ll_trimester.setVisibility(GONE);
        ll_dose = (LinearLayout) view.findViewById(R.id.ll_dose);
        ll_dose.setVisibility(GONE);
        ll_edd = (LinearLayout) view.findViewById(R.id.ll_edd);
        ll_edd.setVisibility(GONE);
        txt_trim1 = view.findViewById(R.id.trim1);
        txt_trim2 = view.findViewById(R.id.trim2);
        txt_trim3 = view.findViewById(R.id.trim3);

        txt_dose1 = view.findViewById(R.id.txt_dose1);
        txt_dose2 = view.findViewById(R.id.txt_dose2);

        txt_edd1 = view.findViewById(R.id.txt_edd1);
        txt_edd2 = view.findViewById(R.id.txt_edd2);
        txt_edd3 = view.findViewById(R.id.txt_edd3);
        edt_search = view.findViewById(R.id.edt_search);
        edt_search.setVisibility(GONE);

//        card_mother = view.findViewById(R.id.card_mother);
        mAdapter = new MoviesAdapter(getActivity(), movieList,"ANMother");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        if (AppConstent.isfromhome==0){
            sp_mother_type.setVisibility(GONE);
            edt_search.setVisibility(View.VISIBLE);
            sp_village.setVisibility(GONE);
            ll_trimester.setVisibility(GONE);
            ll_dose.setVisibility(GONE);
            ll_edd.setVisibility(GONE);
        }
        txt_trim1.setOnClickListener(this);
        txt_trim2.setOnClickListener(this);
        txt_trim3.setOnClickListener(this);

        txt_dose1.setOnClickListener(this);
        txt_dose2.setOnClickListener(this);
        txt_edd1.setOnClickListener(this);
        txt_edd2.setOnClickListener(this);
        txt_edd3.setOnClickListener(this);


        sp_mother_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                Toast.makeText(getActivity(),getResources().getTextArray(R.array.array_mothers_list)[position],Toast.LENGTH_SHORT).show();

                if (position == 1) {
                    edt_search.setVisibility(View.VISIBLE);
                    sp_village.setVisibility(GONE);
                    ll_trimester.setVisibility(GONE);
                    ll_dose.setVisibility(GONE);
                    ll_edd.setVisibility(GONE);
                } else if (position == 2) {
//                Toast.makeText(getActivity(), getResources().getTextArray(R.array.array_mothers_list)[1], Toast.LENGTH_SHORT).show();
                    edt_search.setVisibility(View.GONE);
                    sp_village.setVisibility(View.VISIBLE);
                    ll_trimester.setVisibility(GONE);
                    ll_dose.setVisibility(GONE);
                    ll_edd.setVisibility(GONE);
                } else if (position == 3) {
//                Toast.makeText(getActivity(), getResources().getTextArray(R.array.array_mothers_list)[1], Toast.LENGTH_SHORT).show();
                    edt_search.setVisibility(View.GONE);
                    sp_village.setVisibility(GONE);
                    ll_trimester.setVisibility(View.VISIBLE);
                    ll_dose.setVisibility(GONE);
                    ll_edd.setVisibility(GONE);
                } else if (position == 4) {
//                Toast.makeText(getActivity(), getResources().getTextArray(R.array.array_mothers_list)[1], Toast.LENGTH_SHORT).show();
                    edt_search.setVisibility(View.GONE);
                    sp_village.setVisibility(GONE);
                    ll_trimester.setVisibility(View.GONE);
                    ll_dose.setVisibility(View.VISIBLE);
                    ll_edd.setVisibility(GONE);
                } else if (position == 5) {
//                Toast.makeText(getActivity(), getResources().getTextArray(R.array.array_mothers_list)[1], Toast.LENGTH_SHORT).show();
                    edt_search.setVisibility(View.GONE);
                    sp_village.setVisibility(GONE);
                    ll_trimester.setVisibility(View.GONE);
                    ll_dose.setVisibility(View.GONE);
                    ll_edd.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
     /*   edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                card_mother.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });*/
        prepareMovieData();
//        card_mother.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), MothersDetailsActivity.class));
//
//            }
//        });
        return view;

    }

    private void prepareMovieData() {
        movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);
        movie = new Movie("Tamil selvi", "1001", "High");
        movieList.add(movie);
        movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);
        movie = new
                Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);
        movie = new Movie("Tamil selvi", "1001", "High");
        movieList.add(movie);

        movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);

        movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);

        movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);

        movie = new Movie("Tamil selvi", "1001", "High");
        movieList.add(movie);

        movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trim1:

                break;
            case R.id.trim2:

                break;
            case R.id.trim3:
                break;
            case R.id.txt_dose1:
                break;
            case R.id.txt_dose2:
                break;

            case R.id.txt_edd1:
                break;
            case R.id.txt_edd2:
                break;
            case R.id.txt_edd3:
                break;

        }
    }

}
