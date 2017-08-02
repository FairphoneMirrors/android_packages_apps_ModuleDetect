package com.fairphone.moduledetect;

import android.app.IntentService;
import android.content.Intent;

/**
 * An {@link IntentService} subclass for showing the camera swap notification
 * <p>
 * Use {@code com.fairphone.moduledetect.action.notification_show} to display notification:
 * e.g. {@code adb shell am startservice -a com.fairphone.moduledetect.action.notification_show com.fairphone.moduledetect/.CameraSwapShowNotificationIntentService}
 */
public class CameraSwapShowNotificationIntentService extends IntentService {

    private static final String ACTION_SHOW_NOTIFICATION =
            "com.fairphone.moduledetect.action.notification_show";

    public CameraSwapShowNotificationIntentService() {
        super("CameraSwapShowNotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SHOW_NOTIFICATION.equals(action)) {
                CameraSwapNotification.showNotification(this);
            }
        }
    }
}
