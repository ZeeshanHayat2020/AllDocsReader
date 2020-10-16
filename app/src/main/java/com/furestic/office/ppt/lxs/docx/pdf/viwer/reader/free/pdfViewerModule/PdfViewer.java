package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.pdfViewerModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.furestic.alldocument.office.ppt.lxs.docx.pdf.viwer.reader.free.R;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.activities.ActivityPdfViewer;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.constant.Constant;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.fc.util.IOUtils;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.pdfViewerModule.fragment.DocumentPropertiesFragment;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.pdfViewerModule.fragment.JumpToPageFragment;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.pdfViewerModule.loader.DocumentPropertiesLoader;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class PdfViewer extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<CharSequence>> {
    public static final String TAG = "PdfViewer";

    private static final String STATE_URI = "uri";
    private static final String STATE_PAGE = "page";
    private static final String STATE_ZOOM_RATIO = "zoomRatio";
    private static final String STATE_DOCUMENT_ORIENTATION_DEGREES = "documentOrientationDegrees";
    private static final String KEY_PROPERTIES = "properties";

    private static final String CONTENT_SECURITY_POLICY =
            "default-src 'none'; " +
                    "form-action 'none'; " +
                    "connect-src https://localhost/placeholder.pdf; " +
                    "img-src blob: 'self'; " +
                    "script-src 'self' 'resource://pdf.js'; " +
                    "style-src 'self'; " +
                    "frame-ancestors 'none'; " +
                    "base-uri 'none'";

    private static final String FEATURE_POLICY =
            "accelerometer 'none'; " +
                    "ambient-light-sensor 'none'; " +
                    "autoplay 'none'; " +
                    "camera 'none'; " +
                    "encrypted-media 'none'; " +
                    "fullscreen 'none'; " +
                    "geolocation 'none'; " +
                    "gyroscope 'none'; " +
                    "magnetometer 'none'; " +
                    "microphone 'none'; " +
                    "midi 'none'; " +
                    "payment 'none'; " +
                    "picture-in-picture 'none'; " +
                    "speaker 'none'; " +
                    "sync-xhr 'none'; " +
                    "usb 'none'; " +
                    "vr 'none'";

    private static final float MIN_ZOOM_RATIO = 0.5f;
    private static final float MAX_ZOOM_RATIO = 1.5f;
    private static final int ALPHA_LOW = 130;
    private static final int ALPHA_HIGH = 255;
    private static final int ACTION_OPEN_DOCUMENT_REQUEST_CODE = 1;
    private static final int STATE_LOADED = 1;
    private static final int STATE_END = 2;
    private static final int PADDING = 10;

    private Uri mUri;
    public int mPage;
    public int mNumPages;
    private float mZoomRatio = 1f;
    private int mDocumentOrientationDegrees;
    private int mDocumentState;
    private int windowInsetTop;
    private List<CharSequence> mDocumentProperties;
    private InputStream mInputStream;

    private WebView mWebView;
    private TextView mTextView;
    private Toast mToast;
    private Snackbar snackbar;


    private LinearLayout rootViewChangeButton;
    private ProgressBar loadingBar;
    private Button prevBtn;
    private Button nextBtn;
    private Intent intent;
    private String fileUri;
    private String fileName;

    private class Channel {
        @JavascriptInterface
        public int getWindowInsetTop() {
            return windowInsetTop;
        }

        @JavascriptInterface
        public int getPage() {
            return mPage;
        }

        @JavascriptInterface
        public float getZoomRatio() {
            return mZoomRatio;
        }

        @JavascriptInterface
        public int getDocumentOrientationDegrees() {
            return mDocumentOrientationDegrees;
        }

        @JavascriptInterface
        public void setNumPages(int numPages) {
            mNumPages = numPages;
            runOnUiThread(PdfViewer.this::invalidateOptionsMenu);
        }

        @JavascriptInterface
        public void setDocumentProperties(final String properties) {
            if (mDocumentProperties != null) {
                throw new SecurityException("mDocumentProperties not null");
            }

            final Bundle args = new Bundle();
            args.putString(KEY_PROPERTIES, properties);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoaderManager.getInstance(PdfViewer.this).restartLoader(DocumentPropertiesLoader.ID, args, PdfViewer.this);
                }
            });
        }
    }

    // Can be removed once minSdkVersion >= 26
    @SuppressWarnings("deprecation")
    private void disableSaveFormData(final WebSettings settings) {
        settings.setSaveFormData(false);
    }

    @Override
    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pdf_viewer_module);
        initMyViews();
        mWebView = findViewById(R.id.webview);
        getSupportActionBar().setTitle("PDF Files");

        mWebView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                windowInsetTop = insets.getSystemWindowInsetTop();
                mWebView.evaluateJavascript("updateInset()", null);
                return insets;
            }
        });

        final WebSettings settings = mWebView.getSettings();
        settings.setAllowContentAccess(false);
        settings.setAllowFileAccess(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
        disableSaveFormData(settings);

        CookieManager.getInstance().setAcceptCookie(false);

        mWebView.addJavascriptInterface(new Channel(), "channel");

        mWebView.setWebViewClient(new WebViewClient() {
            private WebResourceResponse fromAsset(final String mime, final String path) {
                try {
                    InputStream inputStream = getAssets().open(path.substring(1));
                    return new WebResourceResponse(mime, null, inputStream);
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (!"GET".equals(request.getMethod())) {
                    return null;
                }

                final Uri url = request.getUrl();
                if (!"localhost".equals(url.getHost())) {
                    return null;
                }

                final String path = url.getPath();
                Log.d(TAG, "path " + path);

                if ("/placeholder.pdf".equals(path)) {
                    return new WebResourceResponse("application/pdf", null, mInputStream);
                }

                if ("/viewer.html".equals(path)) {
                    final WebResourceResponse response = fromAsset("text/html", path);
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Security-Policy", CONTENT_SECURITY_POLICY);
                    headers.put("Feature-Policy", FEATURE_POLICY);
                    headers.put("X-Content-Type-Options", "nosniff");
                    response.setResponseHeaders(headers);
                    return response;
                }

                if ("/viewer.css".equals(path)) {
                    return fromAsset("text/css", path);
                }

                if ("/viewer.js".equals(path) || "/pdf.js".equals(path) || "/pdf.worker.js".equals(path)) {
                    return fromAsset("application/javascript", path);
                }

                return null;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mDocumentState = STATE_LOADED;
                invalidateOptionsMenu();
            }
        });

        GestureHelper.attach(PdfViewer.this, mWebView,
                new GestureHelper.GestureListener() {
                    @Override
                    public boolean onTapUp() {
                        if (mUri != null) {
                            mWebView.evaluateJavascript("isTextSelected()", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String selection) {
                                }
                            });
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void onZoomIn(float value) {
                        zoomIn(value, false);
                    }

                    @Override
                    public void onZoomOut(float value) {
                        zoomOut(value, false);
                    }

                    @Override
                    public void onZoomEnd() {
                        zoomEnd();
                    }
                });

        mTextView = new TextView(this);
        mTextView.setBackgroundColor(Color.DKGRAY);
        mTextView.setTextColor(ColorStateList.valueOf(Color.WHITE));
        mTextView.setTextSize(18);
        mTextView.setPadding(PADDING, 0, PADDING, 0);

        // If loaders are not being initialized in onCreate(), the result will not be delivered
        // after orientation change (See FragmentHostCallback), thus initialize the
        // loader manager impl so that the result will be delivered.
        LoaderManager.getInstance(this);

        snackbar = Snackbar.make(mWebView, "", Snackbar.LENGTH_LONG);
        intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (intent != null) {
            if (Intent.ACTION_VIEW.equals(action) && type != null) {
                if ("application/pdf".equals(type)) {
                    Uri tempUri = intent.getData();
                    mPage = 1;
                    fileUri = String.valueOf(Uri.parse(getFilePathFromExternalAppsURI(PdfViewer.this, tempUri)));
                    mUri = Uri.fromFile(new File(fileUri));
//                    mUri = Uri.parse(getFilePathFromExternalAppsURI(PdfViewer.this, tempUri));
                    Log.d(TAG, "onCreate: URI FILE:" + mUri);
                    Log.d(TAG, "onCreate: MIME TYPE:" + getMimeType(this, tempUri));
                }
            } else {

                fileUri = intent.getStringExtra(Constant.KEY_SELECTED_FILE_URI);
                fileName = intent.getStringExtra(Constant.KEY_SELECTED_FILE_NAME);
                mUri = Uri.fromFile(new File(fileUri));
                mPage = 1;

            }
            if (savedInstanceState != null) {
                mUri = savedInstanceState.getParcelable(STATE_URI);
                mPage = savedInstanceState.getInt(STATE_PAGE);
                mZoomRatio = savedInstanceState.getFloat(STATE_ZOOM_RATIO);
                mDocumentOrientationDegrees = savedInstanceState.getInt(STATE_DOCUMENT_ORIENTATION_DEGREES);
            }


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                if (!isProtected(fileUri)) {
                    loadPdf();
                } else {
                    dialogError("Protected", "File is password protected. App cannot open this file.");
                    Log.d(TAG, "onCreate: File is protected:");
                }
            } else {
                loadPdf();
            }

        } else {
            dialogError("Error", "Failed to load file.");
        }


    }

    private void initMyViews() {
        loadingBar = findViewById(R.id.moduleAcPdfView_loadingBar);
        rootViewChangeButton = findViewById(R.id.moduleAcPdfView_rootView_buttons);
        prevBtn = findViewById(R.id.moduleAcPdfView_btnPrev);
        nextBtn = findViewById(R.id.moduleAcPdfView_btnNext);
        nextBtn.setOnClickListener(onClickListener);
        prevBtn.setOnClickListener(onClickListener);
        showLoadingBar();
    }

    void showLoadingBar() {
        loadingBar.setVisibility(View.VISIBLE);
    }

    void hideLoadingBar() {
        loadingBar.setVisibility(View.INVISIBLE);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.moduleAcPdfView_btnNext: {
                    onJumpToPageInDocument(mPage + 1);
                }
                break;
                case R.id.moduleAcPdfView_btnPrev: {
                    onJumpToPageInDocument(mPage - 1);
                }
                break;
            }
        }
    };

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isProtected(String path) {
        Boolean isEncrypted = Boolean.FALSE;
        try {
            byte[] byteArray = new byte[0];

            byteArray = Files.readAllBytes(Paths.get(path));

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

    private void dialogError(String title, String message) {
        new AlertDialog.Builder(PdfViewer.this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.sys_button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public Loader<List<CharSequence>> onCreateLoader(int id, Bundle args) {
        return new DocumentPropertiesLoader(this, args.getString(KEY_PROPERTIES), mNumPages, mUri);
    }

    @Override
    public void onLoadFinished(Loader<List<CharSequence>> loader, List<CharSequence> data) {
        mDocumentProperties = data;
        LoaderManager.getInstance(this).destroyLoader(DocumentPropertiesLoader.ID);
    }

    @Override
    public void onLoaderReset(Loader<List<CharSequence>> loader) {
        mDocumentProperties = null;
    }

    private void loadPdf() {
        try {
            if (mInputStream != null) {
                mInputStream.close();
            }
            mInputStream = getContentResolver().openInputStream(mUri);
            getSupportActionBar().setTitle(fileName);
            zoomOut(0.25f, true);
        } catch (IOException e) {
            snackbar.setText(R.string.io_error).show();
            return;
        }

        mWebView.loadUrl("https://localhost/viewer.html");
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadingBar();
            }
        }, 1000);

    }

    private void renderPage(final int zoom) {
        mWebView.evaluateJavascript("onRenderPage(" + zoom + ")", null);
    }

    private void documentOrientationChanged(final int orientationDegreesOffset) {
        mDocumentOrientationDegrees = (mDocumentOrientationDegrees + orientationDegreesOffset) % 360;
        if (mDocumentOrientationDegrees < 0) {
            mDocumentOrientationDegrees += 360;
        }
        renderPage(0);
    }

    private void openDocument() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, ACTION_OPEN_DOCUMENT_REQUEST_CODE);
    }

    private void zoomIn(float value, boolean end) {
        if (mZoomRatio < MAX_ZOOM_RATIO) {
            mZoomRatio = Math.min(mZoomRatio + value, MAX_ZOOM_RATIO);
            renderPage(end ? 1 : 2);
            invalidateOptionsMenu();
        }
    }

    private void zoomOut(float value, boolean end) {
        if (mZoomRatio > MIN_ZOOM_RATIO) {
            mZoomRatio = Math.max(mZoomRatio - value, MIN_ZOOM_RATIO);
            renderPage(end ? 1 : 2);
            invalidateOptionsMenu();
        }
    }

    private void zoomEnd() {
        renderPage(1);
    }

    private static void enableDisableMenuItem(MenuItem item, boolean enable) {
        if (enable) {
            item.setEnabled(true);
            item.getIcon().setAlpha(ALPHA_HIGH);
        } else {
            item.setEnabled(false);
            item.getIcon().setAlpha(ALPHA_LOW);
        }
    }

    public void onJumpToPageInDocument(final int selected_page) {
        if (selected_page >= 1 && selected_page <= mNumPages && mPage != selected_page) {
            mPage = selected_page;
            renderPage(0);
            showPageNumber();
            prevBtn.setEnabled(selected_page > 1);
            nextBtn.setEnabled(selected_page < mNumPages);
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(STATE_URI, mUri);
        savedInstanceState.putInt(STATE_PAGE, mPage);
        savedInstanceState.putFloat(STATE_ZOOM_RATIO, mZoomRatio);
        savedInstanceState.putInt(STATE_DOCUMENT_ORIENTATION_DEGREES, mDocumentOrientationDegrees);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == ACTION_OPEN_DOCUMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                mUri = resultData.getData();
                mPage = 1;
                mDocumentProperties = null;
                loadPdf();
                invalidateOptionsMenu();
            }
        }
    }

    private void showPageNumber() {
        if (mToast != null) {
            mToast.cancel();
        }
        mTextView.setText(String.format("%s/%s", mPage, mNumPages));
        mToast = new Toast(getApplicationContext());
        mToast.setGravity(Gravity.BOTTOM, 0, 160);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(mTextView);
        mToast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pdf_viewer, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final int ids[] = {R.id.action_zoom_in, R.id.action_zoom_out, R.id.action_jump_to_page, R.id.action_first, R.id.action_last,
                R.id.action_rotate_clockwise, R.id.action_rotate_counterclockwise,
                R.id.action_view_document_properties};
        if (mDocumentState < STATE_LOADED) {
            for (final int id : ids) {
                final MenuItem item = menu.findItem(id);
                if (item.isVisible()) {
                    item.setVisible(false);
                }
            }
        } else if (mDocumentState == STATE_LOADED) {
            for (final int id : ids) {
                final MenuItem item = menu.findItem(id);
                if (!item.isVisible()) {
                    item.setVisible(true);
                }
            }
            mDocumentState = STATE_END;
        }

        enableDisableMenuItem(menu.findItem(R.id.action_zoom_in), mZoomRatio != MAX_ZOOM_RATIO);
        enableDisableMenuItem(menu.findItem(R.id.action_zoom_out), mZoomRatio != MIN_ZOOM_RATIO);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.action_first:
                onJumpToPageInDocument(1);
                return true;

            case R.id.action_last:
                onJumpToPageInDocument(mNumPages);
                return true;

            case R.id.action_zoom_out:
                zoomOut(0.25f, true);
                return true;

            case R.id.action_zoom_in:
                zoomIn(0.25f, true);
                return true;

            case R.id.action_rotate_clockwise:
                documentOrientationChanged(90);
                return true;

            case R.id.action_rotate_counterclockwise:
                documentOrientationChanged(-90);
                return true;

            case R.id.action_view_document_properties:
                DocumentPropertiesFragment
                        .newInstance(mDocumentProperties)
                        .show(getSupportFragmentManager(), DocumentPropertiesFragment.TAG);
                return true;

            case R.id.action_jump_to_page:
                new JumpToPageFragment()
                        .show(getSupportFragmentManager(), JumpToPageFragment.TAG);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
