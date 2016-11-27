package com.newbiechen.chatdemo;

import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.newbiechen.chatdemo.adapter.ChatAdapter;
import com.newbiechen.chatdemo.entity.ChatMessage;
import com.newbiechen.chatdemo.utils.DimenUtils;
import com.newbiechen.chatdemo.widget.ChatFrameView;
import com.newbiechen.chatdemo.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RefreshListView mRlv;
    private ChatFrameView mChatFrame;
    private LinearLayout mLinearContent;

    private ChatAdapter mAdapter;

    private final Handler mHandler = new Handler();
    private final List<ChatMessage> mChatMessageItems = new ArrayList<>();
    private final Random mRandom = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initWidget();
        initListener();
    }

    private void initView(){
        mRlv = (RefreshListView) findViewById(R.id.main_rlv_item);
        mChatFrame = (ChatFrameView) findViewById(R.id.main_cf_toolbox);
        mLinearContent = (LinearLayout) findViewById(R.id.main_linear_content);
        setUpListView();
    }

    public void setUpListView(){
        mChatMessageItems.add(new ChatMessage("","http://www.google.com支持http、https、svn、ftp开头的链接",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","也可以这样www.csdn.com",ChatMessage.TYPE_SEND,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","手机号也没问题12345678910",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","邮箱当然也可以13456789@csdn.com",ChatMessage.TYPE_SEND,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","还有发送成功和失败的效果~~",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_FAIL));
        mChatMessageItems.add(new ChatMessage("","正在发送中哦",ChatMessage.TYPE_SEND,ChatMessage.STATUS_SENDING));
        mChatMessageItems.add(new ChatMessage("","哈哈~~~",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","可以，可以",ChatMessage.TYPE_SEND,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","github地址:https://github.com/newbiechen1024/ChatDemo",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_SUCCEED));

        mAdapter = new ChatAdapter(this);
        mAdapter.addItems(mChatMessageItems);
        mRlv.setAdapter(mAdapter);

        //刷新
        mRlv.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addPreItem(new ChatMessage("","历史记录测试数据",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_SUCCEED));
                        mAdapter.addPreItem(new ChatMessage("","这是历史记录",ChatMessage.TYPE_SEND,ChatMessage.STATUS_SUCCEED));
                        mRlv.setSelection(2);
                        mRlv.onRefreshComplete();
                        getWindow();
                    }
                },1000);
            }
        });
    }

    private void initWidget(){
    }

    private void initListener(){
        /**
         * 点击ScrollView的时候，隐藏发送框
         */
        mRlv.setOnContextTouchListener(new RefreshListView.OnContextTouchListener() {
            @Override
            public void onContextTouch() {
                mChatFrame.hideLayout();
            }
        });
        mRlv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mChatFrame.setOnChatFrameListener(new ChatFrameView.OnChatFrameListener() {
            @Override
            public void sendMessage(String inputContent) {
                ChatMessage message = new ChatMessage("",inputContent,ChatMessage.TYPE_SEND,ChatMessage.STATUS_SENDING);
                mAdapter.addNextItem(message);
                mRlv.setSelection(mAdapter.getCount());
            }
        });
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //动态修改LinearLayout的大小
        if (hasFocus){
            //当前可展示空间的高度
            int contentHeight = DimenUtils.getContentLayoutHeight(this);
            //动态设置高度
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, contentHeight
            );
            mLinearContent.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onBackPressed() {
        //返回的时候，首先键盘是否关闭
        if (mChatFrame.getFrameState() != mChatFrame.STATE_HIDE){
            mChatFrame.hideLayout();
        }
        else {
            super.onBackPressed();
        }
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_add:
                mAdapter.addNextItem(new ChatMessage("","测试功能是否没问题",mRandom.nextInt(2),ChatMessage.STATUS_SUCCEED));
                mRlv.setSelection(mAdapter.getCount());
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onDestroy() {
        mChatFrame.removeChatFrame();
        super.onDestroy();
    }

}
