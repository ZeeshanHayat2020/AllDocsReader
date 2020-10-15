package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.furestic.alldocument.office.ppt.lxs.docx.pdf.viwer.reader.free.R;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.adapters.AdapterAcMain;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.constant.Constant;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.fc.util.IOUtils;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.interfaces.OnRecyclerItemClickLister;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.models.ModelAcMain;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.officereader.AppActivity;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.utils.FileUtils;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.utils.PathUtil;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.utils.RecyclerViewItemDecoration;
import com.google.android.gms.ads.AdListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends ActivityBase implements OnRecyclerItemClickLister {

    private final String TAG = this.getClass().getName();
    private RelativeLayout acMainParentContainer;
    private RelativeLayout acMainFilePicker;
    private Toolbar toolbar;
    private TextView toolBarTitleTv;
    private RecyclerView recyclerView;
    private AdapterAcMain adapterAcMain;
    private ArrayList<ModelAcMain> itemsList;
    private GridLayoutManager layoutManager;
    private AppUpdateManager appUpdateManager;

    /* ReviewManager reviewManager;
     ReviewInfo reviewInfo = null;*/
    Handler handler;

    private TextView tvStorageTotal;
    private TextView tvStorageFree;
    private ProgressBar progressBarStorage;
    private int reviewCounter = 0;
    private int adCounter = 0;
    private String fileName;


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
        displayingStorageOfDevice();
        setUpRecyclerView();
        setUpInAppUpdate();
//        setUpInAppReview();
        reqNewInterstitial(this);


    }

    protected void onResume() {
        super.onResume();
        if (haveNetworkConnected(MainActivity.this)) {
            checkForUpdate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);

        ImageView itemPurchase = (ImageView) menu.findItem(R.id.menu_acMain_noAdd).getActionView();
        if (!myPreferences.isItemPurchased()) {
            itemPurchase.setImageResource(R.drawable.ic_in_app_purchase);
            Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_blink);
            itemPurchase.startAnimation(startAnimation);
            itemPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bp.purchase(MainActivity.this, getResources().getString(R.string.producti_id));
                    Log.d(TAG, "onOptionsItemSelected: ItemPurchase");
                }
            });
        } else {
            itemPurchase.setImageResource(0);
            itemPurchase.setVisibility(View.GONE);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_acMain_rateUs: {
                rateUs();
            }
            break;
            case R.id.menu_acMain_shareUs: {
                shareUs();
            }
            break;
            case R.id.menu_acMain_changeLanguage: {
                startActivity(new Intent(this, ActivityLanguage.class));
                this.finish();
            }
            break;
            default: {
                super.onOptionsItemSelected(item);
            }
        }
        return false;
    }

    /*  private void setUpInAppReview() {
          reviewManager = new FakeReviewManager(MainActivity.this);
          Task<ReviewInfo> request = reviewManager.requestReviewFlow();
          request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
              @Override
              public void onComplete(@NonNull Task<ReviewInfo> task) {
                  if (task.isSuccessful()) {
                      // We can get the ReviewInfo object
                      reviewInfo = task.getResult();
                  } else {
                      // There was some problem, continue regardless of the result.
                      reviewInfo = null;
                  }
              }
          });

      }
  */
    private void setUpInAppUpdate() {
        appUpdateManager = (AppUpdateManager) AppUpdateManagerFactory.create(this);
        // Returns an intent object that you use to check for an update.
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                appUpdateInfo,
                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                AppUpdateType.IMMEDIATE,
                                // The current activity making the update request.
                                MainActivity.this,
                                // Include a request code to later monitor this update request.
                                Constant.REQUEST_CODE_FOR_IN_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void checkForUpdate() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    // If an in-app update is already running, resume the update.
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                AppUpdateType.IMMEDIATE,
                                MainActivity.this,
                                Constant.REQUEST_CODE_FOR_IN_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void initViews() {
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.acMain_toolbar);
        acMainParentContainer = (RelativeLayout) findViewById(R.id.acMain_parentContainer);
        recyclerView = findViewById(R.id.acMain_RecyclerView);
        tvStorageTotal = (TextView) findViewById(R.id.acMain_storage_totalSpaceTv);
        tvStorageFree = (TextView) findViewById(R.id.acMain_storage_freeSpaceTv);
        progressBarStorage = (ProgressBar) findViewById(R.id.acMain_storage_progressBar);
        acMainFilePicker = (RelativeLayout) findViewById(R.id.acMain_filePicker);
        acMainFilePicker.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          /*  Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("application/pdf");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            startActivityForResult(chooseFile, Constant.REQUEST_CODE_PICK_FILE);*/

         /* <data android:mimeType="application/msword" />
                <data android:mimeType="application/vnd.openxmlformats-officedocument.wordprocessingml.document" />
                <data android:mimeType="application/vnd.ms-powerpoint" />
                <data android:mimeType="application/vnd.openxmlformats-officedocument.presentationml.presentation" />
                <data android:mimeType="application/vnd.ms-excel" />
                <data android:mimeType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" />
                <data android:mimeType="text/plain" />*/
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            String[] mimetypes = {
                    "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/vnd.ms-powerpoint",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "text/plain"
            };
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, Constant.REQUEST_CODE_PICK_FILE);
        }
    };

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        toolBarTitleTv = (TextView) findViewById(R.id.toolBar_title_tv);
        toolBarTitleTv.setText(getResources().getString(R.string.toolBartxtMain));
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_Red));
    }


    private void loadRecyclerViewItems() {
        int[] imgIds = {
                R.drawable.ic_all_docs,
                R.drawable.ic_pdf_docs,
                R.drawable.ic_word_docs,
                R.drawable.ic_txt_docs,
                R.drawable.ic_ppt_docs,
                R.drawable.ic_html_docs,
                R.drawable.ic_xml_docs,
                R.drawable.ic_sheet_docs,
                R.drawable.ic_rtf_docs

        };
        String[] itemNames = {
                getString(R.string.allDocs),
                getString(R.string.pdf_files),
                getString(R.string.word_files),
                getString(R.string.txtFiles),
                getString(R.string.ppt_files),
                getString(R.string.html_files),
                getString(R.string.xmlFiles),
                getString(R.string.sheet_files),
                getString(R.string.rtf_files)
        };

        itemsList = new ArrayList<>();
        for (int i = 0; i < itemNames.length; i++) {
            itemsList.add(new ModelAcMain(itemNames[i], imgIds[i]));
        }
    }

    private void setUpRecyclerView() {
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._1sdp);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(spacingInPixels));
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 3);
        adapterAcMain = new AdapterAcMain(this, itemsList);
        adapterAcMain.notifyDataSetChanged();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterAcMain);
        adapterAcMain.setOnItemClickListener(this);
    }

    public void intentToFilesHolder(String exString) {
        if (hasStoragePermission()) {
            final Intent intent = new Intent(this, ActivityFilesHolder.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constant.KEY_SELECTED_FILE_FORMAT, exString);
            if (adCounter > 2) {
                adCounter = 0;
                if (mInterstitialAd.isLoaded() && !myPreferences.isItemPurchased()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            startActivityForResult(intent, Constant.REQUEST_CODE_IN_APP_REVIEW);

                        }
                    });
                } else {
                    reqNewInterstitial(this);
                    startActivityForResult(intent, Constant.REQUEST_CODE_IN_APP_REVIEW);
                }
            } else {
                startActivityForResult(intent, Constant.REQUEST_CODE_IN_APP_REVIEW);
            }
        } else {
            checkStoragePermission();
        }


    }

    public static boolean haveNetworkConnected(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }


    private void displayingStorageOfDevice() {

        float totalSpace = DeviceMemory.getInternalStorageSpace();
        float occupiedSpace = DeviceMemory.getInternalUsedSpace();
        float freeSpace1 = DeviceMemory.getInternalFreeSpace();
        DecimalFormat outputFormat = new DecimalFormat("#.##");

        String total = getResources().getString(R.string.total_space);
        String free = getResources().getString(R.string.free_space);
        tvStorageFree.setText(free + outputFormat.format(freeSpace1) + " GB");
        tvStorageTotal.setText(total + outputFormat.format(totalSpace) + " GB");
        progressBarStorage.setMax((int) totalSpace);
        progressBarStorage.setProgress((int) occupiedSpace);

    }

    public static class DeviceMemory {
        public static float getInternalStorageSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            float total = ((float) statFs.getBlockCount() * statFs.getBlockSize()) / 1073741824;
            return total;
        }

        public static float getInternalFreeSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            float free = ((float) statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1073741824;
            return free;
        }

        public static float getInternalUsedSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            float total = ((float) statFs.getBlockCount() * statFs.getBlockSize()) / 1073741824;
            float free = ((float) statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1073741824;
            float busy = total - free;
            return busy;
        }
    }

    @Override
    public void onItemClicked(int position) {
        adCounter++;
        switch (position) {
            case 0: {
                intentToFilesHolder(getString(R.string.allDocs));
            }
            break;
            case 1: {
                intentToFilesHolder(getString(R.string.pdf_files));
            }
            break;
            case 2: {
                intentToFilesHolder(getString(R.string.word_files));
            }
            break;
            case 3: {
                intentToFilesHolder(getString(R.string.txtFiles));
            }
            break;
            case 4: {
                intentToFilesHolder(getString(R.string.ppt_files));
            }
            break;
            case 5: {
                intentToFilesHolder(getString(R.string.html_files));
            }
            break;
            case 6: {
                intentToFilesHolder(getString(R.string.xmlFiles));
            }
            break;
            case 7: {
                intentToFilesHolder(getString(R.string.sheet_files));
            }
            break;
            case 8: {
                intentToFilesHolder(getString(R.string.rtf_files));
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
                .make(view, "Permission denied, Please allow this app to read external storage.", Snackbar.LENGTH_LONG)
                .setAction("Allow", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!hasStoragePermission()) {
                            checkStoragePermission();
                        }
                    }
                });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorWhite));
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

    public String getFilePathFromExternalAppsURI(Context context, Uri contentUri) {
        fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(getFilesDir(), fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return fileName;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_CODE_FOR_IN_APP_UPDATE) {
            if (resultCode == RESULT_CANCELED) {
                this.finish();
            } else if (resultCode == RESULT_OK) {
                View parentLayout = findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar
                        .make(parentLayout, "Application updated successfully.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        } else if (requestCode == Constant.REQUEST_CODE_IN_APP_REVIEW) {
            if (resultCode == RESULT_OK) {
                /*reviewCounter++;
                if (reviewCounter > 4) {
                    reviewCounter = 0;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Task<Void> flow = reviewManager.launchReviewFlow(MainActivity.this, reviewInfo);
                            flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "onComplete: Thanks for the feedback!");
                                }
                            });
                            flow.addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void result) {
                                    Log.d(TAG, "onSuccess:  Reviewed successfully");
                                }
                            });
                            flow.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Log.d(TAG, "onFailed:  Reviewed Failed!");
                                }
                            });
                        }
                    }, 3000);
                }*/
            }
        } else if (requestCode == Constant.REQUEST_CODE_PICK_FILE) {
            if (RESULT_OK == resultCode) {
                Uri uri = data.getData();

                String filePath = "";
                filePath = FileUtils.getPath(MainActivity.this, uri);
                if (!filePath.equals("")) {
                    File file = new File(filePath);
                    fileName = file.getName();
                    if (file.getName().endsWith("pdf")) {
                        final Intent intent = new Intent(MainActivity.this, ActivityPdfViewer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(Constant.KEY_SELECTED_FILE_URI, filePath);
                        intent.putExtra(Constant.KEY_SELECTED_FILE_NAME, fileName);
                        startActivity(intent);
                    } else {
                        final Intent intent = new Intent(MainActivity.this, AppActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(Constant.KEY_SELECTED_FILE_URI, filePath);
                        intent.putExtra(Constant.KEY_SELECTED_FILE_NAME, fileName);
                        startActivity(intent);
                    }
                }
                Log.d(TAG, "onActivityResult: FILE URI:" + uri);
                Log.d(TAG, "onActivityResult: FILE PATH:" + filePath);
            } else {
                Toast.makeText(this, "No file selected.", Toast.LENGTH_SHORT).show();

            }
        }
    }
}