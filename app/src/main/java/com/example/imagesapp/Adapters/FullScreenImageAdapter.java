package com.example.imagesapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.imagesapp.CheckInternetNetwork;
import com.example.imagesapp.Models.ImageModel;
import com.example.imagesapp.R;

import java.util.List;

/**
 * Created by Chaitrali Khanekar on 29/11/2019.
 */
public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private List<ImageModel> _imagePaths;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  List<ImageModel> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);

        if (CheckInternetNetwork.isInternetAvailable(this._activity)) {
            Glide.with(this._activity)
                    .load(_imagePaths.get(position).getImagepath())
                    .apply(new RequestOptions().placeholder(R.drawable.defimg).error(R.drawable.defimg))
                    .into(imgDisplay);

        } else {
            byte[] decodedString = Base64.decode(_imagePaths.get(position).getImagepath(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            Glide.with(this._activity)
                    .asBitmap()
                    .load(decodedByte)
                    .apply(new RequestOptions().placeholder(R.drawable.defimg).error(R.drawable.defimg))
                    .into(imgDisplay);

        }

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
