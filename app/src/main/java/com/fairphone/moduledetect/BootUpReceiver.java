package com.fairphone.moduledetect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BootUpReceiver extends BroadcastReceiver {

    private static boolean isChangedModule(Context context) {
        CameraModules cm = new CameraModules(context);
        cm.updateCameraDetection();

        return cm.changedSinceLastBoot();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if(isChangedModule(context) || CameraSwapNotification.needsDismissal(context)) {
                CameraSwapNotification.showNotification(context);
            }
        }
    }
}
