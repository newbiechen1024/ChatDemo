package com.newbiechen.demo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.newbiechen.demo.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RefreshListView mListView;

    private ArrayAdapter<String> mAdapter;
    private final Handler mHandler = new Handler();

    private final List<String> mStringItem = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
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

        setUpListView();
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
                        mListView.setSelection(21);

                        //无效的原因是，notifyDataSetChanged()是异步加载
                        mListView.onRefreshComplete();
                    }
                },2000);
            }
        });
    }
}
