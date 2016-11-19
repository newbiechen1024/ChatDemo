package com.newbiechen.demo;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.newbiechen.demo.widget.RefreshListView;
import android.widget.FrameLayout.LayoutParams;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RefreshListView mListView;
    private LinearLayout mLinearSendFrame;
    private ScrollView mScrollView;
    private ArrayAdapter<String> mAdapter;
    private final Handler mHandler = new Handler();
    private InputMethodManager imm;
    private final List<String> mStringItem = new ArrayList<>();

    private boolean mKeyboardUp = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int screenHeight = findViewById(android.R.id.content).getHeight();
        int sendFrameHeight = mLinearSendFrame.getHeight();
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = screenHeight - sendFrameHeight;
        mListView.setLayoutParams(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_add:
                mStringItem.add("#测试"+"添加的item");
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mStringItem.size());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(){
        mListView = (RefreshListView) findViewById(R.id.main_lv);
        mScrollView = (ScrollView) findViewById(R.id.main_sv);
        mLinearSendFrame = (LinearLayout) findViewById(R.id.main_linear_sendFrame);
        setUpListView();
        setListenerToRootView();
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if (mKeyboardUp){
                            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void setUpListView(){
        for (int i=0; i<20; ++i){
            String content = "#测试"+i;
            mStringItem.add(content);
        }

        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mStringItem);
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        for (int i=0; i<20; ++i){
                            String content = "#刷新"+i;
                            mStringItem.add(0,content);
                        }
                        mAdapter.notifyDataSetChanged();

                        //无效的原因是，notifyDataSetChanged()是异步加载
                        mListView.onRefreshComplete();
                    }
                },2000);
            }
        });
    }

    private void setListenerToRootView() {
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int headerHeight = getSupportActionBar().getHeight() +
                        getStatusBarHeight() + SizeUtils.getNavbarHeight(MainActivity.this);
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                if (heightDiff > headerHeight) {
                    Log.e("keyboard", "keyboard is up");
                    mKeyboardUp = true;
                } else{
                    mKeyboardUp = false;
                }
            }
        });
    }
    private int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Status height:" + height);
        return height;
    }
}
