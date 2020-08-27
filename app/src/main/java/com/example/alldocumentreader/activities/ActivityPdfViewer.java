package com.example.alldocumentreader.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alldocumentreader.R;
import com.example.alldocumentreader.adapters.PdfViewPager2Adapter;
import com.example.alldocumentreader.constant.Constant;
import com.example.alldocumentreader.dialogboxes.NumberPickerDialog;
import com.lukelorusso.verticalseekbar.VerticalSeekBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ActivityPdfViewer extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    String TAG = "ActivityPdfRenderView";
    private Toolbar toolbar;
    private TextView toolBarTitleTv;
    private PdfRenderer renderer;
    private PdfRenderer.Page currentPage;
    private LinearLayout rootViewChangeButton;
    private ProgressBar loadingBar;
    private Button prevBtn;
    private Button nextBtn;
    private TextView tv_pageCount;
    private ImageButton btnFindPage;
    private ParcelFileDescriptor parcelFileDescriptor;
    private ViewPager2 viewPager;
    private PdfViewPager2Adapter pagerAdapter;
    private ArrayList<Bitmap> pdfImagesList;
    private int currentPageIndex;
    private int totalPages;
    private VerticalSeekBar verticalSeekBar;
    private boolean isSwipe;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Intent intent;
    private String fileUri;
    private String fileName;

    private Uri externalStoragePdfFileUri;
    private String externalStoragePdfFileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSharedPrefs();
        if (getThemeNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_pdf_viewer);
        intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();



        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if ("application/pdf".equals(type)) {
                externalStoragePdfFileUri = intent.getData();

                fileName = getExternalStorageFileName(externalStoragePdfFileUri);
                File file = new File(getPath(externalStoragePdfFileUri));
                fileUri = file.getAbsolutePath();
                Log.d(TAG, "onCreate: URI FILE:" + fileUri);

            }
        } else {
            if (intent != null) {
                fileUri = intent.getStringExtra(Constant.KEY_SELECTED_FILE_URI);
                fileName = intent.getStringExtra(Constant.KEY_SELECTED_FILE_NAME);
            }
        }
        initViews();
        setUpToolBar();
        new LoadFiles().execute();
    }

   /* private void getDataFromWhatsApp(Intent intent){
        for (int i=0; i<intent.getClipData().getItemCount(); i++){
            Uri uri = intent.getClipData().getItemAt(i).getUri();
            InputStream inputstream = null;
            try {
                inputstream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] data = new byte[1024];
            int bytesRead = 0;
            try {
                bytesRead = inputstream.read(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (bytesRead != -1) {

//                chatContent.append(new String(data));
                try {
                    bytesRead = inputstream.read(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "getDataFromWhatsApp: " +new String(data));
            }
        }
    }*/
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void initViews() {
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.acPdfViewer_toolbar);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        rootViewChangeButton = (LinearLayout) findViewById(R.id.pdfView_ac_rootView_buttons);
        tv_pageCount = (TextView) findViewById(R.id.pdfView_ac_pageCount_Tv);
        verticalSeekBar = findViewById(R.id.pdfView_ac_seekbarVertical);
        prevBtn = (Button) findViewById(R.id.btnPrev);
        nextBtn = (Button) findViewById(R.id.btnNext);
        btnFindPage = (ImageButton) findViewById(R.id.btn_findPage);
        btnFindPage.setOnClickListener(onClickListener);
        nextBtn.setOnClickListener(onClickListener);
        prevBtn.setOnClickListener(onClickListener);
        tv_pageCount.setOnClickListener(onClickListener);
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolBarTitleTv = (TextView) findViewById(R.id.toolBar_title_tv);
        toolBarTitleTv.setText(fileName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initRenderer() {
        try {
            File tempFile = new File(fileUri);
            parcelFileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY);
            renderer = new PdfRenderer(parcelFileDescriptor);
            totalPages = renderer.getPageCount() - 1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "initRenderer: Error File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new PdfViewPager2Adapter(this, pdfImagesList, viewPager);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setCurrentItem(0);
        prevBtn.setEnabled(currentPageIndex > 0);
        nextBtn.setEnabled(currentPageIndex + 1 < totalPages);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "Page SelectedListener  Called:");
                super.onPageSelected(position);
            }


            @Override
            public void onPageScrollStateChanged(int state) {
                currentPageIndex = viewPager.getCurrentItem();
                prevBtn.setEnabled(currentPageIndex > 0);
                nextBtn.setEnabled(currentPageIndex + 1 < totalPages);
                updateScrollerValue(currentPageIndex);
                isSwipe = true;
                super.onPageScrollStateChanged(state);
            }
        });

    }

    private void getBitmapsFromRenderer() {
        pdfImagesList = new ArrayList<>();
        for (int i = 0; i < renderer.getPageCount(); i++) {
            if (currentPage != null) {
                currentPage.close();
            }
            currentPage = renderer.openPage(i);
            Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(), Bitmap.Config.ARGB_8888);
            currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            pdfImagesList.add(bitmap);
            Log.d(TAG, "renderPage: Page Num" + i);
        }
    }

    private void setUpVerticalSeekBar(int maxValue) {
        verticalSeekBar.setMaxValue(maxValue);
        verticalSeekBar.setProgress(0);
        verticalSeekBar.setOnProgressChangeListener(
                new Function1<Integer, Unit>() {
                    @Override
                    public Unit invoke(Integer progressValue) {
                        updateViewPager(progressValue);
                        isSwipe = false;
                        Log.d(TAG, "seekbar Pressed Called:");
                        return null;
                    }
                }
        );
        verticalSeekBar.setOnPressListener(
                new Function1<Integer, Unit>() {
                    @Override
                    public Unit invoke(Integer progressValue) {
                        if (isSwipe == false) {
                            updateViewPager(progressValue);
                            Log.d(TAG, "seekbar Value Change Called:");
                        }
                        isSwipe = false;
                        return null;
                    }
                }
        );
        verticalSeekBar.setOnReleaseListener(
                new Function1<Integer, Unit>() {
                    @Override
                    public Unit invoke(Integer progressValue) {
                        updateViewPager(progressValue);
                        Log.d(TAG, "seekbar Release  Called:");
                        return null;
                    }
                }
        );

    }

    private void initSharedPrefs() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
    }

    private boolean getThemeNightMode() {
        return sharedPreferences.getBoolean(Constant.KEY_PREFS_THEME_MODE, false);
    }

    private void saveThemeNightMode(boolean nightMode) {
        editor.putBoolean(Constant.KEY_PREFS_THEME_MODE, nightMode);
        editor.commit();
        editor.apply();
    }

    private void setThemeMode(boolean isNightMode) {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private String getExternalStorageFileName(Uri uri) {
        String fileName = null;
        String scheme = uri.getScheme();
        if (scheme.equals("file")) {
            fileName = uri.getLastPathSegment();
        } else if (scheme.equals("content")) {
            String[] proj = {MediaStore.Images.Media.TITLE};
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                cursor.moveToFirst();
                fileName = cursor.getString(columnIndex);
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return fileName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ac_pdfviewer, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem menuIsNightMode = menu.findItem(R.id.menu_pdfView_item_Theme);
        if (getThemeNightMode()) {
            menuIsNightMode.setChecked(true);
            setThemeMode(true);
        } else {
            menuIsNightMode.setChecked(false);
            setThemeMode(false);
        }
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pdfView_item_Theme: {
                if (item.isChecked()) {
                    item.setChecked(false);
                    saveThemeNightMode(false);
                    setThemeMode(false);
                } else {
                    item.setChecked(true);
                    saveThemeNightMode(true);
                    setThemeMode(true);
                }
            }
            break;
            case R.id.menu_pdfView_item_Vertical: {
                setViewPagerOrientation(ViewPager2.ORIENTATION_VERTICAL);
                if (rootViewChangeButton.getVisibility() == View.VISIBLE)
                    rootViewChangeButton.setVisibility(View.INVISIBLE);
                verticalSeekBar.setVisibility(View.VISIBLE);
            }
            break;
            case R.id.menu_pdfView_item_Horizontal: {
                setViewPagerOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                if (verticalSeekBar.getVisibility() == View.VISIBLE) {
                    rootViewChangeButton.setVisibility(View.VISIBLE);
                    verticalSeekBar.setVisibility(View.INVISIBLE);

                }
            }
            break;
            default: {
                super.onOptionsItemSelected(item);
            }
        }
        return true;
    }

    private void setViewPagerOrientation(int orientation) {

        viewPager.setOrientation(orientation);
    }

    private void updateViewPager(int currentPageIndex) {
        viewPager.setCurrentItem(currentPageIndex);
        prevBtn.setEnabled(currentPageIndex > 0);
        nextBtn.setEnabled(currentPageIndex + 1 < totalPages);
        updatePageCountTV(currentPageIndex);
    }

    private void updatePageCountTV(int currentPageIndex) {
        int roundedCurrentPage = currentPageIndex + 1;
        int roundedTotalPages = totalPages + 1;
        tv_pageCount.setText(roundedCurrentPage + "/" + roundedTotalPages);
    }

    private void updateScrollerValue(int progress) {
        if (isSwipe == true) {
            verticalSeekBar.setProgress(progress);
        }
    }

    public void showNumberPicker() {
        NumberPickerDialog newFragment = new NumberPickerDialog(0, renderer.getPageCount() - 1);
        newFragment.setValueChangeListener(this);
        newFragment.show(getSupportFragmentManager(), "time picker");
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View view) {
            if (renderer != null && currentPage != null) {
                switch (view.getId()) {
                    case R.id.btnNext: {
                        updateViewPager(currentPageIndex + 1);
                    }
                    break;
                    case R.id.btnPrev: {
                        updateViewPager(currentPageIndex - 1);
                    }
                    break;
                    case R.id.btn_findPage: {
                        showNumberPicker();
                    }
                    break;
                    case R.id.pdfView_ac_pageCount_Tv: {
                        showNumberPicker();
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {

        updateViewPager(i);

        Log.d(TAG, "onValueChange: Number PickerValue:" + numberPicker.getValue() + "i:" + i + "i1:" + i);
    }


    public class LoadFiles extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Void doInBackground(Void... voids) {
            initRenderer();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getBitmapsFromRenderer();
            loadingBar.setVisibility(View.INVISIBLE);
            if (pdfImagesList != null) {
                tv_pageCount.setVisibility(View.VISIBLE);
                initViewPager();
                updatePageCountTV(0);
                setUpVerticalSeekBar(totalPages);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            if (currentPage != null) {
                currentPage.close();
            }

            try {
                parcelFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            renderer.close();
        }
    }
}