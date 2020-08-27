package com.example.alldocumentreader.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alldocumentreader.R;
import com.example.alldocumentreader.adapters.AdapterAcMain;
import com.example.alldocumentreader.constant.Constant;
import com.example.alldocumentreader.interfaces.OnRecyclerItemClickLister;
import com.example.alldocumentreader.models.ModelAcMain;
import com.example.alldocumentreader.utils.RecyclerViewItemDecoration;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends ActivityBase implements OnRecyclerItemClickLister {

    private RelativeLayout acMainParentContainer;
    private Toolbar toolbar;
    private TextView toolBarTitleTv;
    private RecyclerView recyclerView;
    private AdapterAcMain adapterAcMain;
    private ArrayList<ModelAcMain> itemsList;
    private GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!hasStoragePermission()) {
            checkStoragePermission();
        }
        initViews();
        setUpToolBar();
        loadRecyclerViewItems();
        setUpRecyclerView();

    }

    private void initViews() {
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.acMain_toolbar);
        acMainParentContainer = (RelativeLayout) findViewById(R.id.acMain_parentContainer);
        recyclerView = findViewById(R.id.acMain_RecyclerView);
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolBarTitleTv = (TextView) findViewById(R.id.toolBar_title_tv);
        toolBarTitleTv.setText("HOME");
    }


    private void loadRecyclerViewItems() {
        String[] itemNames = {
                "All Documents",
                "PDF Files",
                "WordFiles",
                "Text Files",
                "PPT Files",
                "HTML Files",
                "XML Files",
                "Sheet Files"

        };
        itemsList = new ArrayList<>();
        for (int i = 0; i < itemNames.length; i++) {
            itemsList.add(new ModelAcMain(itemNames[i], null));
        }
    }

    private void setUpRecyclerView() {
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._12sdp);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(spacingInPixels));
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        adapterAcMain = new AdapterAcMain(this, itemsList);
        adapterAcMain.notifyDataSetChanged();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterAcMain);
        adapterAcMain.setOnItemClickListener(this);
    }

    public void intentToFilesHolder(String exString) {
        Intent intent = new Intent(this, ActivityFilesHolder.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Constant.KEY_SELECTED_FILE_FORMAT, exString);
        startActivity(intent);

    }

    @Override
    public void onItemClicked(int position) {
        switch (position) {
            case 0: {
                intentToFilesHolder("All Files");
            }
            break;
            case 1: {
                intentToFilesHolder("PDF Files");
            }
            break;
            case 2: {
                intentToFilesHolder("Word Files");
            }
            break;
            case 3: {
                intentToFilesHolder("Text Files");
            }
            break;
            case 4: {
                intentToFilesHolder("PPT Files");
            }
            break;
            case 5: {
                intentToFilesHolder("HTML Files");
            }
            break;
            case 6: {
                intentToFilesHolder("XML Files");
            }
            break;
            case 7: {
                intentToFilesHolder("Sheet Files");
            }
            break;

        }

    }

    @Override
    public void onItemShareClicked(int position) {

    }

    @Override
    public void onItemDeleteClicked(int position) {

    }

    @Override
    public void onItemRenameClicked(int position) {

    }

    @Override
    public void onItemLongClicked(int position) {

    }

    @Override
    public void onItemCheckBoxClicked(View view, int position) {

    }


    private void showPermissionDeniedSnackBar(View view) {
        Snackbar snackbar = Snackbar
                .make(view, "Permission denied, Please allow this app to use device camera or gallery.", Snackbar.LENGTH_LONG)
                .setAction("Allow", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!hasStoragePermission()) {
                            checkStoragePermission();
                        }
                    }
                });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                showPermissionDeniedSnackBar(acMainParentContainer);
            }
        }
    }
}