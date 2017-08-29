package com.fairphone.moduledetect;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ModuleDetectIntentService extends IntentService {
    public static final String ACTION_MODULE_CHANGED = "com.fairphone.moduledetect.module_changed";
    private static final String TAG = "ModuleDetect";

    private static final String PACKAGE_CAMERA_SWAP_INFO =
            "com.fairphone.cameraswapinfo";
    private static final String SERVICE_CAMERA_SWAP_NOTIFICATION_SERVICE =
            "com.fairphone.cameraswapinfo.CameraSwapNotificationService";
    private static final String ACTION_HANDLE_CAMERA_CHANGED =
            "com.fairphone.cameraswapinfo.handle_camera_change";

    private static final String PACKAGE_CALIBRATION_SERVICE =
            "com.fairphone.psensor";
    private static final String SERVICE_CALIBRATION_SERVICE =
            "com.fairphone.psensor.CalibrationService";
    private static final String ACTION_CALIBRATION_SERVICE_HANDLE_RECEIVER_MODULE_CHANGED =
            "com.fairphone.psensor.action.handle_receiver_module_changed";

    public static void startActionModuleChanged(Context context, boolean hasReceiverModuleChanged,
            boolean hasCameraModuleChanged) {
        context.startService(
                new Intent(context, ModuleDetectIntentService.class)
                        .setAction(ACTION_MODULE_CHANGED)
                        .putExtra("receiver_module_changed", hasReceiverModuleChanged)
                        .putExtra("camera_module_changed", hasCameraModuleChanged));
    }

    public ModuleDetectIntentService() {
        super("ModuleDetectIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_MODULE_CHANGED.equals(action)) {
                handleActionModuleChanged(intent);
            }
        }
    }

    private void handleActionModuleChanged(Intent intent) {
        boolean hasReceiverModuleChanged = intent.getBooleanExtra("receiver_module_changed", false);
        boolean hasCameraModuleChanged = intent.getBooleanExtra("camera_module_changed", false);

        if (!hasReceiverModuleChanged && !hasCameraModuleChanged) {
            Log.w(TAG, "handleActionComponentSwapped: No camera changed; Doing nothing...");
        } else {
            Log.w(TAG, "handleActionComponentSwapped: Camera change detected; Launching CameraSwapInfo!");
            /* launch CameraSwapInfo notification intent */
            startService(getCameraSwapIntent(hasReceiverModuleChanged, hasCameraModuleChanged));

            if (hasReceiverModuleChanged) {
                Log.w(TAG, "handleActionCameraSwapped: Receiver module change detected; Launching ProximitySensor!");
                /* Propagate the change of receiver module to the ProximitySensor calibration service */
                startService(getCalibrationIntent());
            }
        }
    }

    private Intent getCameraSwapIntent(boolean hasReceiverModuleChanged, boolean hasCameraModuleChanged) {
        Intent intent = new Intent();
        intent.setClassName(PACKAGE_CAMERA_SWAP_INFO, SERVICE_CAMERA_SWAP_NOTIFICATION_SERVICE);
        intent.setAction(ACTION_HANDLE_CAMERA_CHANGED);
        intent.putExtra("frontCameraChanged", hasReceiverModuleChanged);
        intent.putExtra("mainCameraChanged", hasCameraModuleChanged);
        return intent;
    }

    private Intent getCalibrationIntent() {
        Intent intent = new Intent();
        intent.setClassName(PACKAGE_CALIBRATION_SERVICE, SERVICE_CALIBRATION_SERVICE);
        intent.setAction(ACTION_CALIBRATION_SERVICE_HANDLE_RECEIVER_MODULE_CHANGED);
        return intent;
    }
}
