package com.example.alldocumentreader.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.example.alldocumentreader.R;
import com.example.alldocumentreader.customviews.TouchImageView;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PdfViewPager2Adapter extends RecyclerView.Adapter<PdfViewPager2Adapter.ViewHolder> {

    private ArrayList<Bitmap> bitmapsList;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager2;
    public PdfViewPager2Adapter(Context context, ArrayList<Bitmap> bitmapsList, ViewPager2 viewPager2) {
        this.mInflater = LayoutInflater.from(context);
        this.bitmapsList = bitmapsList;
        this.viewPager2 = viewPager2;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_view_view_pager2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(bitmapsList.get(position));
        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (holder.imageView.isZoomed()){
                    holder.itemView.getParent().requestDisallowInterceptTouchEvent(true);
                    Log.d(TAG, "onTouch: parent should be disable");
                }else {
                    holder.itemView.getParent().requestDisallowInterceptTouchEvent(false);
                    Log.d(TAG, "onTouch: parent should be enable");
                }
                return false;
            }
        });
       /* holder.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        Toast.makeText(view.getContext(), "Touched", Toast.LENGTH_SHORT).show();

                    }break;
                }
                return false;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return bitmapsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TouchImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_view_viewpager_iv);

        }

    }
}
