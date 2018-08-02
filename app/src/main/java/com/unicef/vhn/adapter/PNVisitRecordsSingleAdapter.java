package com.unicef.vhn.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.ImageFullViewActivity;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.responseModel.PNVisitRecordsSingleResponseModel;

import java.util.ArrayList;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class PNVisitRecordsSingleAdapter extends RecyclerView.Adapter<PNVisitRecordsSingleAdapter.SingleImageHolder> {

    private ArrayList<PNVisitRecordsSingleResponseModel> visitRecordsSingleResponseModels;
    private Context context;
    String visitImage;
    Activity applicationContext;
    PreferenceData preferenceData;
    boolean isImageFitToScreen;

    public PNVisitRecordsSingleAdapter(ArrayList<PNVisitRecordsSingleResponseModel> visitRecordsSingleResponseModels, Context context){
        this.context = context;
        this.visitRecordsSingleResponseModels = visitRecordsSingleResponseModels;
//        AppConstants.mylist.addAll(visitRecordsSingleResponseModels);

       /* for (int i=0; i<visitRecordsSingleResponseModels.size();i++){
            AppConstants.mylist.add(visitRecordsSingleResponseModels.get(i));

        }*/
    }

    @Override
    public SingleImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reports_list_single, parent,false);
//        SingleImageHolder singleImageHolder = new SingleImageHolder(v);
        return new SingleImageHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleImageHolder imageHolder, int position){
        preferenceData = new PreferenceData(context);
        final PNVisitRecordsSingleResponseModel visitRecordsSingleResponseModel = visitRecordsSingleResponseModels.get(position);

        visitImage ="/"+ visitRecordsSingleResponseModel.getImage();
//        Log.w("PN Visit Reports", Apiconstants.PN_VISIT_REPORTS_URL+preferenceData.getPicmeId()+visitImage);
        Log.w("PN Visit Reports", Apiconstants.PN_VISIT_REPORTS_URL+ AppConstants.MOTHER_PICME_ID+visitImage);

        if(!TextUtils.isEmpty(visitRecordsSingleResponseModel.getImage())) {
            Picasso.with(this.context)
//                    .load(!TextUtils.isEmpty(visitRecordsSingleResponseModel.getImage())? Apiconstants.PN_VISIT_REPORTS_URL+preferenceData.getPicmeId()+"/"+visitRecordsSingleResponseModel.getImage():"")
                    .load(Apiconstants.PN_VISIT_REPORTS_URL+ AppConstants.MOTHER_PICME_ID+visitImage)
//
                    .placeholder(R.drawable.no_image)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.no_image)
                    .into(imageHolder.itemImage);
        }
        else{
            imageHolder.itemImage.setImageResource(R.drawable.no_image);
        }
        imageHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(isImageFitToScreen){
                    isImageFitToScreen=false;
                    imageHolder.itemImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    imageHolder.itemImage.setAdjustViewBounds(true);
                }else{
                    isImageFitToScreen=true;
                    imageHolder.itemImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    imageHolder.itemImage.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                Intent intent= new Intent(context,FullImageViewActivity.class);
                intent.putExtra("image",visitRecordsSingleResponseModel.getImage());
                context.startActivity(intent);*/
                /*Log.d("Image Url--->",Apiconstants.VISIT_REPORTS_URL+preferenceData.getPicmeId()+"/"+visitRecordsSingleResponseModel.getImage());
                Intent intent= new Intent(context,FullImageViewActivity.class);
                intent.putExtra("image",Apiconstants.VISIT_REPORTS_URL+preferenceData.getPicmeId()+"/"+visitRecordsSingleResponseModel.getImage());
                context.startActivity(intent);*/
                Intent intent= new Intent(context,ImageFullViewActivity.class);
                AppConstants.ISPNVISIT=true;
//                intent.putExtra("mylist", visitRecordsSingleResponseModels);
                String[] imgList=new String[visitRecordsSingleResponseModels.size()];
                for (int i=0;i<visitRecordsSingleResponseModels.size();i++){
                    imgList[i]=visitRecordsSingleResponseModels.get(i).getImage();
                }
                intent.putExtra("mylist",imgList);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
//        return (null != visitRecordsSingleResponseModels ? visitRecordsSingleResponseModels.size() : 0);
        return visitRecordsSingleResponseModels.size();
    }

    public class SingleImageHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;

        public SingleImageHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}
