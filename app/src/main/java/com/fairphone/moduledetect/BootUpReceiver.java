package com.fairphone.moduledetect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BootUpReceiver extends BroadcastReceiver {

    private static boolean isChangedModule() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/system/bin/getprop",
                    "ro.build.fingerprint"});
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            return "1".equals(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if(isChangedModule() || CameraSwapNotification.needsDismissal(context)) {
                CameraSwapNotification.showNotification(context);
            }
        }
    }
}
