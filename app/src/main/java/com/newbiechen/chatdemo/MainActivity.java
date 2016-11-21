package com.newbiechen.chatdemo;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.newbiechen.chatdemo.adapter.ChatAdapter;
import com.newbiechen.chatdemo.entity.ChatMessage;
import com.newbiechen.chatdemo.utils.DimenUtils;
import com.newbiechen.chatdemo.utils.PatternUtils;
import com.newbiechen.chatdemo.widget.ChatFrameView;
import com.newbiechen.chatdemo.widget.KeyboardStateHelper;
import com.newbiechen.chatdemo.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;

public class MainActivity extends AppCompatActivity {
    private ScrollView mScrollView;
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
        mScrollView = (ScrollView) findViewById(R.id.main_sv_content);
        mChatFrame = (ChatFrameView) findViewById(R.id.main_cf_toolbox);
        mLinearContent = (LinearLayout) findViewById(R.id.main_linear_content);
        setUpListView();
    }

    public void setUpListView(){
        mChatMessageItems.add(new ChatMessage("","以后的版本支持链接高亮喔:http://www.kymjs.com支持http、https、svn、ftp开头的链接",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","<a href=\"http://kymjs.com\">自定义链接</a>也是支持的",ChatMessage.TYPE_SEND,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","以后的版本支持链接高亮喔:http://www.kymjs.com支持http、https、svn、ftp开头的链接",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","<a href=\"http://kymjs.com\">自定义链接</a>也是支持的",ChatMessage.TYPE_SEND,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","以后的版本支持链接高亮喔:http://www.kymjs.com支持http、https、svn、ftp开头的链接",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","<a href=\"http://kymjs.com\">自定义链接</a>也是支持的",ChatMessage.TYPE_SEND,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","以后的版本支持链接高亮喔:http://www.kymjs.com支持http、https、svn、ftp开头的链接",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","<a href=\"http://kymjs.com\">自定义链接</a>也是支持的",ChatMessage.TYPE_SEND,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","以后的版本支持链接高亮喔:http://www.kymjs.com支持http、https、svn、ftp开头的链接",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","<a href=\"http://kymjs.com\">自定义链接</a>也是支持的",ChatMessage.TYPE_SEND,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","以后的版本支持链接高亮喔:http://www.kymjs.com支持http、https、svn、ftp开头的链接",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMessage("","<a href=\"http://kymjs.com\">自定义链接</a>也是支持的",ChatMessage.TYPE_SEND,ChatMessage.STATUS_SUCCEED));

        mAdapter = new ChatAdapter(this);
        mAdapter.addItems(mChatMessageItems);
        mRlv.setAdapter(mAdapter);

        //刷新
        mRlv.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.addPreItem(new ChatMessage("","以后的版本支持链接高亮喔:http://www.kymjs.com支持http、https、svn、ftp开头的链接",ChatMessage.TYPE_REPLY,ChatMessage.STATUS_SUCCEED));
                mAdapter.addPreItem(new ChatMessage("","<a href=\"http://kymjs.com\">自定义链接</a>也是支持的",ChatMessage.TYPE_SEND,ChatMessage.STATUS_SUCCEED));
                mRlv.setSelection(2);
                mRlv.onRefreshComplete();
                getWindow();
            }
        });
    }

    private void initWidget(){
    }

    private void initListener(){

        /**
         * 点击ScrollView的时候，隐藏发送框
         */
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mChatFrame.hideKeyBoard();
                return false;
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
            int sendFrameHeight = mChatFrame.getHeight();
            //动态设置高度
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, contentHeight
            );
            mLinearContent.setLayoutParams(layoutParams);
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
