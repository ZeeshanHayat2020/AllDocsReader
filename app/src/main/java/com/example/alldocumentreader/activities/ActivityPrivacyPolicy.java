package com.example.alldocumentreader.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alldocumentreader.R;
import com.example.alldocumentreader.database.MyPreferences;

public class ActivityPrivacyPolicy extends AppCompatActivity {

    private TextView tvPrivacyPolicy;
    private Button btnAccept;
    private Button bntDecline;
    private MyPreferences myPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_privacy_policy);
        myPreferences = new MyPreferences(this);
        initViews();
        setPrivacyPolicyText();


    }

    private void initViews() {
        tvPrivacyPolicy = (TextView) findViewById(R.id.acPrivacyPolicy_textView);
        btnAccept = (Button) findViewById(R.id.acPP_btnAccept);
        bntDecline = (Button) findViewById(R.id.acPP_btnDecline);
        btnAccept.setOnClickListener(onClickListener);
        bntDecline.setOnClickListener(onClickListener);
        btnAccept.setBackground(getGradient(
                this.getResources().getColor(R.color.color_cardBg_pdfDoc_upper),
                this.getResources().getColor(R.color.color_cardBg_pdfDoc_lower)));
        bntDecline.setBackground(getGradient(
                this.getResources().getColor(R.color.color_cardBg_txtDoc_upper),
                this.getResources().getColor(R.color.color_cardBg_txtDoc_lower)));
    }

    private void setPrivacyPolicyText() {
        tvPrivacyPolicy.setText(Html.fromHtml(getString(R.string.privacy_policy_text)));
    }

    private GradientDrawable getGradient(int color1, int color2) {
        int[] colors = {Integer.parseInt(String.valueOf(color1)),
                Integer.parseInt(String.valueOf(color2))
        };
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                colors);
        float radius = getResources().getDimension(R.dimen._6sdp);
        gd.setCornerRadius(radius);
        return gd;
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    private void setAcceptance(boolean isAccept) {
        myPreferences.setPrivacyPolicyAcceptance(isAccept);
        if (isAccept) {
            startMainActivity();
        } else {
            this.finish();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.acPP_btnAccept: {
                    setAcceptance(true);
                }
                break;
                case R.id.acPP_btnDecline: {
                    setAcceptance(false);
                }
                break;
            }
        }
    };


}