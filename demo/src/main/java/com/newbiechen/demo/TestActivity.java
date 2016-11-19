package com.newbiechen.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by PC on 2016/11/17.
 */

public class TestActivity extends AppCompatActivity {
    private RelativeLayout mRlContent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mRlContent = (RelativeLayout) findViewById(R.id.root_view);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ViewGroup.LayoutParams layoutParams = mRlContent.getLayoutParams();
        layoutParams.width = 500;
        layoutParams.height = 500;
        mRlContent.setLayoutParams(layoutParams);
    }

}
