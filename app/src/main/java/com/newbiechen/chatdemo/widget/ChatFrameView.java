package com.newbiechen.chatdemo.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.newbiechen.chatdemo.R;
import com.newbiechen.chatdemo.fragment.FaceContentFragment;
import com.newbiechen.chatdemo.fragment.ToolFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2016/11/20.
 */

public class ChatFrameView extends RelativeLayout
        implements KeyboardStateHelper.OnKeyboardStateChangeListener{
    public static final int STATE_HIDE = 0;
    public static final int STATE_FACE = 1;
    public static final int STATE_MORE = 2;

    private ViewPager mVpBox;
    private CheckBox mCbFace;
    private CheckBox mCbMore;
    private EditText mEtInput;
    private Button mBtnSend;

    private Context mContext;
    private InputMethodManager mImm;
    private KeyboardStateHelper mStateHelper;
    private OnChatFrameListener mChatFrameListener;

    private FaceContentFragment mFaceContentFragment;
    private ToolFragment mToolFragment;

    private String mSendContent = "";
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private int mFrameState = STATE_HIDE;

    public ChatFrameView(Context context) {
        this(context,null);
    }

    public ChatFrameView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ChatFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        initView();
        initWidget();
        initListener();
    }

    private void initView(){
        //将聊天框加入到该Layout中
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_chat_frame,this,false);
        addView(view);

        mCbFace = (CheckBox) view.findViewById(R.id.frame_cb_face);
        mCbMore = (CheckBox) view.findViewById(R.id.frame_cb_more);
        mEtInput = (EditText) view.findViewById(R.id.frame_et_message);
        mBtnSend = (Button) view.findViewById(R.id.frame_btn_send);
        mVpBox = (ViewPager) view.findViewById(R.id.frame_vp_box);
        changeSendState();
    }

    private void initWidget(){
        //这里有可能出错，小心
        mStateHelper = new KeyboardStateHelper((AppCompatActivity) getContext());
        mStateHelper.setOnKeyboardStateChangeListener(this);
        mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mEtInput.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"fonts/AppleColorEmoji.ttf"));
        changeSendState();
        setUpViewPager();
    }

    private void setUpViewPager(){
        mFaceContentFragment = new FaceContentFragment();
        mToolFragment = new ToolFragment();

        final List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(mFaceContentFragment);
        mFragmentList.add(mToolFragment);

        AppCompatActivity mActivity = (AppCompatActivity) mContext;
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(mActivity.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };
        mVpBox.setAdapter(adapter);
    }

    /**
     * 设置Button是否可点击
     */
    private void changeSendState(){
        if (mSendContent.equals("")){
            mBtnSend.setSelected(false);
        }
        else{
            mBtnSend.setSelected(true);
        }
    }

    private void initListener(){
        //当输入的时候的监听
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSendContent = charSequence.toString().trim();
                changeSendState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //发送数据的点击事件
        mBtnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChatFrameListener != null && !mSendContent.equals("")){
                    //发送数据
                    mChatFrameListener.sendMessage(mSendContent);
                    mSendContent = "";
                    mEtInput.setText(mSendContent);
                    changeSendState();
                }
            }
        });

        mCbFace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFrameState(STATE_FACE);
                mVpBox.setCurrentItem(0,false);
            }
        });

        mCbMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFrameState(STATE_MORE);
                mVpBox.setCurrentItem(1,false);
            }
        });

        mEtInput.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏toolbox
                hideToolBox();
            }
        });

        mVpBox.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //切换按钮的状态
                switch (position){
                    case 0:
                        changeState(STATE_FACE);
                        break;
                    case 1:
                        changeState(STATE_MORE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mFaceContentFragment.setOnFaceClickListener(new FaceContentFragment.OnFaceClickListener() {
            @Override
            public void onFaceClick(String code) {
                mEtInput.getText().append(code);
            }
        });
    }

    private void checkFrameState(int state){
        //判断是否ViewPager打开了，如果打开了，并且等于当前状态，就表示关闭
        if (isBoxOpening() && state == mFrameState){
            hideToolBox();
        }
        else {
            //首先关闭软键盘
            hideKeyBoard();
            //首先关闭软件盘后再打开工具箱
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mVpBox.setVisibility(View.VISIBLE);
                }
            }, 50);
            changeState(state);
        }
    }

    private void changeState(int state){
        //修改当前状态
        mFrameState = state;

        //将CheckBox的状态修改回来
        mCbFace.setChecked(state == STATE_FACE);
        mCbMore.setChecked(state == STATE_MORE);
    }

    private void hideKeyBoard(){
        if (mStateHelper.getKeyboardState()){
            mImm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
            mStateHelper.setKeyboardState(false);
        }
    }

    private void hideToolBox(){
        if (mFrameState != STATE_HIDE){
            //首先隐藏ViewPager
            mVpBox.setVisibility(GONE);
            //将按钮恢复原状
            mCbFace.setChecked(false);
            mCbMore.setChecked(false);
            //修改当前状态
            mFrameState = STATE_HIDE;
        }
    }

    private boolean isBoxOpening(){
        return mVpBox.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onKeyboardOpen(int keyboardHeight) {
    }

    @Override
    public void onKeyboardClosed() {
    }

    public interface OnChatFrameListener{
        //发送数据的回调
        void sendMessage(String inputContent);
    }

    /*************************************公共的方法**********************************/
    public void setOnChatFrameListener(OnChatFrameListener listener){
        mChatFrameListener = listener;
    }

    public int getFrameState(){
        return mFrameState;
    }

    public void hideLayout(){
        hideToolBox();
        hideKeyBoard();
    }

    public void removeChatFrame(){
        mStateHelper.removeKeyboardStateHelper();
    }
}
