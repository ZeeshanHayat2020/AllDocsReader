package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.furestic.alldocument.office.ppt.lxs.docx.pdf.viwer.reader.free.R;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.database.MyPreferences;

public class ActivityPrivacyPolicy extends AppCompatActivity {

    private TextView tvPrivacyPolicy;
    private TextView tvPrivacyPolicyLinkView;

    private Button btnAccept;
    private Button bntDecline;
    private MyPreferences myPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this);
        setContentView(R.layout.activity_privacy_policy);
        myPreferences = new MyPreferences(this);
        initViews();
        setPrivacyPolicyText();
    }

    public static void setStatusBarGradient(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.ic_main_bg);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    private void initViews() {
        tvPrivacyPolicy = (TextView) findViewById(R.id.acPrivacyPolicy_textView);
        tvPrivacyPolicyLinkView= (TextView) findViewById(R.id.acPrivacyPolicy_linkView);
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvPrivacyPolicy.setText(Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_LEGACY));
            tvPrivacyPolicyLinkView.setText(Html.fromHtml(getString(R.string.privacy_policy_linkText), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvPrivacyPolicy.setText(Html.fromHtml(getString(R.string.privacy_policy_text)));
            tvPrivacyPolicyLinkView.setText(Html.fromHtml(getString(R.string.privacy_policy_linkText)));
        }
        tvPrivacyPolicyLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.pp_url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

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