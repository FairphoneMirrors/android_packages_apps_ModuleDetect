package com.fairphone.moduledetect;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Wrapper for /system/bin/camera_detect
 */

public class CameraDetect {

    private static final String TAG = "CameraDetect";

    private static final String CAMERA_DETECT_BINARY = "/system/bin/camera_detect";

    private static final String FIELD_DELIM = ",";
    private static final String VALUE_DELIM = ":";

    private static final String DEV_FIELD_NAME = "dev";
    private static final String SENSOR_FIELD_NAME = "sensor";
    private static final String POSITION_FIELD_NAME = "pos";

    private static final String FRONT_POSITION = "front";
    private static final String MAIN_POSITION = "main";

    private String mainSensorName = "";
    private String mainSensorDev = "";
    private String frontSensorName = "";
    private String frontSensorDev = "";

    public CameraDetect() {
        try {
            setSensorInformation(runCameraDetectBin());
        } catch (IOException e) {
            Log.e(TAG, "Could not get output from " + CAMERA_DETECT_BINARY + ": " + e.getMessage());
        }
    }

    public String getMainSensorName() {
        return mainSensorName;
    }

    public String getMainSensorDev() {
        return mainSensorDev;
    }

    public String getFrontSensorName() {
        return frontSensorName;
    }

    public String getFrontSensorDev() {
        return frontSensorDev;
    }

    private BufferedReader runCameraDetectBin() throws IOException {
        Process p = Runtime.getRuntime().exec(CAMERA_DETECT_BINARY);
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private void setSensorInformation(BufferedReader sensors) throws IOException {
        for (String line = sensors.readLine(); line != null; line = sensors.readLine()) {
            parseSensorInformation(line);
        }
    }

    private void parseSensorInformation(String sensorInfo) {
        StringTokenizer st = new StringTokenizer(sensorInfo, FIELD_DELIM);

        try {
            String deviceName = parseField(st.nextToken(), DEV_FIELD_NAME);
            String sensorName = parseField(st.nextToken(), SENSOR_FIELD_NAME);
            String sensorPosition = parseField(st.nextToken(), POSITION_FIELD_NAME);

            if (FRONT_POSITION.equals(sensorPosition)) {
                frontSensorDev = deviceName;
                frontSensorName = sensorName;
            } else if (MAIN_POSITION.equals(sensorPosition)) {
                mainSensorDev = deviceName;
                mainSensorName = sensorName;
            } else {
                Log.w(TAG, "Unknown position specifier '" + sensorPosition +"' in " + sensorInfo);
            }
        } catch (NoSuchElementException e) {
            Log.e(TAG, "Could not parse sensor information: " + sensorInfo);
        }
    }

    private String parseField(String field, String fieldName) throws NoSuchElementException {
        StringTokenizer st = new StringTokenizer(field, VALUE_DELIM);

        if (!st.nextToken().equals(fieldName)) {
            throw new NoSuchElementException();
        }

        return st.nextToken();
    }
}
