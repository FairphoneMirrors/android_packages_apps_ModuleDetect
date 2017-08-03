package com.fairphone.moduledetect;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class CameraSwapIntentService extends IntentService {
    static final String ACTION_NOTIFICATION_DISMISS = "com.fairphone.moduledetect.notification_dismiss";
    static final String ACTION_NOTIFICATION_OPEN    = "com.fairphone.moduledetect.notification_open";
    static final String ACTION_NOTIFICATION_SWIPE   = "com.fairphone.moduledetect.notification_swipe";

    public CameraSwapIntentService() {
        super("CameraSwapIntentService");
    }


    public static PendingIntent getPendingDismissIntent(Context context) {
        Intent intent = new Intent(context, CameraSwapIntentService.class);
        intent.setAction(ACTION_NOTIFICATION_DISMISS);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    public static PendingIntent getPendingOpenIntent(Context context) {
        String url = context.getString(R.string.camera_swap_url);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.putExtra("android.support.customtabs.extra.SESSION", context.getPackageName());
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getPendingSoftDismissIntent(Context context) {
        Intent intent = new Intent(context, CameraSwapIntentService.class);
        intent.setAction(ACTION_NOTIFICATION_SWIPE);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NOTIFICATION_DISMISS.equals(action)) {
                handleActionDismissNotification();
            } else if (ACTION_NOTIFICATION_SWIPE.equals(action)) {
                handleActionSwipeNotification();
            }
        }
    }

    private void handleActionSwipeNotification() {
        Log.d("ModuleDetect", "Camera Swap notification dismissed. Will show again on next reboot.");
    }

    private void handleActionDismissNotification() {
        Log.d("ModuleDetect", "Camera Swap notification dismissed.");
        CameraSwapNotification.dismiss(this);
    }
}
