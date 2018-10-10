package com.unicef.vhn.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MotherDetails.ANMotherDetailsViewActivcity;
import com.unicef.vhn.activity.MotherDetails.PNMotherDetailsViewActivity;
import com.unicef.vhn.activity.MotherLocationActivity;
import com.unicef.vhn.activity.MothersDetailsActivity;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.utiltiy.RoundedTransformation;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sathish on 3/20/2018.
 */

public class MotherListAdapter extends RecyclerView.Adapter<MotherListAdapter.ViewHolder> implements Filterable {
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResultfilter;
    Activity applicationContext;
    MakeCallInterface makeCallInterface;
    String strMid, str_mPhoto, type, userType;
    ContactsAdapterListener listener;

    public MotherListAdapter(List<PNMotherListResponse.VhnAN_Mothers_List> mResult, Activity applicationContext,
                             String type, MakeCallInterface makeCallInterface, ContactsAdapterListener listener) {
        this.applicationContext = applicationContext;
        this.mResult = mResult;
        this.type = type;
        this.makeCallInterface = makeCallInterface;
        this.listener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mothers_an, parent, false);
        return new ViewHolder(view);
    }

    /*new*/
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PNMotherListResponse.VhnAN_Mothers_List pNMotherResponseModel = null;
        if (AppConstants.ISQUERYFILTER) {
            pNMotherResponseModel = mResultfilter.get(position);
        } else {
            pNMotherResponseModel = mResult.get(position);
        }
        holder.txt_username.setText(pNMotherResponseModel.getMName());
        holder.txt_picme_id.setText(pNMotherResponseModel.getMPicmeId());
        holder.txt_list_type.setText(pNMotherResponseModel.getMotherType());
        userType = pNMotherResponseModel.getUserType();
        if (userType.equalsIgnoreCase("0")) {
            holder.iv_android_user.setVisibility(View.GONE);
        } else {
            holder.iv_android_user.setVisibility(View.VISIBLE);

        }
        str_mPhoto = pNMotherResponseModel.getmPhoto();
        Log.d("mphoto-->", Apiconstants.MOTHER_PHOTO_URL + str_mPhoto);

        if (!TextUtils.isEmpty(pNMotherResponseModel.getmPhoto())) {
            Picasso.with(applicationContext)
                    .load(!TextUtils.isEmpty(pNMotherResponseModel.getmPhoto()) ? Apiconstants.MOTHER_PHOTO_URL + pNMotherResponseModel.getmPhoto() : "")
                    .placeholder(R.drawable.girl)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .transform(new RoundedTransformation(90, 4))
                    .error(R.drawable.girl)
                    .into(holder.cardview_image);
        } else {
            holder.cardview_image.setImageResource(R.drawable.girl);
        }

        strMid = pNMotherResponseModel.getMid();
        type = pNMotherResponseModel.getMotherType();


        if (type.equalsIgnoreCase("PN")) {
            holder.txt_list_type.setText(type);
        } else if (type.equalsIgnoreCase("AN")) {
            holder.txt_list_type.setText(type);
        } else if (type.equalsIgnoreCase("Risk")) {
            holder.txt_list_type.setText(type);
        }


        final PNMotherListResponse.VhnAN_Mothers_List finalPNMotherResponseModel = pNMotherResponseModel;
        if (finalPNMotherResponseModel.getmMotherMobile().equalsIgnoreCase("null") || finalPNMotherResponseModel.getmMotherMobile().length() < 10) {
            holder.ll_call.setVisibility(View.GONE);
        } else {
            holder.ll_call.setVisibility(View.VISIBLE);
        }
        holder.ll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCallInterface.makeCall(finalPNMotherResponseModel.getmMotherMobile());
            }
        });

        holder.ll_track_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckNetwork checkNetwork =new CheckNetwork(applicationContext);
                if (checkNetwork.isNetworkAvailable()) {
                    AppConstants.SELECTED_MID = finalPNMotherResponseModel.getMid();
                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), MotherLocationActivity.class));
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
                    builder.setTitle("You can't see this mother location!");
                    builder.setCancelable(false);
                    builder.setMessage("Please check internet connection");
                    // Add the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                    dialog.dismiss();
//                            finish();
                        }
                    });


// Create the AlertDialog
                    AlertDialog dialog = builder.create();

                    dialog.show();
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppConstants.SELECTED_MID=pNMotherResponseModel.getMid();
                type = finalPNMotherResponseModel.getMotherType();
                if (type.equalsIgnoreCase("PN")) {
                    AppConstants.SELECTED_MID = finalPNMotherResponseModel.getMid();
                    AppConstants.MOTHER_PICME_ID = finalPNMotherResponseModel.getMPicmeId();

//                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), PNMotherVisitDetailsActivity.class));
                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), PNMotherDetailsViewActivity.class));

                } else if (type.equalsIgnoreCase("AN")) {
                    AppConstants.SELECTED_MID = finalPNMotherResponseModel.getMid();
                    AppConstants.MOTHER_PICME_ID = finalPNMotherResponseModel.getMPicmeId();
//                    Toast.makeText(applicationContext.getApplicationContext(),"AN MID"+AppConstants.SELECTED_MID ,Toast.LENGTH_SHORT).show();

//                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), MothersDetailsActivity.class));
                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), ANMotherDetailsViewActivcity.class));

                } else if (type.equalsIgnoreCase("Risk")) {
                    AppConstants.SELECTED_MID = finalPNMotherResponseModel.getMid();
                    AppConstants.MOTHER_PICME_ID = finalPNMotherResponseModel.getMPicmeId();

                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), MothersDetailsActivity.class));

                }
//                else{
//                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(),MothersDetailsActivity.class));
//
//                }

            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_username, txt_picme_id, txt_list_type;
        LinearLayout ll_ll_mother_type, ll_track_location, ll_call, iv_android_user;
        ImageView cardview_image;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
            txt_list_type = itemView.findViewById(R.id.txt_list_type);
            ll_ll_mother_type = itemView.findViewById(R.id.ll_ll_mother_type);
            ll_track_location = itemView.findViewById(R.id.ll_track_location);
            ll_call = itemView.findViewById(R.id.ll_call);
            cardview_image = itemView.findViewById(R.id.cardview_image);
            iv_android_user = itemView.findViewById(R.id.iv_android_user);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onContactSelected(mResultfilter.get(getAdapterPosition()));

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (AppConstants.ISQUERYFILTER) {
            AppConstants.ISQUERYFILTER = true;
            return mResultfilter.size();

        } else {
            return mResult.size();

        }

    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mResultfilter = mResult;
                } else {
                    List<PNMotherListResponse.VhnAN_Mothers_List> filteredList = new ArrayList<>();
                    for (PNMotherListResponse.VhnAN_Mothers_List row : mResult) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.getMName().toLowerCase().contains(charString.toLowerCase()) || row.getMPicmeId().contains(charSequence)) {
                            Log.e("filter mother name", row.getMName());
                            Log.e("filter mother picme id", row.getMPicmeId());
                            filteredList.add(row);
                        }
                    }

                    mResultfilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mResultfilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mResultfilter = (ArrayList<PNMotherListResponse.VhnAN_Mothers_List>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(PNMotherListResponse.VhnAN_Mothers_List contact);
    }
}
