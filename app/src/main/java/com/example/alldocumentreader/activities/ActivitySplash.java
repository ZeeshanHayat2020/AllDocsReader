package com.example.alldocumentreader.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.alldocumentreader.R;
import com.example.alldocumentreader.database.MyPreferences;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class ActivitySplash extends ActivityBase {

    private Handler handler;
    private Runnable runnable;
    private InterstitialAd interstitialAd;
    private int loadAttempts;
    private MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this);
        setContentView(R.layout.activity_splash);
        myPreferences = new MyPreferences(this);
        requestInterstitial();

    }

    public static void setStatusBarGradient(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.ic_main_bg);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(R.color.colorSplashNav));
            window.setBackgroundDrawable(background);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (haveNetworkConnection()) {
            loadInterstitial();
        } else {
            launchWithDelay();
        }
    }

    private void launchWithDelay() {
        runnable = new Runnable() {
            @Override
            public void run() {
                launchLanguageActivity();
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }

    private void launchLanguageActivity() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final Intent intent;
                if (!myPreferences.isLanguageSelected()) {
                    intent = new Intent(ActivitySplash.this, ActivityLanguage.class);
                } else {
                    if (myPreferences.isFirstTimeLaunch()) {
                        intent = new Intent(ActivitySplash.this, ActivityIntroSLides.class);
                    } else {
                        if (myPreferences.isPrivacyPolicyAccepted()) {
                            intent = new Intent(ActivitySplash.this, MainActivity.class);
                        } else {
                            intent = new Intent(ActivitySplash.this, ActivityPrivacyPolicy.class);
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                });

                return null;
            }
        }.execute();

    }

    void requestInterstitial() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(this.getResources().getString(R.string.interstitial_Id));
        AdRequest adRequestInter = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequestInter);
    }

    void loadInterstitial() {
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    launchWithDelay();
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                loadAttempts++;
                if (loadAttempts >= 2) {
                    loadAttempts = 0;
                    launchLanguageActivity();
                } else {
                    requestInterstitial();
                    loadInterstitial();
                }
                super.onAdFailedToLoad(i);
            }


            @Override
            public void onAdClosed() {

                launchLanguageActivity();
            }


            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }
        });
    }


   /* @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }*/
}