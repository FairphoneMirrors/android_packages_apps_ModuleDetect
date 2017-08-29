package com.fairphone.moduledetect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            CameraModules cm = new CameraModules(context);
            cm.updateCameraDetection();
            if (cm.changedSinceLastBoot()) {
                ModuleDetectIntentService.startActionModuleChanged(context,
                        cm.frontCameraChangedSinceLastBoot(), cm.mainCameraChangedSinceLastBoot());
            }
        }
    }
}
