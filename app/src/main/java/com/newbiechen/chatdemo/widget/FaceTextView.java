package com.newbiechen.chatdemo.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by PC on 2016/11/26.
 */

public class FaceTextView extends TextView {
    private Context mContext;
    public FaceTextView(Context context) {
        this(context,null);
    }

    public FaceTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public void setTypeface(Typeface tf) {
        AssetManager assetManager = mContext.getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager,"fonts/emoji.ttf");
        super.setTypeface(typeface);
    }
}
