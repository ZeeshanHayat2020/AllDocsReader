package com.example.alldocumentreader.database;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "com.example.alldocumentreader";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String KEY_PREF_LANGUAGE_SELECTED = "KEY_PREF_LANGUAGE_SELECTED";
    private static final String KEY_PREF_PRIVACY_POLICY_ACCEPTANCE = "KEY_PREF_PRIVACEY_POLICY_ACCEPTANCE";
    private static final String KEY_PREF_IN_APP_IS_ITEM_PURCHASE = "KEY_PREF_IN_APP_IS_ITEM_PURCHASE";

    public MyPreferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLanguageSelected(boolean languageSelected) {
        editor.putBoolean(KEY_PREF_LANGUAGE_SELECTED, languageSelected);
        editor.commit();
    }

    public boolean isLanguageSelected() {
        return pref.getBoolean(KEY_PREF_LANGUAGE_SELECTED, false);
    }

    public void setPrivacyPolicyAcceptance(boolean isAccepted) {
        editor.putBoolean(KEY_PREF_PRIVACY_POLICY_ACCEPTANCE, isAccepted);
        editor.commit();
    }

    public boolean isPrivacyPolicyAccepted() {
        return pref.getBoolean(KEY_PREF_PRIVACY_POLICY_ACCEPTANCE, false);
    }

    public void setIsItemPurchased(boolean isItemPurchased) {
        editor.putBoolean(KEY_PREF_IN_APP_IS_ITEM_PURCHASE, isItemPurchased);
        editor.commit();
    }

    public boolean isItemPurchased() {
        return pref.getBoolean(KEY_PREF_IN_APP_IS_ITEM_PURCHASE, false);

    }


}