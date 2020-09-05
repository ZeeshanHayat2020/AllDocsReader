package com.example.alldocumentreader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LanguageManager {


    public static final String LANGUAGE_KEY_ENGLISH = "en";
    public static final String LANGUAGE_KEY_SPANISH = "es";
    public static final String LANGUAGE_KEY_ARABIC = "ar";
    public static final String LANGUAGE_KEY_RUSSIA = "ru";
    public static final String LANGUAGE_KEY_FRENCH = "fr";
    public static final String LANGUAGE_KEY_CHINES = "zh-rCN";
    public static final String LANGUAGE_KEY_GERMAN = "de";
    public static final String LANGUAGE_KEY_INDONESIAN = "in";
    public static final String LANGUAGE_KEY_ITALIAN = "it";
    public static final String LANGUAGE_KEY_POLISH = "pl";
    public static final String LANGUAGE_KEY_PORTUGUESE = "pt";

    private static final String LANGUAGE_KEY = "language_key";

    public static Context setLocale(Context mContext) {
        return updateResources(mContext, getLanguagePref(mContext));
    }

    public static Context setNewLocale(Context mContext, String mLocaleKey) {
        setLanguagePref(mContext, mLocaleKey);
        return updateResources(mContext, mLocaleKey);
    }

    public static String getLanguagePref(Context mContext) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        return mPreferences.getString(LANGUAGE_KEY, "");
    }


    private static void setLanguagePref(Context mContext, String localeKey) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mPreferences.edit().putString(LANGUAGE_KEY, localeKey).commit();
    }


    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;
    }
}
