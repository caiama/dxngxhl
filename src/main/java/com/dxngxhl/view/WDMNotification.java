package com.dxngxhl.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.List;

/**
 * Created by wadema on 2017/6/6.
 * 通知
 *
 */

/*调用方式:*/
/**

调用方式:

-----普通
 WDMNotification.create(NotificationTestActivity.this)
                    .setInfo(R.mipmap.ic_launcher,"ceshi","内容")
                    .send();

-----扩展(数组或list)
 WDMNotification.create(NotificationTestActivity.this)
                        .setInfo(R.mipmap.ic_launcher,"扩展ceshi","内容")
                        .addArray("打开title",new String[]{"111","222"})
                        .send();

-----跳转activity
 WDMNotification.create(NotificationTestActivity.this)
                        .setInfo(R.mipmap.ic_launcher,"扩展ceshi","内容")
                        .setIntent(intent)
                        .send();

 默认普通id为1
 扩展id为2
 自定义id-->.setId(id);
*/
public class WDMNotification {
    private static NotificationCompat.Builder builder = null;
    private static NotificationManager notificationManager = null;
    private static WDMNotification wmNotification = null;
    private static NotificationCompat.InboxStyle inboxStyle;
    private static Context sContext;
    private static int anInt = 0;

    public static WDMNotification create(Context context) {
        sContext = context;
        if (wmNotification == null)
            wmNotification = new WDMNotification();
        if (builder == null)
            builder = new NotificationCompat.Builder(context);
        if (notificationManager == null)
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return wmNotification;
    }

    public static WDMNotification setInfo(int icon, String title, String content) {
        builder.setSmallIcon(icon);
        builder.setContentTitle(title);
        builder.setContentText(content);
        anInt = 1;
        return wmNotification;
    }

    public static WDMNotification addArray(String title,String[] moreArrays) {
        inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(title);
        for (int i = 0; i < moreArrays.length; i++) {
            inboxStyle.addLine(moreArrays[i]);
        }
        builder.setStyle(inboxStyle);
        anInt = 2;
        return wmNotification;
    }

    public static WDMNotification assArray(String title,List<String> moreList) {
        inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(title);
        for (int i = 0; i < moreList.size(); i++) {
            inboxStyle.addLine(moreList.get(i));
        }
        builder.setStyle(inboxStyle);
        anInt = 2;
        return wmNotification;
    }

    public static WDMNotification setIntent(Intent intent) {
        // 需要VIBRATE权限--震动
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        //自定义打开的界面
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(sContext, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        return wmNotification;
    }

    public static WDMNotification setId(int id) {
        anInt = id;
        return wmNotification;
    }

    public static WDMNotification customlayout(int layout){
        setInfo(android.R.drawable.sym_def_app_icon,"","");
        RemoteViews remoteViews = new RemoteViews(sContext.getPackageName(),layout);
        builder.setContent(remoteViews);
        anInt = 3;
        return wmNotification;
    }

    public void send() {
        notificationManager.notify(anInt,builder.build());
    }
}
