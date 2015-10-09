package com.chickenger.islam.tracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DefaultPrefs {

    private static SharedPreferences preferences;

    public static SharedPreferences getInstance(Context context) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return preferences;
    }
}
