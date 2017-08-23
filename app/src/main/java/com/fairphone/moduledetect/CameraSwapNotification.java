package com.fairphone.moduledetect;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

public class CameraSwapNotification {
    private static final String NOTIFICATION_NEEDS_DISMISSAL_PREF =
            "com.fairphone.moduledetect.notification_needs_dismissal";
    static final int NOTIFICATION_ID = 0;

    static boolean needsDismissal(Context context) {
        SharedPreferences preferenceManager =
                PreferenceManager.getDefaultSharedPreferences(context);
        return preferenceManager.getBoolean(NOTIFICATION_NEEDS_DISMISSAL_PREF, false);
    }

    static void setNeedsDismissal(Context context) {
        SharedPreferences preferenceManager =
                PreferenceManager.getDefaultSharedPreferences(context);
        preferenceManager.edit().putBoolean(NOTIFICATION_NEEDS_DISMISSAL_PREF, true).apply();
    }

    public static void showNotification(Context context) {
        Notification notification = getNotification(context);
        setNeedsDismissal(context);
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static void destroyNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }


    static Notification getNotification(Context context) {
        PendingIntent openIntent = CameraSwapIntentService.getPendingOpenIntent(context);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_camera_swap)
                        .setContentTitle(context.getString(R.string.camera_swap_notification_title))
                        .setContentText(context.getString(R.string.camera_swap_notification_summary))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(
                                context.getString(R.string.camera_swap_notification_summary) + " "
                                        + context.getString(R.string.camera_swap_notification_text)))
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setPriority(Notification.PRIORITY_MAX)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setContentIntent(openIntent)
                        .addAction(android.support.v7.appcompat.R.drawable.notification_template_icon_bg,
                                context.getString(R.string.camera_swap_notification_dismiss),
                                CameraSwapIntentService.getPendingDismissIntent(context))
                        .addAction(android.support.v7.appcompat.R.drawable.notification_template_icon_bg,
                                context.getString(R.string.camera_swap_notification_read_now),
                                openIntent)
                        .setOngoing(true);
        return mBuilder.build();
    }

    public static void dismiss(Context context) {
        destroyNotification(context);
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        SharedPreferences preferenceManager =
                PreferenceManager.getDefaultSharedPreferences(context);
        preferenceManager.edit().putBoolean(NOTIFICATION_NEEDS_DISMISSAL_PREF, false).apply();
    }
}