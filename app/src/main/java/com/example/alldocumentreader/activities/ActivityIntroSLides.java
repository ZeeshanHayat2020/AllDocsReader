package com.example.alldocumentreader.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alldocumentreader.R;
import com.example.alldocumentreader.adapters.AdapterIntroSlidesViewPager;
import com.example.alldocumentreader.database.MyPreferences;

public class ActivityIntroSLides extends AppCompatActivity {


    private PreferenceManager preferenceManager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] screensList;
    private Button btnSkip, btnNext;
    private ViewPager viewPager;
    private AdapterIntroSlidesViewPager pagerAdapter;
    private MyPreferences myPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_s_lides);
        myPreferences = new MyPreferences(this);
        initViews();
        setUpViewPager();
        addBottomDots(0);


    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.acIntroSlide_viewPager);
        dotsLayout = (LinearLayout) findViewById(R.id.acIntroSlides_barsRoot);
        btnSkip = (Button) findViewById(R.id.acIntroSlides_btnSkip);
        btnNext = (Button) findViewById(R.id.acIntroSlides_btnNext);
        viewPager.setOnPageChangeListener(viewPagerPageChangeListener);
        btnNext.setOnClickListener(onButtonsClicked);
        btnSkip.setOnClickListener(onButtonsClicked);
    }

    private void setUpViewPager() {
        screensList = new int[]{
                R.layout.layout_introslide_one,
                R.layout.layout_introslide_two,
                R.layout.layout_introslide_three
        };
        pagerAdapter = new AdapterIntroSlidesViewPager(this, screensList);
        viewPager.setAdapter(pagerAdapter);
    }

    private void launchPrivacyPolicyScreen(boolean isFirstLaunch) {
        myPreferences.setFirstTimeLaunch(isFirstLaunch);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Intent intent;
                if (myPreferences.isPrivacyPolicyAccepted()) {
                    intent = new Intent(ActivityIntroSLides.this, MainActivity.class);
                } else {
                    intent = new Intent(ActivityIntroSLides.this, ActivityPrivacyPolicy.class);
                }
                startActivity(intent);
                finish();
                return null;
            }
        }.execute();

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[screensList.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("¯"));
            dots[i].setTextSize(40);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    View.OnClickListener onButtonsClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.acIntroSlides_btnNext: {
                    int current = getItem(+1);
                    if (current < screensList.length) {
                        viewPager.setCurrentItem(current);
                    } else {
                        launchPrivacyPolicyScreen(false);
                    }

                }
                break;
                case R.id.acIntroSlides_btnSkip: {

                    launchPrivacyPolicyScreen(true);
                }
                break;
            }
        }
    };

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == screensList.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


}