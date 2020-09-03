package com.example.alldocumentreader.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alldocumentreader.R;
import com.example.alldocumentreader.database.MyPreferences;

public class ActivityLanguage extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView toolBarTitleTv;
    private Button bntEng, btnUrdu, btnArabic, btnHindi, btnFrance, btnMalaysia, btnChina;
    private MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language);
        myPreferences = new MyPreferences(this);
        iniViews();
        setUpToolBar();

    }

    private void iniViews() {
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.acLanguage_toolbar);
        bntEng = (Button) findViewById(R.id.acLanguage_btnEnglish);
        btnUrdu = (Button) findViewById(R.id.acLanguage_btnUrdu);
        btnArabic = (Button) findViewById(R.id.acLanguage_btnArabic);
        btnHindi = (Button) findViewById(R.id.acLanguage_btnHindi);
        btnFrance = (Button) findViewById(R.id.acLanguage_btnFrance);
        btnMalaysia = (Button) findViewById(R.id.acLanguage_btnMalaysia);
        btnChina = (Button) findViewById(R.id.acLanguage_btnChina);
        bntEng.setOnClickListener(this);
        btnUrdu.setOnClickListener(this);
        btnArabic.setOnClickListener(this);
        btnHindi.setOnClickListener(this);
        btnFrance.setOnClickListener(this);
        btnMalaysia.setOnClickListener(this);
        btnChina.setOnClickListener(this);

    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);

        toolBarTitleTv = (TextView) findViewById(R.id.toolBar_title_tv);
        toolBarTitleTv.setText("LANGUAGE");
        toolBarTitleTv.setGravity(Gravity.CENTER_HORIZONTAL);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorLanguageToolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setLanguage(boolean isSelected) {
        myPreferences.setLanguageSelected(isSelected);
        startIntroSlidesActivity();
    }

    private void startIntroSlidesActivity() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Intent intent;
                if (myPreferences.isFirstTimeLaunch()) {
                    intent = new Intent(ActivityLanguage.this, ActivityIntroSLides.class);
                } else {
                    if (myPreferences.isPrivacyPolicyAccepted()) {
                        intent = new Intent(ActivityLanguage.this, MainActivity.class);
                    } else {
                        intent = new Intent(ActivityLanguage.this, ActivityPrivacyPolicy.class);
                    }
                }
                startActivity(intent);
                finish();
                return null;
            }
        }.execute();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.acLanguage_btnEnglish: {

                setLanguage(true);
            }
            break;
            case R.id.acLanguage_btnUrdu: {
                setLanguage(true);
            }
            break;
            case R.id.acLanguage_btnArabic: {
                setLanguage(true);
            }

            break;
            case R.id.acLanguage_btnHindi: {
                setLanguage(true);
            }
            break;
            case R.id.acLanguage_btnFrance: {
                setLanguage(true);
            }
            break;
            case R.id.acLanguage_btnMalaysia: {
                setLanguage(true);
            }
            break;
            case R.id.acLanguage_btnChina: {
                setLanguage(true);
            }
            break;
        }
    }
}