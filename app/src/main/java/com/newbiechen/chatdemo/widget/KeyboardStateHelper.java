package com.newbiechen.chatdemo.widget;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.newbiechen.chatdemo.utils.DimenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2016/11/20.
 */

public class KeyboardStateHelper implements ViewTreeObserver.OnGlobalLayoutListener{

    private AppCompatActivity mActivity;
    private OnKeyboardStateChangeListener mStateChangeListener;
    private boolean isKeyboardUp = false;

    public KeyboardStateHelper(AppCompatActivity activity) {
        mActivity = activity;
        mActivity.getWindow().getDecorView().getViewTreeObserver()
                .addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        //判断keyboard是否显示或则隐藏
        int otherHeight = DimenUtils.getStatusBarHeight(mActivity) +
                DimenUtils.getActionbarHeight(mActivity) + DimenUtils.getNavBarHeight(mActivity);
        int distance = DimenUtils.getScreenHeight(mActivity) -
                DimenUtils.getContentLayoutHeight(mActivity);
        //如果之间的距离大于其他零件的距离，说明根布局缩小了
        if (distance > otherHeight){
            isKeyboardUp = true;
            if (mStateChangeListener != null){
                mStateChangeListener.onKeyboardOpen(distance);
            }
        }
        else {
            isKeyboardUp = false;
            mStateChangeListener.onKeyboardClosed();
        }
    }

    public interface OnKeyboardStateChangeListener {
        void onKeyboardOpen(int keyboardHeight);
        void onKeyboardClosed();
    }

    /*********************************公共方法*************************************/
    public void setOnKeyboardStateChangeListener(OnKeyboardStateChangeListener listener){
        mStateChangeListener = listener;
    }

    public void setKeyboardState(boolean state){
        isKeyboardUp = state;

    }

    public boolean getKeyboardState(){
        return isKeyboardUp;
    }

    public void removeKeyboardStateHelper(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            mActivity.getWindow().getDecorView().getViewTreeObserver()
                    .removeOnGlobalLayoutListener(this);
        }
        else {
            mActivity.getWindow().getDecorView().getViewTreeObserver()
                    .removeGlobalOnLayoutListener(this);
        }

    }
}
