package com.example.alldocumentreader.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alldocumentreader.R;
import com.example.alldocumentreader.activities.ActivityFilesHolder;
import com.example.alldocumentreader.interfaces.OnRecyclerItemClickLister;
import com.example.alldocumentreader.models.ModelAcMain;
import com.example.alldocumentreader.models.ModelFilesHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterFilesHolder extends RecyclerView.Adapter<AdapterFilesHolder.AdapterViewHolder> implements Filterable {
    private Context context;
    private ActivityFilesHolder activityFilesHolder;
    private ArrayList<ModelFilesHolder> itemsList;
    private ArrayList<ModelFilesHolder> tempList;
    private OnRecyclerItemClickLister onRecyclerItemClickLister;


    public AdapterFilesHolder(Context context, ActivityFilesHolder activityFilesHolder,
                              ArrayList<ModelFilesHolder> itemsList) {
        this.context = context;
        this.activityFilesHolder = activityFilesHolder;
        this.itemsList = itemsList;
        this.tempList = itemsList;
    }

    public void setOnItemClickListener(OnRecyclerItemClickLister onRecyclerItemClickLister) {
        this.onRecyclerItemClickLister = onRecyclerItemClickLister;
    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView btnMenu;
        CheckBox checkBox;
        TextView fileNameTv;
        TextView fileSizeTv;

        public AdapterViewHolder(@NonNull View itemView, final OnRecyclerItemClickLister onRecyclerItemClickLister) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemView_acFilesHolder_Iv);
            btnMenu = itemView.findViewById(R.id.itemView_acFilesHolder_btnMenu);
            checkBox = itemView.findViewById(R.id.itemView_acFilesHolder_checkBox);
            fileNameTv = itemView.findViewById(R.id.itemView_acFilesHolder_FileNameTV);
            fileSizeTv = itemView.findViewById(R.id.itemView_acFilesHolder_FileSizeTV);


        }
    }

    @NonNull
    @Override
    public AdapterFilesHolder.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_ac_filesholder, parent, false);
        return new AdapterFilesHolder.AdapterViewHolder(view, onRecyclerItemClickLister);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterFilesHolder.AdapterViewHolder holder, final int position) {
        final ModelFilesHolder currentItem = itemsList.get(position);
        holder.fileNameTv.setText(currentItem.getFileName());
        holder.fileSizeTv.setText(getFileSize(new File(currentItem.getFileUri())));
        String fileExtension = getMimeType(context, currentItem.getFileUri());
        switch (fileExtension) {
            case "pdf": {
                holder.imageView.setImageResource(R.drawable.pdf);
                setCheckBoxTintColor(holder.checkBox, context.getResources().getColor(R.color.color_cardBg_pdfDoc_upper));
            }
            break;
            case "txt": {
                holder.imageView.setImageResource(R.drawable.text);
                setCheckBoxTintColor(holder.checkBox, context.getResources().getColor(R.color.color_cardBg_txtDoc_upper));
            }
            break;
            case "xls": {
                holder.imageView.setImageResource(R.drawable.sheet);
                setCheckBoxTintColor(holder.checkBox, context.getResources().getColor(R.color.color_cardBg_sheetDoc_upper));
            }
            break;
            case "xlsx": {
                holder.imageView.setImageResource(R.drawable.sheet);
                setCheckBoxTintColor(holder.checkBox, context.getResources().getColor(R.color.color_cardBg_sheetDoc_upper));
            }
            break;
            case "doc": {
                holder.imageView.setImageResource(R.drawable.word);
                setCheckBoxTintColor(holder.checkBox, context.getResources().getColor(R.color.color_cardBg_wordDoc_upper));
            }
            break;
            case "docx": {
                holder.imageView.setImageResource(R.drawable.word);
                setCheckBoxTintColor(holder.checkBox, context.getResources().getColor(R.color.color_cardBg_wordDoc_upper));
            }
            break;
            case "html": {
                holder.imageView.setImageResource(R.drawable.html);
                setCheckBoxTintColor(holder.checkBox, context.getResources().getColor(R.color.color_cardBg_htmlDoc_upper));
            }
            break;
            case "xml": {
                holder.imageView.setImageResource(R.drawable.xml);
                setCheckBoxTintColor(holder.checkBox, context.getResources().getColor(R.color.color_cardBg_xmlDoc_upper));
            }
            break;
            case "ppt": {
                holder.imageView.setImageResource(R.drawable.ppt);
                setCheckBoxTintColor(holder.checkBox, context.getResources().getColor(R.color.color_cardBg_pptDoc_upper));
            }
            break;
            case "pptx": {
                holder.imageView.setImageResource(R.drawable.ppt);
                setCheckBoxTintColor(holder.checkBox, context.getResources().getColor(R.color.color_cardBg_pptDoc_upper));
            }
            break;
            default: {
                holder.imageView.setImageResource(R.drawable.all);
                setCheckBoxTintColor(holder.checkBox, context.getResources().getColor(R.color.colorAccent));
            }

        }

        if (activityFilesHolder.isContextualMenuOpen) {
            if (activityFilesHolder.isSelectAll) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
            holder.btnMenu.setVisibility(View.INVISIBLE);
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.btnMenu.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecyclerItemClickLister != null) {
                    if (!activityFilesHolder.isContextualMenuOpen) {
                        if (position != RecyclerView.NO_POSITION) {
                            onRecyclerItemClickLister.onItemClicked(position);
                        }
                    } else {
                        holder.checkBox.performClick();
                    }
                }
            }
        });
        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecyclerItemClickLister != null) {
                    if (position != RecyclerView.NO_POSITION) {
                        setItemViewMenu(view.getContext(), view, position);
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onRecyclerItemClickLister != null) {
                    if (position != RecyclerView.NO_POSITION) {
                        onRecyclerItemClickLister.onItemLongClicked(position);
                    }
                }
                return false;
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecyclerItemClickLister != null) {
                    if (position != RecyclerView.NO_POSITION) {
                        onRecyclerItemClickLister.onItemCheckBoxClicked(view, position);
                    }
                }
            }
        });

    }

    private void setCheckBoxTintColor(CheckBox btn, int tintColor) {
        if (Build.VERSION.SDK_INT < 21) {
            CompoundButtonCompat.setButtonTintList(btn, ColorStateList.valueOf(tintColor));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
        } else {
            btn.setButtonTintList(ColorStateList.valueOf(tintColor));//setButtonTintList is accessible directly on API>19
        }
    }

    public static String getMimeType(Context context, String uri) {
        String extension;
        //If scheme is a File
        //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
        extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri)).toString());
        return extension;
    }

    private void setItemViewMenu(Context context, View view, final int position) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.inflate(R.menu.menu_ac_filesholder_item_views);
        popup.setGravity(Gravity.RIGHT);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_filesHolder_itemView_rename: {
                        if (onRecyclerItemClickLister != null) {
                            if (position != RecyclerView.NO_POSITION) {
                                onRecyclerItemClickLister.onItemRenameClicked(position);
                            }
                        }
                    }
                    break;
                    case R.id.menu_filesHolder_itemView_share: {
                        if (onRecyclerItemClickLister != null) {
                            if (position != RecyclerView.NO_POSITION) {
                                onRecyclerItemClickLister.onItemShareClicked(position);
                            }
                        }
                    }
                    break;
                    case R.id.menu_filesHolder_itemView_delete: {
                        if (onRecyclerItemClickLister != null) {
                            if (position != RecyclerView.NO_POSITION) {
                                onRecyclerItemClickLister.onItemDeleteClicked(position);
                            }
                        }
                    }
                    break;


                }
                return false;
            }
        });
        popup.show();


    }

    public String getFileSize(File file) {
        String modifiedFileSize = null;
        double fileSize;
        if (file.isFile()) {
            fileSize = (double) file.length();//in Bytes

            if (fileSize < 1024) {
                modifiedFileSize = String.valueOf(fileSize).concat("B");
            } else if (fileSize > 1024 && fileSize < (1024 * 1024)) {
                modifiedFileSize = String.valueOf(Math.round((fileSize / 1024 * 100.0)) / 100.0).concat("KB");
            } else {
                modifiedFileSize = String.valueOf(Math.round((fileSize / (1024 * 1204) * 100.0)) / 100.0).concat("MB");
            }
        }

        return modifiedFileSize;
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

    @Override
    public Filter getFilter() {
        return MyFilesFilter;
    }

    private Filter MyFilesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModelFilesHolder> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(tempList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ModelFilesHolder item : tempList) {
                    if (item.getFileName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemsList.clear();
            itemsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void updateData(ArrayList<ModelFilesHolder> viewModels) {
        itemsList.clear();
        itemsList = viewModels;
        notifyDataSetChanged();
    }


}