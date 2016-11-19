package com.newbiechen.chatdemo.widget;

import android.content.Context;
import android.icu.util.Measure;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by PC on 2016/11/18.
 * 判断是否为
 */

public class KeyboardLayout extends ViewGroup {

    private int mScreenWidth;
    private int mScreenHeight;

    public KeyboardLayout(Context context) {
        super(context);
    }

    public KeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取可用屏幕的大小
        if (mScreenWidth == 0 && mScreenHeight == 0){
            mScreenWidth = widthSize;
            mScreenHeight = heightSize;
        }
        //首先测量发送框
        View sendFrame = getChildAt(1);
        measureChild(sendFrame,widthMeasureSpec,heightMeasureSpec);
        //然后获取到ListView的大小
        int viewHeight = mScreenHeight - sendFrame.getMeasuredHeight();
        View view = getChildAt(0);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,viewHeight));
        measureChild(view,widthMeasureSpec,heightMeasureSpec);

        setMeasuredDimension(widthSize,heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
