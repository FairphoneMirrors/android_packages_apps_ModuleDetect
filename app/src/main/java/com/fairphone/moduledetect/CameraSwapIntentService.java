package com.fairphone.moduledetect;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CameraSwapIntentService extends IntentService {
    static final String ACTION_NOTIFICATION_DISMISS = "com.fairphone.moduledetect.notification_dismiss";
    static final String ACTION_NOTIFICATION_SWIPE   = "com.fairphone.moduledetect.notification_swipe";
    static final String PACKAGE_CAMERA_SWAP_INFO = "com.fairphone.cameraswapinfo";
    static final String ACTIVITY_CAMERA_SWAP_INFO_DETAILS =
            "com.fairphone.cameraswapinfo.CameraSwapDetailsActivity";


    public CameraSwapIntentService() {
        super("CameraSwapIntentService");
    }


    public static PendingIntent getPendingDismissIntent(Context context) {
        Intent intent = new Intent(context, CameraSwapIntentService.class);
        intent.setAction(ACTION_NOTIFICATION_DISMISS);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    public static PendingIntent getPendingOpenIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(PACKAGE_CAMERA_SWAP_INFO, ACTIVITY_CAMERA_SWAP_INFO_DETAILS);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
