package com.dxngxhl.view.intf;

import android.content.Context;
import android.content.Intent;

import com.dxngxhl.view.WDMNotification;

import java.util.List;

/**
 * Created by Cool on 2017/6/6.
 */

public interface WDMNotificationIntf {

    WDMNotification create(Context context);
    /**
     * 普通通知
     * @param icon :图标
     * @param title :标题
     * @param content :通知内容
     * */
    WDMNotification setInfo(int icon, String title, String content);
    /**
     * 通知扩展
     * @param title ：展开扩展时的tite
     * @param moreArrays :扩展内容(数组),可为空
     * */
    WDMNotification addArray(String title, String[] moreArrays);
    /**
     * 通知扩展
     * @param title ：展开扩展时的tite
     * @param moreList :扩展内容(数列),可为空
     * */
    WDMNotification assArray(String title, List<String> moreList);
    /**
     * 通知跳转
     * @param intent :跳转
     * */
    WDMNotification setIntent(Intent intent);
    /**
     * 通知跳转
     * @param notId :通知id
     * */
    WDMNotification setId(int notId);
    /**
     * 自定义通知布局
     * */
    WDMNotification customlayout(int layout);
    /**
     * 发送
     * */
    void send();
}
