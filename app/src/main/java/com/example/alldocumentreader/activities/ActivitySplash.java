package com.example.alldocumentreader.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.alldocumentreader.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class ActivitySplash extends ActivityBase {

    private Handler handler;
    private Runnable runnable;
    private InterstitialAd interstitialAd;
    private int loadAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        requestInterstitial();
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
        startActivity(new Intent(ActivitySplash.this, ActivityLanguage.class));
        this.finish();
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
                if (loadAttempts > 2) {
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


    @Override
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
    }
}