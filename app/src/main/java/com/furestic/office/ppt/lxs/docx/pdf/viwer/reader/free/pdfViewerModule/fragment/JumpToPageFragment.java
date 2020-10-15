package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.pdfViewerModule.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.pdfViewerModule.PdfViewer;

public class JumpToPageFragment extends DialogFragment {
    public static final String TAG = "JumpToPageFragment";

    private final static String STATE_PICKER_CUR = "picker_cur";
    private final static String STATE_PICKER_MIN = "picker_min";
    private final static String STATE_PICKER_MAX = "picker_max";

    private NumberPicker mPicker;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mPicker.setMinValue(savedInstanceState.getInt(STATE_PICKER_MIN));
            mPicker.setMaxValue(savedInstanceState.getInt(STATE_PICKER_MAX));
            mPicker.setValue(savedInstanceState.getInt(STATE_PICKER_CUR));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mPicker = new NumberPicker(getActivity());
        mPicker.setMinValue(1);
        mPicker.setMaxValue(((PdfViewer)getActivity()).mNumPages);
        mPicker.setValue(((PdfViewer)getActivity()).mPage);

        final FrameLayout layout = new FrameLayout(getActivity());
        layout.addView(mPicker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));

        return new AlertDialog.Builder(getActivity())
                .setView(layout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPicker.clearFocus();
                        ((PdfViewer)getActivity()).onJumpToPageInDocument(mPicker.getValue());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_PICKER_MIN, mPicker.getMinValue());
        outState.putInt(STATE_PICKER_MAX, mPicker.getMaxValue());
        outState.putInt(STATE_PICKER_CUR, mPicker.getValue());
    }
}
