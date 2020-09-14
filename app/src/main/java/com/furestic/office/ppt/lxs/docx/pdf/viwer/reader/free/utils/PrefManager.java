package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private final SharedPreferences pref;
    private final SharedPreferences pref2;
    private final SharedPreferences prefInApp;
    private final SharedPreferences.Editor editor;
    private final SharedPreferences.Editor editor2;
    private final SharedPreferences.Editor editorInApp;

    // Shared preferences file name
    private static final String PREF_NAME = "welcome";
    private static final String PREF_NAME2 = "privacyPolicy";
    private static final String PREF_name_inAppPurchases = "inAppPurchases";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_Accepted = "isAccepted";
    private static final String IS_Purchased = "isPurchased";
    private static final String IS_AcceptedLanguage = "isAcceptedLanguage";

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        pref2 = context.getSharedPreferences(PREF_NAME2, PRIVATE_MODE);
        editor2 = pref2.edit();

        prefInApp = context.getSharedPreferences(PREF_name_inAppPurchases, PRIVATE_MODE);
        editorInApp = prefInApp.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setAccept(boolean isAccepted) {
        editor2.putBoolean(IS_Accepted, isAccepted);
        editor2.commit();
    }

    public boolean isAccepted() {
        return pref2.getBoolean(IS_Accepted, false);
    }

    public void setPurchased(boolean Purchased) {
        editorInApp.putBoolean(IS_Purchased, Purchased);
        editorInApp.commit();
    }

    public boolean isPurchased() {
        return prefInApp.getBoolean(IS_Purchased, false);
    }

    public boolean isAcceptedLanguage() {
        return pref2.getBoolean(IS_AcceptedLanguage, false);
    }

    public void setAcceptLanguage(boolean isAccepted) {
        editor2.putBoolean(IS_AcceptedLanguage, isAccepted);
        editor2.commit();
    }
}
