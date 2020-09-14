/*
 * 文件名称:          SheetButton.java
 *
 * 编译器:            android2.2
 * 时间:              下午6:06:50
 */
package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.ss.sheetbar;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.furestic.alldocument.office.ppt.lxs.docx.pdf.viwer.reader.free.R;

/**
 * sheet表名称按钮
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-6
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public class SheetButton extends LinearLayout {
    private static final int FONT_SIZE = 18;
    //
    private static final int SHEET_BUTTON_MIN_WIDTH = 100;

    /**
     * @param context
     */
    public SheetButton(Context context, String sheetName, int sheetIndex, SheetbarResManager sheetbarResManager) {
        super(context);
        setOrientation(HORIZONTAL);
        this.sheetIndex = sheetIndex;
        this.sheetbarResManager = sheetbarResManager;

        init(context, sheetName);
    }

    /**
     *
     */
    private void init(Context context, String sheetName) {
        //左边图标
        left = new View(context);

        // left.setBackgroundDrawable(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_LEFT));
        left.setBackgroundDrawable(getResources().getDrawable(R.drawable.ss_sheetbar_button_normal_left));
        addView(left);

        // 
        textView = new TextView(context);
        //textView.setBackgroundDrawable(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_MIDDLE));
        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ss_sheetbar_button_normal_middle));
        textView.setText(sheetName);
        textView.setTextSize(FONT_SIZE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        int w = (int) textView.getPaint().measureText(sheetName);
        w = Math.max(w, SHEET_BUTTON_MIN_WIDTH);
        addView(textView, new LayoutParams(w, LayoutParams.MATCH_PARENT));

        // 右边图标
        right = new View(context);
        //  right.setBackgroundDrawable(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_RIGHT));
        right.setBackgroundDrawable(getResources().getDrawable(R.drawable.ss_sheetbar_button_normal_right));
        addView(right);
    }

    /**
     *
     */
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (!active) {
                    // left.setBackgroundDrawable(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_PUSH_LEFT));
                    left.setBackgroundDrawable(getResources().getDrawable(R.drawable.ss_sheetbar_button_push_left));
                    //textView.setBackgroundDrawable(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_PUSH_MIDDLE));
                    textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ss_sheetbar_button_push_middle));
                    //right.setBackgroundDrawable(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_PUSH_RIGHT));
                    right.setBackgroundDrawable(getResources().getDrawable(R.drawable.ss_sheetbar_button_push_right));
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!active) {
                    // left.setBackgroundDrawable(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_LEFT));
                    left.setBackgroundDrawable(getResources().getDrawable(R.drawable.ss_sheetbar_button_normal_left));
                    // textView.setBackgroundDrawable(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_MIDDLE));
                    textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ss_sheetbar_button_normal_middle));
                    //right.setBackgroundDrawable(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_RIGHT));
                    right.setBackgroundDrawable(getResources().getDrawable(R.drawable.ss_sheetbar_button_normal_right));
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 选中或取消选中用到
     */
    public void changeFocus(boolean gainFocus) {
        active = gainFocus;
        
       /* left.setBackgroundDrawable(gainFocus ? sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_FOCUS_LEFT) :
            sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_LEFT));*/

        left.setBackgroundDrawable(gainFocus ? getResources().getDrawable(R.drawable.ss_sheetbar_button_focus_left) :
                getResources().getDrawable(R.drawable.ss_sheetbar_button_normal_left));

     /*   textView.setBackgroundDrawable(gainFocus ? sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_FOCUS_MIDDLE) :
            sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_MIDDLE));  */

        textView.setBackgroundDrawable(gainFocus ? getResources().getDrawable(R.drawable.ss_sheetbar_button_focus_middle) :
                getResources().getDrawable(R.drawable.ss_sheetbar_button_normal_middle));
        /*
        right.setBackgroundDrawable(gainFocus ? sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_FOCUS_RIGHT) :
            sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_RIGHT));*/

        right.setBackgroundDrawable(gainFocus ? getResources().getDrawable(R.drawable.ss_sheetbar_button_focus_right) :
                getResources().getDrawable(R.drawable.ss_sheetbar_button_normal_right));
    }

    /**
     *
     */
    public int getSheetIndex() {
        return this.sheetIndex;
    }

    /**
     *
     */
    public void dispose() {
        sheetbarResManager = null;

        left = null;
        textView = null;
        right = null;
    }

    private SheetbarResManager sheetbarResManager;
    //
    private int sheetIndex;
    ;
    // 左边图标
    private View left;
    // 中间文本
    private TextView textView;
    // 右边图标
    private View right;

    private boolean active;
}
