package com.newbiechen.chatdemo.widget;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.newbiechen.chatdemo.R;

/**
 * Created by PC on 2016/11/20.
 */

public class ChatFrameView extends RelativeLayout
        implements KeyboardStateHelper.OnKeyboardStateChangeListener{

    private CheckBox mCbFace;
    private CheckBox mCbMore;
    private EditText mEtInput;
    private Button mBtnSend;

    private Context mContext;
    private InputMethodManager mImm;
    private KeyboardStateHelper mStateHelper;
    private OnChatFrameListener mChatFrameListener;
    private String mSendContent = "";

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

        changeButtonState();
    }

    private void initWidget(){
        //这里有可能出错，小心
        mStateHelper = new KeyboardStateHelper((AppCompatActivity) getContext());
        mStateHelper.setOnKeyboardStateChangeListener(this);

        mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        changeButtonState();
    }

    /**
     * 设置Button是否可点击
     */
    private void changeButtonState(){
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
                changeButtonState();
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
                    changeButtonState();
                }
            }
        });
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

    public void openKeyBoard(){
        if (!mStateHelper.getKeyboardState()){
            mImm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
            mStateHelper.setKeyboardState(true);
        }
    }

    public void hideKeyBoard(){
        Log.d("ChatFrameView",mStateHelper.getKeyboardState()+"");
        if (mStateHelper.getKeyboardState()){
            mImm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
            mStateHelper.setKeyboardState(false);
        }
    }

    public void removeChatFrame(){
        mStateHelper.removeKeyboardStateHelper();
    }
}
