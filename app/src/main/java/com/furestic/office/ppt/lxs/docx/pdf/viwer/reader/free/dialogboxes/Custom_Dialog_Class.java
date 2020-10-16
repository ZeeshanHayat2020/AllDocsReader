package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.dialogboxes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;


import com.furestic.alldocument.office.ppt.lxs.docx.pdf.viwer.reader.free.R;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Custom_Dialog_Class extends Dialog implements
        View.OnClickListener {


    public Activity activity;
    public Dialog d;
    public Button yes, no;
    SmileRating smileRating;

    public Custom_Dialog_Class(Activity a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_box);
        yes = findViewById(R.id.btn_may_be_later);
        no = findViewById(R.id.btn_never);
        smileRating = findViewById(R.id.ratingView);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);


        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                switch (smiley) {
                    case SmileRating.BAD:
                        dismiss();
                        Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        Log.i(TAG, "Okay");
                        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        activity.startActivity(goToMarket);
                        Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        Log.i(TAG, "Okay");
                        Uri uri1 = Uri.parse("market://details?id=" + activity.getPackageName());
                        Intent goToMarket1 = new Intent(Intent.ACTION_VIEW, uri1);
                        goToMarket1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        activity.startActivity(goToMarket1);
                        Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        dismiss();
                        Log.i(TAG, "Great");
                        break;
                    case SmileRating.TERRIBLE:
                        dismiss();
                        Log.i(TAG, "Terrible");
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_may_be_later:
                dismiss();
                break;
            case R.id.btn_never:
                activity.finish();
                activity.finishAffinity();
                System.exit(0);
                break;
            default:
                break;
        }
        dismiss();
    }
}