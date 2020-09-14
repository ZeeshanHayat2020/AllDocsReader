package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.furestic.alldocument.office.ppt.lxs.docx.pdf.viwer.reader.free.R;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.database.MyPreferences;
import com.google.android.gms.ads.AdListener;

public class ActivitySplash extends ActivityBase {

    private Handler handler;
    private Runnable runnable;
    private int loadAttempts;
    private MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setStatusBarGradient(this);
        myPreferences = new MyPreferences(this);
        reqNewInterstitial(this);

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
                startActivity(intent);
                finish();

                return null;
            }
        }.execute();

    }


    void loadInterstitial() {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded() && !myPreferences.isItemPurchased()) {
                    mInterstitialAd.show();
                } else {
                    launchWithDelay();
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                loadAttempts++;
                if (loadAttempts >= 1) {
                    loadAttempts = 0;
                    launchLanguageActivity();
                } else {
                    reqNewInterstitial(ActivitySplash.this);
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

}