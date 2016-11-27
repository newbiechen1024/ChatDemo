package com.newbiechen.chatdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.newbiechen.chatdemo.R;
import com.newbiechen.chatdemo.adapter.FaceAdapter;
import com.newbiechen.chatdemo.base.BaseAdapter;

/**
 * Created by PC on 2016/11/26.
 */

public class FaceContentFragment extends Fragment {
    private RecyclerView mRvShow;
    private FaceAdapter mFaceAdapter;
    private OnFaceClickListener mFaceClickListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_face_content,container,false);
        initView(v);
        return v;
    }

    private void initView(View view){
        mRvShow = (RecyclerView) view.findViewById(R.id.content_rv_show);
        setUpGridView();
        initListener();
    }

    private void setUpGridView(){
        mRvShow.setLayoutManager(new GridLayoutManager(getContext(),
                8, GridLayoutManager.VERTICAL,false));
        mFaceAdapter = new FaceAdapter(getContext());
        mRvShow.setAdapter(mFaceAdapter);
    }

    private void initListener(){
        mFaceAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                if (mFaceClickListener != null){
                    mFaceClickListener.onFaceClick(mFaceAdapter.getItem(pos));
                }
            }
        });
    }

    public interface OnFaceClickListener{
        void onFaceClick(String code);
    }

    /*********************公共的方法*******************************/
    public void setOnFaceClickListener(OnFaceClickListener listener){
        mFaceClickListener = listener;
    }
}
