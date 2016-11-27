package com.newbiechen.chatdemo.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by PC on 2016/11/26.
 */

public class FaceEditText extends EditText {
    public FaceEditText(Context context) {
        super(context);
    }

    public FaceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FaceEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTypeface(Typeface tf) {
        AssetManager assetManager = getContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager,"fonts/emoji.ttf");
        super.setTypeface(typeface);
    }
}
