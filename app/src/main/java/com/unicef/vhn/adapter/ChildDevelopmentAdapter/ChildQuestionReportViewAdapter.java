package com.unicef.vhn.adapter.ChildDevelopmentAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import com.unicef.vhn.R;
import com.unicef.vhn.activity.ChildDevelopment.FullViewImageActivity;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.ChildDevelopmentModdel.ChildQuestionReportViewModel;

import java.util.ArrayList;

public class ChildQuestionReportViewAdapter extends RecyclerView.Adapter<ChildQuestionReportViewAdapter.ViewHolder>{
    String TAG = ChildQuestionReportViewAdapter.class.getSimpleName();
    Context context;
    ArrayList<ChildQuestionReportViewModel> childQuestionReportViewModel;
    public ChildQuestionReportViewAdapter(Context context, ArrayList<ChildQuestionReportViewModel> childQuestionReportViewModel) {
        this.context = context;
        this.childQuestionReportViewModel = childQuestionReportViewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_child_dev_view_question, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e(ChildQuestionReportViewAdapter.class.getSimpleName(), "position-->" + position + "array size-->" + childQuestionReportViewModel.size());
        final ChildQuestionReportViewModel childDevQuestionModel = childQuestionReportViewModel.get(position);
        holder.txt_question.setText(childDevQuestionModel.getQuestion());
        holder.txt_status.setText(childDevQuestionModel.getAnswer());

        if (!TextUtils.isEmpty(childDevQuestionModel.getPhoto())) {
            Picasso.with(this.context)
                    .load(!TextUtils.isEmpty(childDevQuestionModel.getPhoto()) ? Apiconstants.CHILD_TRACKING_IMAGES + childDevQuestionModel.getPhoto() : "")
                    .placeholder(R.drawable.no_image)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.no_image)
                    .into(holder.img_camera);
        } else {
            holder.img_camera.setImageResource(R.drawable.no_image);
        }
        holder.img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(childDevQuestionModel.getPhoto())) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("imageuri", childDevQuestionModel.getPhoto());
                    AppConstants.selectedFullViewImageUri=childDevQuestionModel.getPhoto();
                    Intent intent = new Intent(context.getApplicationContext(), FullViewImageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return childQuestionReportViewModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_question, txt_status;
        public ImageView img_camera, img_view;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_question = itemView.findViewById(R.id.txt_question);
            txt_status = itemView.findViewById(R.id.txt_ans1);
            img_camera = itemView.findViewById(R.id.img_view_image1);
            img_view = itemView.findViewById(R.id.img_open_camera);
        }
    }
}
