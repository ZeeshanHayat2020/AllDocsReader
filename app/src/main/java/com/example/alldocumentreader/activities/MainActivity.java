package com.example.alldocumentreader.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alldocumentreader.R;
import com.example.alldocumentreader.adapters.AdapterAcMain;
import com.example.alldocumentreader.constant.Constant;
import com.example.alldocumentreader.interfaces.OnRecyclerItemClickLister;
import com.example.alldocumentreader.models.ModelAcMain;
import com.example.alldocumentreader.utils.RecyclerViewItemDecoration;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import java.util.ArrayList;

public class MainActivity extends ActivityBase implements OnRecyclerItemClickLister {

    private final String TAG = this.getClass().getName();
    private RelativeLayout acMainParentContainer;
    private Toolbar toolbar;
    private TextView toolBarTitleTv;
    private RecyclerView recyclerView;
    private AdapterAcMain adapterAcMain;
    private ArrayList<ModelAcMain> itemsList;
    private GridLayoutManager layoutManager;
    private AppUpdateManager appUpdateManager;

    ReviewManager reviewManager;
    ReviewInfo reviewInfo = null;
    Handler handler;


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
        setUpInAppUpdate();
        setUpInAppReview();

        Toast.makeText(
                MainActivity.this,
                "Toast Working",
                Toast.LENGTH_LONG
        ).show();

    }

    protected void onResume() {
        super.onResume();
        if (haveNetworkConnected(MainActivity.this)) {
            checkForUpdate();
        }
    }

    private void setUpInAppReview() {
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
        handler = new Handler(getMainLooper());
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
                "Word Files",
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
//        startActivity(intent);
        startActivityForResult(intent, Constant.REQUEST_CODE_IN_APP_REVIEW);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_CODE_FOR_IN_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                View parentLayout = findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar
                        .make(parentLayout, "Installation Failed!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        } else if (requestCode == Constant.REQUEST_CODE_IN_APP_REVIEW) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Return From FilesHolder", Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Task<Void> flow = reviewManager.launchReviewFlow(MainActivity.this, reviewInfo);
                        flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "Complete: Thanks for the feedback!",
                                        Toast.LENGTH_LONG
                                ).show();
                                Log.d(TAG, "onComplete: Thanks for the feedback!");
                            }
                        });
                        flow.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void result) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "Success: Reviewed successfully",
                                        Toast.LENGTH_LONG
                                ).show();
                                Log.d(TAG, "onSuccess:  Reviewed successfully");
                            }
                        });
                        flow.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "Failed: Reviewed Failed!",
                                        Toast.LENGTH_LONG
                                ).show();

                                Log.d(TAG, "onFailed:  Reviewed Failed!");
                            }
                        });
                    }
                }, 3000);
            }
        }
    }
}