package com.example.imagesapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.imagesapp.CheckInternetNetwork;
import com.example.imagesapp.FullscreenImageActivity;
import com.example.imagesapp.Models.ImageModel;
import com.example.imagesapp.OnBottomReachedListener;
import com.example.imagesapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chaitrali Khanekar on 29/11/2019.
 */
public class ImagelistAdapter extends RecyclerView.Adapter<ImagelistAdapter.MyViewHolder> {
    public static final String EXTRA_IMAGE_TRANSITION_NAME = "image_transition_name";
    private List<ImageModel> imageList = new ArrayList<>();
    private int pos;
    Context mContext;
    int lastPosition = -1;
    ArrayList<String> abclist = new ArrayList<>();

    OnBottomReachedListener onBottomReachedListener;

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {

        this.onBottomReachedListener = onBottomReachedListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageIV;

        public MyViewHolder(View view) {
            super(view);
            imageIV = view.findViewById(R.id.item_image_iv);

        }
    }

    public ImagelistAdapter(Context mContext, List<ImageModel> imageList) {
        this.imageList = imageList;
        this.mContext = mContext;
    }

    public void ImagelistAdapter1(List<ImageModel> imageList) {
        this.imageList = new ArrayList<>();
        this.imageList.clear();
        this.imageList.addAll(imageList);

//        notifyDataSetChanged();


    }

    @Override
    public ImagelistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);

        return new ImagelistAdapter.MyViewHolder(itemView);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final ImagelistAdapter.MyViewHolder holder, final int position) {
        final ImageModel li = imageList.get(position);
        pos = position;

        if (position == imageList.size() - 1) {

            onBottomReachedListener.onBottomReached(position);

        }


        if (CheckInternetNetwork.isInternetAvailable(mContext)) {

            Glide.with(mContext)
                    .load(li.getImagepath())
                    .apply(new RequestOptions().placeholder(R.drawable.defimg).error(R.drawable.defimg))
                    .into(holder.imageIV);

        } else {

            byte[] decodedString = Base64.decode(li.getImagepath(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            Glide.with(mContext)
                    .asBitmap()
                    .load(decodedByte)
                    .apply(new RequestOptions().placeholder(R.drawable.defimg).error(R.drawable.defimg))
                    .into(holder.imageIV);

        }


        holder.imageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = position;
                Intent i = new Intent(mContext, FullscreenImageActivity.class);
                i.putExtra("data", (ArrayList<ImageModel>) imageList);
                i.putExtra("position", pos);
                i.putExtra(EXTRA_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(v));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, (View) holder.imageIV, ViewCompat.getTransitionName(holder.imageIV));
                mContext.startActivity(i, options.toBundle());

            }
        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public void updateList(List<ImageModel> list) {
        imageList = list;
        notifyDataSetChanged();
    }

    public static byte[] toBytes(String data, int length) {
        byte[] result = new byte[length];
        System.arraycopy(data.getBytes(), 0, result, length - data.length(), data.length());
        return result;
    }

}