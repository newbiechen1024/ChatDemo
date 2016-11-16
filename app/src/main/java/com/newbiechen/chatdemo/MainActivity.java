package com.newbiechen.chatdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.newbiechen.chatdemo.adapter.ChatAdapter;
import com.newbiechen.chatdemo.entity.ChatMessage;
import com.newbiechen.chatdemo.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RefreshListView mRlv;
    private EditText mEtContent;
    private Button mBtnSend;

    private ChatAdapter mAdapter;
    private final Handler mHanlder = new Handler();
    private final List<ChatMessage> mChatMessageItems = new ArrayList<>();
    private final Random mRandom = new Random();

    private String mSendContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView(){
        mRlv = (RefreshListView) findViewById(R.id.main_rlv);
        mEtContent = (EditText) findViewById(R.id.main_et_content);
        mBtnSend = (Button) findViewById(R.id.main_btn_send);

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
            }
        });
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
                mHanlder.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        chatMessage.setMsgStatus(ChatMessage.STATUS_SUCCEED);
                        mAdapter.notifyDataSetChanged();
                    }
                },1000);
            }
        });
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

}
