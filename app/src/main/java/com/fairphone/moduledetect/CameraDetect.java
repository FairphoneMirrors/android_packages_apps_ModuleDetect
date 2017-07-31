package com.fairphone.moduledetect;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Wrapper for finding currently installed front and main camera sensors
 */

public class CameraDetect {

    private static final String TAG = "CameraDetect";

    private static final String SHELL_BIN = "/system/bin/sh";
    private static final String GREP_BIN = "/system/bin/grep";
    private static final String CAT_BIN = "/system/bin/cat";
    private static final String CAMERA_SUBDEVICE_PATH =
            "/sys/devices/fd8c0000.qcom,msm-cam/video4linux/*/name";
    private static final String CAMERA_DETECT_COMMAND =
            CAT_BIN + " " + CAMERA_SUBDEVICE_PATH + " | " + GREP_BIN;

    private String mainSensorName = "";
    private String frontSensorName = "";

    public CameraDetect(Context context) {
        Resources res = context.getResources();

        setMainSensorName(runCameraDetectCommand(res.getStringArray(R.array.sensor_names_main)));
        setFrontSensorName(runCameraDetectCommand(res.getStringArray(R.array.sensor_names_front)));
    }

    public String getMainSensorName() {
        return mainSensorName;
    }

    public String getFrontSensorName() {
        return frontSensorName;
    }

    private void setMainSensorName(String mainSensorName) {
        this.mainSensorName = mainSensorName;
    }

    private void setFrontSensorName(String frontSensorName) {
        this.frontSensorName = frontSensorName;
    }

    /**
     * Find which sensor name of {@code sensorNames} is installed
     *
     * @param sensorNames List of sensor names to match against
     * @return First sensor name that is matched. Empty String otherwise.
     */
    private String runCameraDetectCommand(String[] sensorNames) {
        String command = CAMERA_DETECT_COMMAND;

        for (String sensor : sensorNames) {
            command += " -e " + sensor;
        }

        String sensor = "";
        try {
            Process p = Runtime.getRuntime().exec(new String[]{ SHELL_BIN, "-c", command });
            // Only one sensor out of the list sensorNames can be installed
            sensor = new BufferedReader(new InputStreamReader(p.getInputStream())).readLine();
        } catch (IOException e) {
            Log.e(TAG, "Could not get sensor name from '" + command + "': " + e.getMessage());
        }

        return sensor;
    }
}
