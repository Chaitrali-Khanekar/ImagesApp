package com.example.imagesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.imagesapp.Adapters.FullScreenImageAdapter;
import com.example.imagesapp.Adapters.ImagelistAdapter;
import com.example.imagesapp.Models.ImageModel;

import java.util.ArrayList;
import java.util.List;

public class FullscreenImageActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private FullScreenImageAdapter adapter;
    public static List<ImageModel> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen_image);

        viewPager = findViewById(R.id.pager);
        Bundle bundle = getIntent().getExtras();

        ArrayList<ImageModel> filelist = (ArrayList<ImageModel>) getIntent().getSerializableExtra("data");
        int position_selected = (int) getIntent().getSerializableExtra("position");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = bundle.getString(ImagelistAdapter.EXTRA_IMAGE_TRANSITION_NAME);
            viewPager.setTransitionName(imageTransitionName);
        }

        adapter = new FullScreenImageAdapter(FullscreenImageActivity.this, filelist);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position_selected);

    }


}
