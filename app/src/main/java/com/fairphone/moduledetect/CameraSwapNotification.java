package com.fairphone.moduledetect;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

public class CameraSwapNotification {
    static final int NOTIFICATION_ID = 0;

    static boolean needsDismissal(Context context) {
        SharedPreferences preferenceManager =
                PreferenceManager.getDefaultSharedPreferences(context);
        return preferenceManager.getBoolean("com.fairphone.moduledetect.notification_needs_dismissal",
                false);
    }

    public static void showNotification(Context context) {
        Notification notification = getNotification(context);
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    static Notification getNotification(Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.support.v7.appcompat.R.drawable.notification_template_icon_bg)
                        .setContentTitle(context.getString(R.string.camera_swap_notification_title))
                        .setContentText(context.getString(R.string.camera_swap_notification_text))
                        .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(context.getString(R.string.camera_swap_notification_title)))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.camera_swap_notification_text)))
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .addAction(android.support.v7.appcompat.R.drawable.notification_template_icon_bg,
                                context.getString(R.string.camera_swap_notification_dismiss),
                                CameraSwapIntentService.getPendingDismissIntent(context))
                        .addAction(android.support.v7.appcompat.R.drawable.notification_template_icon_bg,
                                context.getString(R.string.camera_swap_notification_open),
                                CameraSwapIntentService.getPendingOpenIntent(context))
                        .setDeleteIntent(CameraSwapIntentService.getPendingSoftDismissIntent(context));
        return mBuilder.build();
    }

    public static void dismiss(Context context) {
        SharedPreferences preferenceManager =
                PreferenceManager.getDefaultSharedPreferences(context);
        preferenceManager.edit().putBoolean("com.fairphone.moduledetect.notification_needs_dismissal",
                false);
    }
}