package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.interfaces;

import android.view.View;

public interface OnRecyclerItemClickLister {
    void onItemClicked(int position);

    void onItemShareClicked(int position);

    void onItemDeleteClicked(int position);
    void onItemRenameClicked(int position);

    void onItemLongClicked(int position);

    void onItemCheckBoxClicked(View view, int position);

}
