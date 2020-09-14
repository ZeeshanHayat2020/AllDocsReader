package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemsDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public RecyclerViewItemsDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = (int) (space/1.2);

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
//            outRect.top = space;
            outRect.top=0;
        } else {
            outRect.top = 0;
        }
    }
}