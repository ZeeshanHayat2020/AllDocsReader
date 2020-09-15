package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.furestic.alldocument.office.ppt.lxs.docx.pdf.viwer.reader.free.R;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.adapters.PdfViewPager2Adapter;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.constant.Constant;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.dialogboxes.NumberPickerDialog;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.fc.pdf.PDFReader;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.fc.util.IOUtils;
import com.google.android.gms.ads.AdListener;
import com.lukelorusso.verticalseekbar.VerticalSeekBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ActivityPdfViewer extends ActivityBase implements NumberPicker.OnValueChangeListener {

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        reqNewInterstitial(this);
        initViews();
        intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (intent != null) {
            if (Intent.ACTION_VIEW.equals(action) && type != null) {
                if ("application/pdf".equals(type)) {
                    Uri tempUri = intent.getData();
                    fileUri = String.valueOf(Uri.parse(getFilePathFromExternalAppsURI(ActivityPdfViewer.this, tempUri)));

                    Log.d(TAG, "onCreate: URI FILE:" + fileUri);
                    Log.d(TAG, "onCreate: MIME TYPE:" + getMimeType(this, tempUri));
                }
            } else {

                fileUri = intent.getStringExtra(Constant.KEY_SELECTED_FILE_URI);
                fileName = intent.getStringExtra(Constant.KEY_SELECTED_FILE_NAME);

            }
            setUpToolBar();
            if (!isProtected(fileUri)) {
                new LoadFiles().execute();
            } else {
                dialogError("Protected", "File is password protected. App cannot open this file.");
                Log.d(TAG, "onCreate: File is protected:");
            }
        } else {
            dialogError("Error", "Failed to load file.");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isProtected(String path) {
        Boolean isEncrypted = Boolean.FALSE;
        try {
            byte[] byteArray = Files.readAllBytes(Paths.get(path));
            //Convert the binary bytes to String. Caution, it can result in loss of data. But for our purposes, we are simply interested in the String portion of the binary pdf data. So we should be fine.
            String pdfContent = new String(byteArray);
            int lastTrailerIndex = pdfContent.lastIndexOf("trailer");
            if (lastTrailerIndex >= 0 && lastTrailerIndex < pdfContent.length()) {
                String newString = pdfContent.substring(lastTrailerIndex, pdfContent.length());
                int firstEOFIndex = newString.indexOf("%%EOF");
                String trailer = newString.substring(0, firstEOFIndex);
                if (trailer.contains("/Encrypt"))
                    isEncrypted = Boolean.TRUE;
            }
        } catch (Exception e) {
            System.out.println(e);
            //Do nothing
        }
        return isEncrypted;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ac_pdfviewer, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pdfView_item_Vertical: {
                if (mInterstitialAd.isLoaded() && !myPreferences.isItemPurchased()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            setViewPagerOrientation(ViewPager2.ORIENTATION_VERTICAL);
                            if (rootViewChangeButton.getVisibility() == View.VISIBLE)
                                rootViewChangeButton.setVisibility(View.INVISIBLE);
                            verticalSeekBar.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    setViewPagerOrientation(ViewPager2.ORIENTATION_VERTICAL);
                    if (rootViewChangeButton.getVisibility() == View.VISIBLE)
                        rootViewChangeButton.setVisibility(View.INVISIBLE);
                    verticalSeekBar.setVisibility(View.VISIBLE);
                }
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

    public String getFilePathFromExternalAppsURI(Context context, Uri contentUri) {
        fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(getFilesDir(), fileName + ".pdf");
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy2(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar.setBackground(getGradient(
                this.getResources().getColor(R.color.color_cardBg_pdfDoc_upper),
                this.getResources().getColor(R.color.color_cardBg_pdfDoc_lower)));
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
        nextBtn.setEnabled(currentPageIndex < totalPages);
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
                nextBtn.setEnabled(currentPageIndex < totalPages);
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
//            Log.d(TAG, "renderPage: Page Num" + i);
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


    private void setViewPagerOrientation(int orientation) {

        viewPager.setOrientation(orientation);
    }

    private void updateViewPager(int currentPageIndex) {
        viewPager.setCurrentItem(currentPageIndex);
        prevBtn.setEnabled(currentPageIndex > 0);
        nextBtn.setEnabled(currentPageIndex < totalPages);
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
        final NumberPickerDialog newFragment = new NumberPickerDialog(0, renderer.getPageCount() - 1);
        newFragment.setValueChangeListener(this);
        if (mInterstitialAd.isLoaded() && !myPreferences.isItemPurchased()) {
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    newFragment.show(getSupportFragmentManager(), "time picker");
                }
            });
        } else {
            newFragment.show(getSupportFragmentManager(), "time picker");
        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View view) {
            if (renderer != null && currentPage != null) {
                switch (view.getId()) {
                    case R.id.btnNext: {
                        if (currentPageIndex == 1 || currentPageIndex == 3 || currentPageIndex == 6 || currentPageIndex == 10) {
                            if (mInterstitialAd.isLoaded() && !myPreferences.isItemPurchased()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        updateViewPager(currentPageIndex + 1);
                                    }
                                });
                            } else {
                                updateViewPager(currentPageIndex + 1);
                            }
                        } else {
                            updateViewPager(currentPageIndex + 1);
                        }

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

    private void dialogError(String title, String message) {
        new AlertDialog.Builder(ActivityPdfViewer.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.sys_button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

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
            getBitmapsFromRenderer();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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
            if (parcelFileDescriptor != null) {
                try {

                    parcelFileDescriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (renderer != null) {
                renderer.close();
            }
        }
    }
}