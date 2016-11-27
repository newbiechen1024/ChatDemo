package com.newbiechen.chatdemo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newbiechen.chatdemo.R;
import com.newbiechen.chatdemo.base.BaseAdapter;
import com.newbiechen.chatdemo.widget.FaceTextView;

/**
 * Created by PC on 2016/11/26.
 */

public class FaceAdapter extends BaseAdapter<String,FaceAdapter.FaceViewHolder> {

    private Context mContext;
    private Typeface mTypeFace;
    public FaceAdapter(Context context) {
        mContext = context;
        mTypeFace = Typeface.createFromAsset(mContext.getAssets(),"fonts/AppleColorEmoji.ttf");
        initItem();
    }

    /**
     * 初始化表情
     */
    private void initItem(){

        for (int i=0x1F600;i<0x1F635; i = i + 0x1){
            String str = new String(Character.toChars(i));
            mItemList.add(str);
        }
    }

    @Override
    public FaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_face,parent,false);

        return new FaceViewHolder(view);
    }


    @Override
    public void setUpViewHolder(FaceViewHolder holder, int position) {
        holder.tvFace.setText(mItemList.get(position));
    }

    class FaceViewHolder extends RecyclerView.ViewHolder{
        TextView tvFace;
        public FaceViewHolder(View itemView) {
            super(itemView);
            tvFace = (TextView) itemView.findViewById(R.id.face_tv_face);
            tvFace.setTypeface(mTypeFace);
        }
    }
}
