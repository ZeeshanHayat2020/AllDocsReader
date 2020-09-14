package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.furestic.alldocument.office.ppt.lxs.docx.pdf.viwer.reader.free.R;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.database.MyPreferences;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.utils.LanguageManager;

public class ActivityLanguage extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView toolBarTitleTv;
    private Button bntEng, btnArabic, btnChina,
            btnFrench, btnGerman, btnIndonesian, btnItalian,
            btnPolish, btnPortuguese, btnRussian, btnSpanish;
    private MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorLanguageToolBar));
        }
        setContentView(R.layout.activity_language);
        myPreferences = new MyPreferences(this);
        iniViews();
        setUpToolBar();

    }

    private void iniViews() {
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.acLanguage_toolbar);
        bntEng = (Button) findViewById(R.id.acLanguage_btnEnglish);
        btnArabic = (Button) findViewById(R.id.acLanguage_btnArabic);
        btnChina = (Button) findViewById(R.id.acLanguage_btnChina);
        btnFrench = (Button) findViewById(R.id.acLanguage_btnFrance);
        btnGerman = (Button) findViewById(R.id.acLanguage_btnGerman);
        btnIndonesian = (Button) findViewById(R.id.acLanguage_btnIndonesian);
        btnItalian = (Button) findViewById(R.id.acLanguage_btnItalian);
        btnPolish = (Button) findViewById(R.id.acLanguage_btnPolish);
        btnPortuguese = (Button) findViewById(R.id.acLanguage_btnPortuguese);
        btnRussian = (Button) findViewById(R.id.acLanguage_btnRussian);
        btnSpanish = (Button) findViewById(R.id.acLanguage_btnSpanish);


        bntEng.setOnClickListener(this);
        btnArabic.setOnClickListener(this);
        btnFrench.setOnClickListener(this);
        btnChina.setOnClickListener(this);

        btnArabic.setOnClickListener(this);
        btnGerman.setOnClickListener(this);
        btnGerman.setOnClickListener(this);
        btnIndonesian.setOnClickListener(this);
        btnItalian.setOnClickListener(this);
        btnPolish.setOnClickListener(this);
        btnPortuguese.setOnClickListener(this);
        btnRussian.setOnClickListener(this);
        btnSpanish.setOnClickListener(this);

    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        toolBarTitleTv = (TextView) findViewById(R.id.toolBar_title_tv);
        toolBarTitleTv.setText("LANGUAGE");
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorLanguageToolBar));
    }

    private void setLanguage(boolean isSelected) {
        myPreferences.setLanguageSelected(isSelected);
        startIntroSlidesActivity();
    }

    private void setNewLocal(String languageKey) {
        LanguageManager.setNewLocale(this, languageKey);
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
                setNewLocal(LanguageManager.LANGUAGE_KEY_ENGLISH);
            }
            break;
            case R.id.acLanguage_btnArabic: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_ARABIC);
            }
            break;
            case R.id.acLanguage_btnChina: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_CHINES);
            }
            break;
            case R.id.acLanguage_btnFrance: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_FRENCH);
            }
            break;
            case R.id.acLanguage_btnGerman: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_GERMAN);
            }
            break;
            case R.id.acLanguage_btnIndonesian: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_INDONESIAN);
            }
            break;
            case R.id.acLanguage_btnItalian: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_ITALIAN);
            }
            break;
            case R.id.acLanguage_btnPolish: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_POLISH);
            }
            break;
            case R.id.acLanguage_btnPortuguese: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_PORTUGUESE);
            }
            break;
            case R.id.acLanguage_btnRussian: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_RUSSIA);
            }
            break;

            case R.id.acLanguage_btnSpanish: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_SPANISH);
            }
            break;
        }
    }
}