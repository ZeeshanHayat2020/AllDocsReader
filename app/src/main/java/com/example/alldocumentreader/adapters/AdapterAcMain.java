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
import androidx.cardview.widget.CardView;
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
        RelativeLayout itemIVRoot;
        ImageView itemImageView;
        TextView itemName_tv;

        public AdapterViewHolder(@NonNull View itemView, final OnRecyclerItemClickLister onRecyclerItemClickLister) {
            super(itemView);
            itemIVRoot = itemView.findViewById(R.id.itemView_acMain_ivRoot);
            itemImageView = itemView.findViewById(R.id.itemView_acMain_imageView);
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
        holder.itemImageView.setImageResource(currentItem.getItemImageId());

        String checkFileFormat=currentItem.getItemName();
        if (checkFileFormat.equals(context.getString(R.string.allDocs))) {
            holder.itemIVRoot
                    .setBackground(setCardGradient(
                            context.getResources().getColor(R.color.color_cardBg_allDoc_upper),
                            context.getResources().getColor(R.color.color_cardBg_allDoc_lower)));
        } else if (checkFileFormat.equals(context.getString(R.string.pdf_files))) {
            holder.itemIVRoot
                    .setBackground(setCardGradient(
                            context.getResources().getColor(R.color.color_cardBg_pdfDoc_upper),
                            context.getResources().getColor(R.color.color_cardBg_pdfDoc_lower)));
        } else if (checkFileFormat.equals(context.getString(R.string.word_files))) {
            holder.itemIVRoot
                    .setBackground(setCardGradient(
                            context.getResources().getColor(R.color.color_cardBg_wordDoc_upper),
                            context.getResources().getColor(R.color.color_cardBg_wordDoc_lower)));

        } else if (checkFileFormat.equals(context.getString(R.string.txtFiles))) {
            holder.itemIVRoot
                    .setBackground(setCardGradient(
                            context.getResources().getColor(R.color.color_cardBg_txtDoc_upper),
                            context.getResources().getColor(R.color.color_cardBg_txtDoc_lower)));

        } else if (checkFileFormat.equals(context.getString(R.string.ppt_files))) {
            holder.itemIVRoot
                    .setBackground(setCardGradient(
                            context.getResources().getColor(R.color.color_cardBg_pptDoc_upper),
                            context.getResources().getColor(R.color.color_cardBg_pptDoc_lower)));
        } else if (checkFileFormat.equals(context.getString(R.string.html_files))) {
            holder.itemIVRoot
                    .setBackground(setCardGradient(
                            context.getResources().getColor(R.color.color_cardBg_htmlDoc_upper),
                            context.getResources().getColor(R.color.color_cardBg_htmlDoc_lower)));

        } else if (checkFileFormat.equals(context.getString(R.string.xmlFiles))) {
            holder.itemIVRoot
                    .setBackground(setCardGradient(
                            context.getResources().getColor(R.color.color_cardBg_xmlDoc_upper),
                            context.getResources().getColor(R.color.color_cardBg_xmlDoc_lower)));

        } else if (checkFileFormat.equals(context.getString(R.string.sheet_files))) {
            holder.itemIVRoot
                    .setBackground(setCardGradient(
                            context.getResources().getColor(R.color.color_cardBg_sheetDoc_upper),
                            context.getResources().getColor(R.color.color_cardBg_sheetDoc_lower)));
        } else {
            holder.itemIVRoot
                    .setBackground(setCardGradient(
                            context.getResources().getColor(R.color.color_cardBg_allDoc_upper),
                            context.getResources().getColor(R.color.color_cardBg_allDoc_lower)));
        }


/*
        switch (currentItem.getItemName()) {
            case "All Documents": {
                holder.itemIVRoot
                        .setBackground(setCardGradient(
                                context.getResources().getColor(R.color.color_cardBg_allDoc_upper),
                                context.getResources().getColor(R.color.color_cardBg_allDoc_lower)));
            }
            break;
            case "PDF Files": {
                holder.itemIVRoot
                        .setBackground(setCardGradient(
                                context.getResources().getColor(R.color.color_cardBg_pdfDoc_upper),
                                context.getResources().getColor(R.color.color_cardBg_pdfDoc_lower)));
            }
            break;
            case "Word Files": {
                holder.itemIVRoot
                        .setBackground(setCardGradient(
                                context.getResources().getColor(R.color.color_cardBg_wordDoc_upper),
                                context.getResources().getColor(R.color.color_cardBg_wordDoc_lower)));
            }
            break;
            case "Text Files": {
                holder.itemIVRoot
                        .setBackground(setCardGradient(
                                context.getResources().getColor(R.color.color_cardBg_txtDoc_upper),
                                context.getResources().getColor(R.color.color_cardBg_txtDoc_lower)));
            }
            break;
            case "PPT Files": {
                holder.itemIVRoot
                        .setBackground(setCardGradient(
                                context.getResources().getColor(R.color.color_cardBg_pptDoc_upper),
                                context.getResources().getColor(R.color.color_cardBg_pptDoc_lower)));
            }
            break;
            case "HTML Files": {
                holder.itemIVRoot
                        .setBackground(setCardGradient(
                                context.getResources().getColor(R.color.color_cardBg_htmlDoc_upper),
                                context.getResources().getColor(R.color.color_cardBg_htmlDoc_lower)));
            }
            break;
            case "XML Files": {
                holder.itemIVRoot
                        .setBackground(setCardGradient(
                                context.getResources().getColor(R.color.color_cardBg_xmlDoc_upper),
                                context.getResources().getColor(R.color.color_cardBg_xmlDoc_lower)));
            }
            break;
            case "Sheet Files": {
                holder.itemIVRoot
                        .setBackground(setCardGradient(
                                context.getResources().getColor(R.color.color_cardBg_sheetDoc_upper),
                                context.getResources().getColor(R.color.color_cardBg_sheetDoc_lower)));
            }
            break;


        }*/
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