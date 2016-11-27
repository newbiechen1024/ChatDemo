package com.newbiechen.chatdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.newbiechen.chatdemo.R;

/**
 * Created by PC on 2016/11/25.
 */

public class FaceCategoryFragment extends Fragment {
    private GridView mGvItem;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_face_category,container,false);
        initView(v);
        return v;
    }

    private void initView(View view){
        mGvItem = (GridView) view.findViewById(R.id.category_gv_item);
    }
}
