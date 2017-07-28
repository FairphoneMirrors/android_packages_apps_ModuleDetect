package com.fairphone.moduledetect;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Compare old and new camera sensors to detect changed camera modules
 */

public class CameraModules {

    private static final String PREVIOUS_FRONT_SENSOR_PREF =
            "com.fairphone.moduledetect.prev_front_sensor";
    private static final String PREVIOUS_MAIN_SENSOR_PREF =
            "com.fairphone.moduledetect.prev_main_sensor";
    private static final String FRONT_SENSOR_CHANGED_PREF =
            "com.fairphone.moduledetect.front_sensor_changed";
    private static final String MAIN_SENSOR_CHANGED_PREF =
            "com.fairphone.moduledetect.main_sensor_changed";

    private Context mContext;

    public CameraModules(Context context) {
        mContext = context;
    }

    public boolean changedSinceLastBoot() {
        return mainCameraChangedSinceLastBoot() || frontCameraChangedSinceLastBoot();
    }

    public boolean mainCameraChangedSinceLastBoot() {
        return getBoolPref(MAIN_SENSOR_CHANGED_PREF);
    }

    public boolean frontCameraChangedSinceLastBoot() {
        return getBoolPref(FRONT_SENSOR_CHANGED_PREF);
    }

    /**
     * Update information on what camera modules are installed and whether they have been changed
     * since last boot.
     */
    public void updateCameraDetection() {
        CameraDetect cd = new CameraDetect();

        String prevMainSensor = getStringPref(PREVIOUS_MAIN_SENSOR_PREF);
        String prevFrontSensor = getStringPref(PREVIOUS_FRONT_SENSOR_PREF);

        setChangedPref(MAIN_SENSOR_CHANGED_PREF, prevMainSensor, cd.getMainSensorName());
        setChangedPref(FRONT_SENSOR_CHANGED_PREF, prevFrontSensor, cd.getFrontSensorName());

        setStringPref(PREVIOUS_MAIN_SENSOR_PREF, cd.getMainSensorName());
        setStringPref(PREVIOUS_FRONT_SENSOR_PREF, cd.getFrontSensorName());
    }

    /**
     * Decide if camera module has changed since last boot
     * <p>
     * Camera modules are not considered changed if either of the values is empty to avoid false
     * positives in case of boot without a module or if something goes wrong.
     *
     * @param oldName Name of the sensor during previous boot
     * @param newName Name of the sensor currently inserted
     * @return True if camera module has changed since last boot.
     */
    private boolean cameraChanged(String oldName, String newName) {
        if (oldName == null || newName == null || oldName.isEmpty() || newName.isEmpty()) {
            return false;
        }
        return !oldName.equals(newName);
    }

    /**
     * Get String preference stored at {@code key}
     *
     * @param key Preference to get
     * @return preference stored at {@code key} or the emtpy String
     */
    private String getStringPref(String key) {
        SharedPreferences preferenceManager =
                PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferenceManager.getString(key, "");
    }

    /**
     * Get Boolean preference stored at {@code key}
     *
     * @param key Preference to get
     * @return preference stored at {@code key} or false
     */
    private Boolean getBoolPref(String key) {
        SharedPreferences preferenceManager =
                PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferenceManager.getBoolean(key, false);
    }

    private void setStringPref(String key, String value) {
        SharedPreferences preferenceManager =
                PreferenceManager.getDefaultSharedPreferences(mContext);
        preferenceManager.edit().putString(key, value).apply();
    }

    private void setBoolPref(String key, boolean value) {
        SharedPreferences preferenceManager =
                PreferenceManager.getDefaultSharedPreferences(mContext);
        preferenceManager.edit().putBoolean(key, value).apply();
    }

    private void setChangedPref(String key, String oldName, String newName) {
        boolean changed = cameraChanged(oldName, newName);
        setBoolPref(key, changed);
    }
}
