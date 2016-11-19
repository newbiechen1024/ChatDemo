package com.newbiechen.chatdemo;

import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.newbiechen.chatdemo.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mLinearSendFrame;
    private ScrollView mScrollView;
    private RefreshListView mRlv;
    private EditText mEtContent;
    private Button mBtnSend;

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private InputMethodManager mInputManager;
    private ChatAdapter mAdapter;

    private final Handler mHandler = new Handler();
    private final List<ChatMessage> mChatMessageItems = new ArrayList<>();
    private final Random mRandom = new Random();

    private String mSendContent;
    private boolean isKeyboardUp = false;
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
        mEtContent = (EditText) findViewById(R.id.main_et_content);
        mBtnSend = (Button) findViewById(R.id.main_btn_send);
        mLinearSendFrame = (LinearLayout) findViewById(R.id.main_linear_send_frame);
        mScrollView = (ScrollView) findViewById(R.id.main_sv_content);
        setUpListView();
    }

    public void setUpListView(){

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
        mInputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    private void initListener(){
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSendContent = charSequence.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ChatMessage chatMessage = new ChatMessage("",mSendContent,ChatMessage.TYPE_SEND,ChatMessage.STATUS_SENDING);
                mAdapter.addNextItem(chatMessage);
                mRlv.setSelection(mAdapter.getCount());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        chatMessage.setMsgStatus(ChatMessage.STATUS_SUCCEED);
                        mAdapter.notifyDataSetChanged();
                    }
                },1000);

                //消除文字
                mSendContent = "";
                mEtContent.setText(mSendContent);
            }
        });
        /**
         * 点击ScrollView的时候，隐藏发送框
         */
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isKeyboardUp){
                    mInputManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
                    isKeyboardUp = false;
                }
                return false;
            }
        });
        mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //判断keyboard是否显示或则隐藏
                int otherHeight = DimenUtils.getStatusBarHeight(MainActivity.this) +
                        DimenUtils.getActionbarHeight(MainActivity.this) + DimenUtils.getNavBarHeight(MainActivity.this);
                int distance = DimenUtils.getScreenHeight(MainActivity.this) -
                        DimenUtils.getContentLayoutHeight(MainActivity.this);
                //如果之间的距离大于其他零件的距离，说明根布局缩小了
                if (distance > otherHeight){
                    isKeyboardUp = true;
                }
                else {
                    isKeyboardUp = false;
                }
            }
        };

        //当根布局发生变化的时候的监听。
        getWindow().getDecorView().getRootView().getViewTreeObserver()
                .addOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //动态修改ListView的大小
        if (hasFocus){
            //当前可展示空间的高度
            int contentHeight = DimenUtils.getContentLayoutHeight(this);
            int sendFrameHeight = mLinearSendFrame.getHeight();
            int listViewHeight = contentHeight - sendFrameHeight;
            //动态设置高度
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, listViewHeight
            );
            mRlv.setLayoutParams(layoutParams);
        }
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
                mAdapter.addNextItem(new ChatMessage("","测试功能是否没问题",mRandom.nextInt(2),ChatMessage.STATUS_SUCCEED));
                mRlv.setSelection(mAdapter.getCount());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        //解除注册
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            getWindow().getDecorView().getViewTreeObserver()
                    .removeOnGlobalLayoutListener(mGlobalLayoutListener);
        }
        else {
            getWindow().getDecorView().getViewTreeObserver()
                    .removeGlobalOnLayoutListener(mGlobalLayoutListener);
        }
        super.onDestroy();
    }

}
