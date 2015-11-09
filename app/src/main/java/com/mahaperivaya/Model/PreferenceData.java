package com.mahaperivaya.Model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by m84098 on 9/14/15.
 */
public class PreferenceData {
    public static final String PREFS_NAME = "MAHAPERIVAYA";
    private static PreferenceData preferenceData;
    private static SharedPreferences settings;

    public enum PREFVALUES {
        EMAILID("emailid"),
        GENERAL_SETTINGS("GENERAL_SETTINGS");
        private String value;

        PREFVALUES(String value) {
            this.value = value;
        }
    }

    ;


    public static PreferenceData getInstance(Context context) {
        if (preferenceData == null) {
            preferenceData = new PreferenceData();
            settings = context.getSharedPreferences(PREFS_NAME, 0);
        }
        return preferenceData;
    }

    public void setValue(String strTag, Object obj) {
        SharedPreferences.Editor editor = settings.edit();
        if (obj instanceof String)
            editor.putString(strTag, (String) obj);
        if (obj instanceof Integer)
            editor.putInt(strTag, (Integer) obj);
        if (obj instanceof Boolean)
            editor.putBoolean(strTag, (Boolean) obj);
        if (obj instanceof Long)
            editor.putLong(strTag, (Long) obj);
        // Commit the edits!
        editor.commit();
    }

    public Object getValue(String strTag, Object obj) {
        Object o= null;
        if (obj instanceof String)
            o = settings.getString(strTag, (String) obj);
        if (obj instanceof Integer)
            o = settings.getInt(strTag, (Integer) obj);
        if (obj instanceof Boolean)
            o= settings.getBoolean(strTag, (Boolean) obj);
        if (obj instanceof Long)
            o = settings.getLong(strTag, (Long) obj);

        return o;
    }
}
