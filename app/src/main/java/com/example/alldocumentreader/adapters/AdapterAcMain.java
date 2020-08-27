package com.example.alldocumentreader.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alldocumentreader.R;
import com.example.alldocumentreader.interfaces.OnRecyclerItemClickLister;
import com.example.alldocumentreader.models.ModelAcMain;

import java.util.ArrayList;

public class AdapterAcMain extends RecyclerView.Adapter<AdapterAcMain.AdapterViewHolder> {
    private Context context;
    private ArrayList<ModelAcMain> modelAcMainList;
    private OnRecyclerItemClickLister onRecyclerItemClickLister;


    public AdapterAcMain(Context context,
                         ArrayList<ModelAcMain> modelAcMainList) {
        this.context = context;
        this.modelAcMainList = modelAcMainList;
    }

    public void setOnItemClickListener(OnRecyclerItemClickLister onRecyclerItemClickLister) {
        this.onRecyclerItemClickLister = onRecyclerItemClickLister;
    }


    @Override
    public int getItemCount() {
        return modelAcMainList.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView itemName_tv;

        public AdapterViewHolder(@NonNull View itemView, final OnRecyclerItemClickLister onRecyclerItemClickLister) {
            super(itemView);
            itemName_tv = itemView.findViewById(R.id.itemView_acMain_ItemName_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onRecyclerItemClickLister != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onRecyclerItemClickLister.onItemClicked(position);
                        }
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_ac_main, parent, false);
        return new AdapterViewHolder(view, onRecyclerItemClickLister);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        final ModelAcMain currentItem = modelAcMainList.get(position);
        holder.itemName_tv.setText(currentItem.getItemName());
    }


    private void fadeInItemAnimation(View view) {
        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1);
        fadeAnimator.setInterpolator(new LinearInterpolator());
        fadeAnimator.setDuration(500);

        ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(view, "translationY", 10f, 0);
        translateAnimator.setInterpolator(new LinearInterpolator());
        translateAnimator.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(fadeAnimator, translateAnimator);
        set.start();
    }


    public GradientDrawable setCardGradient(int color1, int color2) {
        int[] colors = {Integer.parseInt(String.valueOf(color1)),
                Integer.parseInt(String.valueOf(color2))
        };
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                colors);
        return gd;
    }
}