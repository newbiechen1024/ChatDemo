package com.newbiechen.demo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by PC on 2016/11/17.
 */

public class TestActivity extends AppCompatActivity {
    private EditText mEtContent;
    private TextView mTvShow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mEtContent = (EditText) findViewById(R.id.test_et_content);
        mTvShow = (TextView) findViewById(R.id.test_tv_show);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/AppleColorEmoji.ttf");
        mTvShow.setTypeface(typeface);
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString().trim();
                mTvShow.setText(str);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
