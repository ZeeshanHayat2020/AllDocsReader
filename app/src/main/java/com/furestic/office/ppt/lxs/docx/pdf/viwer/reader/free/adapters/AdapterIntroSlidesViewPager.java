package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

public class AdapterIntroSlidesViewPager extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    int[] layoutsList;


    public AdapterIntroSlidesViewPager(Context context, int[] layoutsList) {
        this.context = context;
        this.layoutsList = layoutsList;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(layoutsList[position], container, false);
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return layoutsList.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
