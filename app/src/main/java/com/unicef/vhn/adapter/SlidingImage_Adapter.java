package com.unicef.vhn.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
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
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;

import java.util.ArrayList;

public class SlidingImage_Adapter extends PagerAdapter {


    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    PreferenceData preferenceData;


    public SlidingImage_Adapter(Context context, ArrayList<String> IMAGES) {
        this.context = context;
        preferenceData = new PreferenceData(context);
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
        Log.e(SlidingImage_Adapter.class.getSimpleName(), IMAGES.size() + "");
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);


//        imageView.setImageResource(Integer.parseInt());
        String imgageUrl = "";
        if (AppConstants.ISPNVISIT) {
            imgageUrl = Apiconstants.PN_VISIT_REPORTS_URL + AppConstants.MOTHER_PICME_ID + "/" + IMAGES.get(position);
        } else {
            imgageUrl = Apiconstants.VISIT_REPORTS_URL + AppConstants.MOTHER_PICME_ID + "/" + IMAGES.get(position);
        }

        Picasso.with(this.context)
                .load(imgageUrl)
                .placeholder(R.drawable.no_image)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.no_image)
                .into(imageView);
//   .centerCrop().
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
