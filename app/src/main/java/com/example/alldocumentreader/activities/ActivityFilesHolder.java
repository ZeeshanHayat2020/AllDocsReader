package com.example.alldocumentreader.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alldocumentreader.R;
import com.example.alldocumentreader.adapters.AdapterFilesHolder;
import com.example.alldocumentreader.constant.Constant;
import com.example.alldocumentreader.interfaces.OnRecyclerItemClickLister;
import com.example.alldocumentreader.models.ModelFilesHolder;
import com.example.alldocumentreader.officereader.AppActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityFilesHolder extends ActivityBase {

    private Toolbar toolbar;
    private TextView toolBarTitleTv;
    private CheckBox selectAllMenuItem;

    private ProgressBar loadingBar;
    private RecyclerView recyclerView;
    private AdapterFilesHolder adapter;
    private ArrayList<ModelFilesHolder> itemsList;
    private LinearLayoutManager layoutManager;
    private String TAG = "ActivityFilesHolder";

    private Intent intent;
    private String checkFileFormat;

    public boolean isContextualMenuOpen = false;
    public boolean isSelectAll = false;
    private ArrayList<ModelFilesHolder> multiSelectedItemList;
    private String selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_holder);
        intent = getIntent();
        if (intent != null) {
            checkFileFormat = intent.getStringExtra(Constant.KEY_SELECTED_FILE_FORMAT);
        }
        initViews();
        setUpToolBar();
        initRecyclerView();
        getDocumentFiles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ac_filesholder_parent_view, menu);
        MenuItem searchitem = menu.findItem(R.id.menu_filesHolder_parentView_search);
        SearchView searchView = (SearchView) searchitem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        SearchView.OnCloseListener closeListener = new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getDocumentFiles();
                adapter.updateData(itemsList);
                return false;
            }
        };
        searchView.setOnCloseListener(closeListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filesHolder_parentView_shareUs:
                shareUs();
                return true;
            case R.id.menu_filesHolder_parentView_rateUs:
                rateUs();
                return true;
            case R.id.menu_contextual_btnDelete:
                deleteMultipleDialoge();
                return true;
            case R.id.menu_contextual_btnShare:
                shareMultipleFile();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (isContextualMenuOpen) {
            closeContextualMenu();
        } else {
            setResult(RESULT_OK);
            finish();
            super.onBackPressed();
        }
    }

    private void setUpToolBar() {
        selected = getResources().getString(R.string.selected);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolBarTitleTv = (TextView) findViewById(R.id.toolBar_title_tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setGradientToToolBar();
        updateToolBarTitle(checkFileFormat);
    }

    private void updateToolBarTitle(String title) {
        toolBarTitleTv.setText(title);
    }

    public void setGradientToToolBar() {
        if (checkFileFormat.equals(getString(R.string.allDocs))) {
            toolbar.setBackground(getGradient(
                    this.getResources().getColor(R.color.color_cardBg_allDoc_upper),
                    this.getResources().getColor(R.color.color_cardBg_allDoc_lower)));
        } else if (checkFileFormat.equals(getString(R.string.pdf_files))) {
            toolbar.setBackground(getGradient(
                    this.getResources().getColor(R.color.color_cardBg_pdfDoc_upper),
                    this.getResources().getColor(R.color.color_cardBg_pdfDoc_lower)));

        } else if (checkFileFormat.equals(getString(R.string.word_files))) {
            toolbar.setBackground(getGradient(
                    this.getResources().getColor(R.color.color_cardBg_wordDoc_upper),
                    this.getResources().getColor(R.color.color_cardBg_wordDoc_lower)));

        } else if (checkFileFormat.equals(getString(R.string.txtFiles))) {
            toolbar.setBackground(getGradient(
                    this.getResources().getColor(R.color.color_cardBg_txtDoc_upper),
                    this.getResources().getColor(R.color.color_cardBg_txtDoc_lower)));

        } else if (checkFileFormat.equals(getString(R.string.ppt_files))) {
            toolbar.setBackground(getGradient(
                    this.getResources().getColor(R.color.color_cardBg_pptDoc_upper),
                    this.getResources().getColor(R.color.color_cardBg_pdfDoc_lower)));
        } else if (checkFileFormat.equals(getString(R.string.html_files))) {
            toolbar.setBackground(getGradient(
                    this.getResources().getColor(R.color.color_cardBg_htmlDoc_upper),
                    this.getResources().getColor(R.color.color_cardBg_htmlDoc_lower)));

        } else if (checkFileFormat.equals(getString(R.string.xmlFiles))) {
            toolbar.setBackground(getGradient(
                    this.getResources().getColor(R.color.color_cardBg_xmlDoc_upper),
                    this.getResources().getColor(R.color.color_cardBg_xmlDoc_lower)));

        } else if (checkFileFormat.equals(getString(R.string.sheet_files))) {
            toolbar.setBackground(getGradient(
                    this.getResources().getColor(R.color.color_cardBg_sheetDoc_upper),
                    this.getResources().getColor(R.color.color_cardBg_sheetDoc_lower)));

        } else {
            toolbar.setBackground(getGradient(
                    this.getResources().getColor(R.color.color_cardBg_pdfDoc_upper),
                    this.getResources().getColor(R.color.color_cardBg_pdfDoc_lower)));
        }
      /*  switch (checkFileFormat) {
            case getString(R.string.allDocs):
                toolbar.setBackground(getGradient(
                        this.getResources().getColor(R.color.color_cardBg_allDoc_upper),
                        this.getResources().getColor(R.color.color_cardBg_allDoc_lower)));
                break;
            case "PDF Files":
                toolbar.setBackground(getGradient(
                        this.getResources().getColor(R.color.color_cardBg_pdfDoc_upper),
                        this.getResources().getColor(R.color.color_cardBg_pdfDoc_lower)));
                break;
            case "Word Files":
                toolbar.setBackground(getGradient(
                        this.getResources().getColor(R.color.color_cardBg_wordDoc_upper),
                        this.getResources().getColor(R.color.color_cardBg_wordDoc_lower)));
                break;
            case "Sheet Files":
                toolbar.setBackground(getGradient(
                        this.getResources().getColor(R.color.color_cardBg_sheetDoc_upper),
                        this.getResources().getColor(R.color.color_cardBg_sheetDoc_lower)));
                break;
            case "PPT Files":
                toolbar.setBackground(getGradient(
                        this.getResources().getColor(R.color.color_cardBg_pptDoc_upper),
                        this.getResources().getColor(R.color.color_cardBg_pdfDoc_lower)));
                break;
            case "Text Files":
                toolbar.setBackground(getGradient(
                        this.getResources().getColor(R.color.color_cardBg_txtDoc_upper),
                        this.getResources().getColor(R.color.color_cardBg_txtDoc_lower)));
                break;
            case "XML Files":
                toolbar.setBackground(getGradient(
                        this.getResources().getColor(R.color.color_cardBg_xmlDoc_upper),
                        this.getResources().getColor(R.color.color_cardBg_xmlDoc_lower)));
                break;
            case "HTML Files":
                toolbar.setBackground(getGradient(
                        this.getResources().getColor(R.color.color_cardBg_htmlDoc_upper),
                        this.getResources().getColor(R.color.color_cardBg_htmlDoc_lower)));
                break;
        }*/
    }

    private GradientDrawable getGradient(int color1, int color2) {
        int[] colors = {Integer.parseInt(String.valueOf(color1)),
                Integer.parseInt(String.valueOf(color2))
        };
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                colors);
        return gd;
    }


    private void initViews() {

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.acFilesHolder_toolbar);

        loadingBar = (ProgressBar) findViewById(R.id.acFilesHolder_loadingBar);
        loadingBar.setVisibility(View.INVISIBLE);

    }

    private void initRecyclerView() {
        itemsList = new ArrayList<>();
        recyclerView = findViewById(R.id.acFilesHolder_RecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

    }

    private void buildRecyclerView() {
        if (itemsList != null) {
            adapter = new AdapterFilesHolder(ActivityFilesHolder.this, ActivityFilesHolder.this,
                    itemsList);
        }

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnRecyclerItemClickLister() {
            @Override
            public void onItemClicked(int position) {
                if (itemsList.get(position).getFileName().endsWith(".pdf")) {
                    Intent intent = new Intent(ActivityFilesHolder.this, ActivityPdfViewer.class);
                    intent.putExtra(Constant.KEY_SELECTED_FILE_URI, itemsList.get(position).getFileUri());
                    intent.putExtra(Constant.KEY_SELECTED_FILE_NAME, itemsList.get(position).getFileName());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ActivityFilesHolder.this, AppActivity.class);
                    intent.putExtra(Constant.KEY_SELECTED_FILE_URI, itemsList.get(position).getFileUri());
                    intent.putExtra(Constant.KEY_SELECTED_FILE_NAME, itemsList.get(position).getFileName());
                    startActivity(intent);
                }
            }

            @Override
            public void onItemShareClicked(int position) {
                File file = new File(itemsList.get(position).getFileUri());
                if (file.exists()) {
                    shareFile(file);
                }
            }

            @Override
            public void onItemDeleteClicked(int position) {
                File file = new File(itemsList.get(position).getFileUri());
                if (file.exists()) {
                    deleteFileDialog(file, position);
                }
            }

            @Override
            public void onItemRenameClicked(int position) {

                dialogRename(position);
            }

            @Override
            public void onItemLongClicked(int position) {
                openContextualMenu();
            }

            @Override
            public void onItemCheckBoxClicked(View view, int position) {
                try {
                    if (((CheckBox) view).isChecked()) {
                        multiSelectedItemList.add(itemsList.get(position));
                        String text = multiSelectedItemList.size() + selected;
                        updateToolBarTitle(text);
                        if (multiSelectedItemList.size() == itemsList.size()) {
                            selectAllMenuItem.setChecked(true);
                        }
                    } else {
                        multiSelectedItemList.remove(itemsList.get(position));
                        String text = multiSelectedItemList.size() + selected;
                        updateToolBarTitle(text);
                        if (multiSelectedItemList.size() == itemsList.size()) {
                            selectAllMenuItem.setChecked(false);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onItemCheckBoxClicked: " + e);
                }

            }
        });
    }

    private void dialogRename(final int position) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_text, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.dialog_etFileName);
        Button btnRename = (Button) dialogView.findViewById(R.id.dialogBtn_rename);
        Button btnCancel = (Button) dialogView.findViewById(R.id.dialgBtn_Cancel);

        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        btnRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renameFile(itemsList.get(position).getFileUri(), itemsList.get(position).getFileName(), editText.getText().toString());
                dialogBuilder.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }


    private void renameFile(final String filePath, final String oldFileName, final String newFileName) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                String completeNewFileName = "";
                if (oldFileName.endsWith(".pdf")) {
                    completeNewFileName = newFileName + ".pdf";
                } else if (oldFileName.endsWith(".doc")) {
                    completeNewFileName = newFileName + ".doc";
                } else if (oldFileName.endsWith(".docx")) {
                    completeNewFileName = newFileName + ".docx";
                } else if (oldFileName.endsWith(".ppt")) {
                    completeNewFileName = newFileName + ".ppt";
                } else if (oldFileName.endsWith(".txt")) {
                    completeNewFileName = newFileName + ".txt";
                } else if (oldFileName.endsWith(".xml")) {
                    completeNewFileName = newFileName + ".xml";
                } else if (oldFileName.endsWith(".html")) {
                    completeNewFileName = newFileName + ".html";
                } else if (oldFileName.endsWith(".xls")) {
                    completeNewFileName = newFileName + ".xls";
                } else if (oldFileName.endsWith(".xlsx")) {
                    completeNewFileName = newFileName + ".xlsx";
                } else {
                }

                File oldPath = new File(filePath);
                File parentFileName = new File(oldPath.getParent());
                File from = new File(parentFileName.toString(), oldFileName);
                File to = new File(parentFileName.toString(), completeNewFileName);
                from.renameTo(to);
                Log.d(TAG, "renameFile: parentFileName:" + parentFileName + "  oldFileName:" + oldFileName + "newFileName:" + completeNewFileName);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getDocumentFiles();
            }
        }.execute();
    }

    private void openContextualMenu() {
        multiSelectedItemList = new ArrayList<>();
        isContextualMenuOpen = true;
        isSelectAll = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_ac_filesholder_contextual);
        toolbar.setNavigationIcon(R.drawable.ic_cross);
        selectAllMenuItem = (CheckBox) toolbar.getMenu().findItem(R.id.menu_contextual_btnSelecAll).getActionView();
        selectAllMenuItem.setChecked(false);

        updateToolBarTitle("0" + selected);
        adapter.notifyDataSetChanged();
        selectAllMenuItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    selectAllItems(true);
                    isSelectAll = true;
                } else {
                    selectAllItems(false);
                    isSelectAll = false;
                }
            }
        });
    }

    private void selectAllItems(boolean isSelectAll) {
        if (isSelectAll) {
            if (!multiSelectedItemList.isEmpty()) {
                multiSelectedItemList.removeAll(itemsList);
                multiSelectedItemList.clear();
            }
            for (int i = 0; i < itemsList.size(); i++) {
                multiSelectedItemList.add(itemsList.get(i));
            }
            String text = multiSelectedItemList.size() + selected;
            updateToolBarTitle(text);
        } else {
            multiSelectedItemList.removeAll(itemsList);
            multiSelectedItemList.clear();
            String text = multiSelectedItemList.size() + selected;
            updateToolBarTitle(text);
        }
        adapter.notifyDataSetChanged();
    }

    private void closeContextualMenu() {
        multiSelectedItemList.removeAll(itemsList);
        multiSelectedItemList.clear();
        isContextualMenuOpen = false;
        isSelectAll = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_ac_filesholder_parent_view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        updateToolBarTitle(checkFileFormat);
        adapter.notifyDataSetChanged();
    }

    private void getDocumentFiles() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                if (!itemsList.isEmpty() || itemsList.size() > 0) {
                    itemsList.clear();
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                File parentFile = new File(String.valueOf(Environment.getExternalStorageDirectory()));
                switch (checkFileFormat) {
                    case "All Files":
                        allDocsFiles(parentFile);
                        break;
                    case "PDF Files":
                        pdfFiles(parentFile);
                        break;
                    case "Word Files":
                        wordFiles(parentFile);
                        break;
                    case "Sheet Files":
                        sheetFiles(parentFile);
                        break;
                    case "PPT Files":
                        pptFiles(parentFile);
                        break;
                    case "Text Files":
                        textFiles(parentFile);
                        break;
                    case "XML Files":
                        xmlFiles(parentFile);
                        break;
                    case "HTML Files":
                        htmlFiles(parentFile);
                        break;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setVisibility(View.VISIBLE);
                        buildRecyclerView();
                        loadingBar.setVisibility(View.INVISIBLE);


                    }
                }, 100);
            }
        }.execute();
    }

    private void pdfFiles(File parentFile) {

        int id = R.drawable.ic_svg_file_type_pdf;
        File[] files = parentFile.listFiles();

        if (files != null) {

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    pdfFiles(files[i]);
                } else {
                    if (files[i].getName().endsWith(".pdf")) {
                        if (!itemsList.contains(files[i])) {
                            itemsList.add(new ModelFilesHolder(files[i].getName(), files[i].getAbsolutePath()));
                        }
                    }
                }
            }
        }
    }

    private void wordFiles(File parentFile) {

        int id = R.drawable.ic_svg_file_type_pdf;
        File[] files = parentFile.listFiles();

        if (files != null) {

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    wordFiles(files[i]);
                } else {
                    if (files[i].getName().endsWith(".doc") || files[i].getName().endsWith(".docx")) {
                        if (!itemsList.contains(files[i])) {
                            itemsList.add(new ModelFilesHolder(files[i].getName(), files[i].getAbsolutePath()));
                        }
                    }
                }
            }
        }
    }

    private void sheetFiles(File parentFile) {

        int id = R.drawable.ic_svg_file_type_pdf;
        File[] files = parentFile.listFiles();

        if (files != null) {

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    sheetFiles(files[i]);
                } else {
                    if (files[i].getName().endsWith("xls") || files[i].getName().endsWith("xlsx")) {
                        if (!itemsList.contains(files[i])) {
                            itemsList.add(new ModelFilesHolder(files[i].getName(), files[i].getAbsolutePath()));
                        }
                    }
                }
            }
        }

    }

    private void pptFiles(File parentFile) {
        int id = R.drawable.ic_svg_file_type_pdf;
        File[] files = parentFile.listFiles();

        if (files != null) {

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    pptFiles(files[i]);
                } else {
                    if (files[i].getName().endsWith("ppt") || files[i].getName().endsWith("pptx")) {
                        if (!itemsList.contains(files[i])) {
                            itemsList.add(new ModelFilesHolder(files[i].getName(), files[i].getAbsolutePath()));
                        }
                    }
                }
            }
        }

    }

    private void textFiles(File parentFile) {
        int id = R.drawable.ic_svg_file_type_pdf;
        File[] files = parentFile.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    textFiles(files[i]);
                } else {
                    if (files[i].getName().endsWith(".txt")) {
                        if (!itemsList.contains(files[i])) {
                            itemsList.add(new ModelFilesHolder(files[i].getName(), files[i].getAbsolutePath()));
                        }
                    }
                }
            }
        }

    }

    private void xmlFiles(File parentFile) {

        int id = R.drawable.ic_svg_file_type_pdf;
        File[] files = parentFile.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    xmlFiles(files[i]);
                } else {
                    if (files[i].getName().endsWith(".xml")) {
                        if (!itemsList.contains(files[i])) {
                            itemsList.add(new ModelFilesHolder(files[i].getName(), files[i].getAbsolutePath()));
                        }
                    }
                }
            }
        }
    }

    private void htmlFiles(File parentFile) {

        int id = R.drawable.ic_svg_file_type_pdf;
        File[] files = parentFile.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    htmlFiles(files[i]);
                } else {
                    if (files[i].getName().endsWith(".html")) {
                        if (!itemsList.contains(files[i])) {
                            itemsList.add(new ModelFilesHolder(files[i].getName(), files[i].getAbsolutePath()));
                        }
                    }
                }
            }
        }

    }

    private void allDocsFiles(File parentFile) {
        int id = R.drawable.ic_svg_file_type_pdf;
        File[] files = parentFile.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    allDocsFiles(files[i]);
                } else {
                    if (files[i].getName().endsWith(".pdf") || files[i].getName().endsWith(".doc")
                            || files[i].getName().endsWith(".docx") || files[i].getName().endsWith(".ppt") || files[i].getName().endsWith(".xlsx") || files[i].getName().endsWith(".xls") ||
                            files[i].getName().endsWith(".ppt") || files[i].getName().endsWith(".pptx") || files[i].getName().endsWith(".txt") || files[i].getName().endsWith(".xml")) {
                        if (!itemsList.contains(files[i])) {
                            itemsList.add(new ModelFilesHolder(files[i].getName(), files[i].getAbsolutePath()));
                        }
                    }
                }
            }
        }
    }

    private void deleteFileDialog(final File LocalFile, final int position) {
        new AlertDialog.Builder(ActivityFilesHolder.this)
                .setTitle("Delete Alert")
                .setMessage(R.string.delete_msg)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LocalFile.delete();
                        itemsList.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

    private void shareFile(File DocFile) {
        if (DocFile.exists()) {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            if (DocFile.exists()) {
                intentShareFile.setType("application/*");
                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(DocFile.getAbsolutePath()));
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing File...");
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                startActivity(Intent.createChooser(intentShareFile, "Share File"));
            }

        }
    }

    private void deleteMultipleDialoge() {
        new AlertDialog.Builder(ActivityFilesHolder.this)
                .setTitle("Delete Alert")
                .setMessage(R.string.delete_msg)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                loadingBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            protected Void doInBackground(Void... voids) {

                                for (int i = 0; i < multiSelectedItemList.size(); i++) {
                                    File file = new File(multiSelectedItemList.get(i).getFileUri());
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    itemsList.remove(multiSelectedItemList.get(i));
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                adapter.notifyDataSetChanged();
                                loadingBar.setVisibility(View.GONE);
                            }
                        }.execute();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void shareMultipleFile() {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND_MULTIPLE);

        ArrayList<Uri> uris = new ArrayList<>();
        for (int i = 0; i < multiSelectedItemList.size(); i++) {
            File file = new File(multiSelectedItemList.get(i).getFileUri());
            uris.add(Uri.fromFile(file));
        }

        intentShareFile.setType("application/*");
        intentShareFile.putExtra(Intent.EXTRA_STREAM, uris);
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing Files...");
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing Files...");
        startActivity(Intent.createChooser(intentShareFile, "Share File"));

    }


}