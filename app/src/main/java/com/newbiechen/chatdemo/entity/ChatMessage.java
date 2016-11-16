package com.newbiechen.chatdemo.entity;

import android.widget.ImageView;

/**
 * Created by PC on 2016/11/15.
 */

public class ChatMessage {
    //判断信息使哪一方发送的
    public static final int TYPE_REPLY = 0;
    public static final int TYPE_SEND = 1;

    //判断当前信息状态
    public static final int STATUS_SENDING = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_SUCCEED=  4;

    //头像
    private String imgUrl;
    //内容
    private String content;
    //接收方还是发送方
    private int userType;

    //当前Msg的状态
    private int msgStatus;

    public ChatMessage(String icon, String content, int userType,int msgStatus) {
        this.imgUrl = icon;
        this.content = content;
        this.userType = userType;
        this.msgStatus = msgStatus;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }
}
