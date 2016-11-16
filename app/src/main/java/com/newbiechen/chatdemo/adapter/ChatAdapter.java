package com.newbiechen.chatdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.newbiechen.chatdemo.R;
import com.newbiechen.chatdemo.entity.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by PC on 2016/11/15.
 */

public class ChatAdapter extends BaseAdapter {
    private final List<ChatMessage> mChatMsgItems = new ArrayList<>();
    private Context mContext;

    public ChatAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mChatMsgItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mChatMsgItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChatMessageViewHolder holder = null;
        if (view == null){
            int userType = getItemViewType(i);
            holder = new ChatMessageViewHolder();
            //获取View
            if (userType == ChatMessage.TYPE_REPLY){
                view = LayoutInflater.from(mContext)
                        .inflate(R.layout.adapter_msg_left,viewGroup,false);
            }
            else if (userType == ChatMessage.TYPE_SEND){
                view = LayoutInflater.from(mContext)
                        .inflate(R.layout.adapter_msg_right,viewGroup,false);
            }
            holder.ivIcon = (ImageView) view.findViewById(R.id.msg_iv_icon);
            holder.tvContent = (TextView) view.findViewById(R.id.msg_tv_content);
            holder.pbSending = (ProgressBar) view.findViewById(R.id.msg_pb_load);
            holder.ivSendFail = (ImageView) view.findViewById(R.id.msg_iv_send_fail);

            view.setTag(holder);
        }
        else {
            holder = (ChatMessageViewHolder) view.getTag();
        }
        //初始化数据
        ChatMessage msg = (ChatMessage) getItem(i);
        holder.ivIcon.setImageResource(R.mipmap.default_head);
        holder.tvContent.setText(msg.getContent());
        //判断当前img状态
        switch (msg.getMsgStatus()){
            case ChatMessage.STATUS_SENDING:
                holder.pbSending.setVisibility(View.VISIBLE);
                holder.ivSendFail.setVisibility(GONE);
                break;
            case ChatMessage.STATUS_FAIL:
                holder.pbSending.setVisibility(GONE);
                holder.ivSendFail.setVisibility(View.VISIBLE);
                break;
            case ChatMessage.STATUS_SUCCEED:
                holder.pbSending.setVisibility(GONE);
                holder.pbSending.setVisibility(GONE);
                break;
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return mChatMsgItems.get(position).getUserType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    class ChatMessageViewHolder{
        ImageView ivIcon;
        TextView tvContent;
        ProgressBar pbSending;
        ImageView ivSendFail;
    }

    /****************************************************/

    public void addPreItem(ChatMessage chatMessage){
        mChatMsgItems.add(0,chatMessage);
        notifyDataSetChanged();
    }

    public void addNextItem(ChatMessage chatMessage){
        mChatMsgItems.add(chatMessage);
        notifyDataSetChanged();
    }

    public void addPreItems(List<ChatMessage> chatMessageList){
        mChatMsgItems.addAll(0,chatMessageList);
        notifyDataSetChanged();
    }

    public void addItems(List<ChatMessage> chatMessageList){
        mChatMsgItems.addAll(chatMessageList);
        notifyDataSetChanged();
    }
}
