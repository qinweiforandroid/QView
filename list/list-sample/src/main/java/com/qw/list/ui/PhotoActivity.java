package com.qw.list.ui;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.qw.framework.core.ui.BaseActivity;
import com.qw.list.R;

import uk.co.senab.photoview.PhotoView;


/**
 * Created by qinwei on 2016/11/27.
 */

public class PhotoActivity extends BaseActivity {
    private PhotoView mPhotoView;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_photo);
        translucentStatus();
    }

    @Override
    protected void initView() {
        mPhotoView = (PhotoView) findViewById(R.id.mPhotoView);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String imageUrl = getIntent().getStringExtra("key_image_url");
        Glide.with(this).load(imageUrl)
                .placeholder(R.drawable.default_square)
                .into(mPhotoView);
    }
}
