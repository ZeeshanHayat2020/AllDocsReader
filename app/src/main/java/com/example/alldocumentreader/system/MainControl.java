/*
 * 文件名称:          AppControl.java
 *
 * 编译器:            android2.2
 * 时间:              下午7:17:16
 */
package com.example.alldocumentreader.system;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.alldocumentreader.common.ICustomDialog;
import com.example.alldocumentreader.common.IOfficeToPicture;
import com.example.alldocumentreader.common.ISlideShow;
import com.example.alldocumentreader.common.picture.PictureKit;
import com.example.alldocumentreader.constant.EventConstant;
import com.example.alldocumentreader.constant.MainConstant;
import com.example.alldocumentreader.fc.doc.TXTKit;
import com.example.alldocumentreader.fc.pdf.PDFLib;
import com.example.alldocumentreader.pdf.PDFControl;
import com.example.alldocumentreader.pg.control.PGControl;
import com.example.alldocumentreader.pg.model.PGModel;
import com.example.alldocumentreader.simpletext.model.IDocument;
import com.example.alldocumentreader.ss.control.SSControl;
import com.example.alldocumentreader.ss.model.baseModel.Workbook;
import com.example.alldocumentreader.wp.control.WPControl;

import java.lang.reflect.Method;

/**
 * application control
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-5-14
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public class MainControl extends AbstractControl {

    /**
     *
     */
    public MainControl(IMainFrame frame) {
        this.frame = frame;
        uncaught = new AUncaughtExceptionHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(uncaught);
        sysKit = new SysKit(this);
        init();
    }

    /**
     *
     */
    private void init() {
        // listener
        initListener();
        //
        toast = Toast.makeText(getActivity().getApplicationContext(), "", 0);

        // 自动测试
        Intent intent = getActivity().getIntent();
        String autoTest = intent.getStringExtra("autoTest");
        isAutoTest = autoTest != null && autoTest.equals("true");

    }

    /**
     *
     */
    private void initListener() {
        /**
         *
         */
        onKeyListener = new DialogInterface.OnKeyListener() {

            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    isCancel = true;
                    if (reader != null) {
                        reader.abortReader();
                        reader.dispose();
                    }
                    //getMainFrame().destroyEngine();
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        };

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (isCancel) {
                    return;
                }
                final Message message = msg;
                switch (msg.what) {
                    case MainConstant.HANDLER_MESSAGE_SUCCESS:
                        post(new Runnable() {
                            public void run() {
                                try {
                                    if (getMainFrame().isShowProgressBar()) {
                                        dismissProgressDialog();
                                    } else {
                                        if (customDialog != null) {
                                            customDialog.dismissDialog(ICustomDialog.DIALOGTYPE_LOADING);
                                        }
                                    }
                                    createApplication(message.obj);
                                } catch (Exception e) {
                                    sysKit.getErrorKit().writerLog(e, true);
                                }
                            }
                        });
                        break;

                    case MainConstant.HANDLER_MESSAGE_ERROR:
                        post(new Runnable() {
                            public void run() {
                                dismissProgressDialog();
                                if (message.obj instanceof Throwable) {
                                    sysKit.getErrorKit().writerLog((Throwable) message.obj, true);
                                }
                            }
                        });
                        break;

                    case MainConstant.HANDLER_MESSAGE_SHOW_PROGRESS:
                        if (getMainFrame().isShowProgressBar()) {
                            post(new Runnable() {
                                public void run() {
                                    progressDialog = ProgressDialog.show(getActivity(),
                                            frame.getAppName(), frame.getLocalString("DIALOG_LOADING"),
                                            false, false, null);
                                    progressDialog.setOnKeyListener(onKeyListener);
                                }
                            });
                        } else {
                            if (customDialog != null) {
                                customDialog.showDialog(ICustomDialog.DIALOGTYPE_LOADING);
                            }
                        }

                        break;

                    case MainConstant.HANDLER_MESSAGE_DISMISS_PROGRESS:
                        post(new Runnable() {
                            public void run() {
                                dismissProgressDialog();
                            }
                        });
                        break;

                    case MainConstant.HANDLER_MESSAGE_SEND_READER_INSTANCE:
                        reader = (IReader) msg.obj;
                        break;
                }
            }
        };
    }

    /**
     * dismiss progress dialog
     */
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     *
     */
    private void createApplication(Object obj) throws Exception {
        if (obj == null) {
            throw new Exception("Document with password");
        }

        // word
        if (applicationType == MainConstant.APPLICATION_TYPE_WP) {
            appControl = new WPControl(this, (IDocument) obj, filePath);
        }
        // excel
        else if (applicationType == MainConstant.APPLICATION_TYPE_SS) {
            appControl = new SSControl(this, (Workbook) obj, filePath);
        }
        // powerpoint
        else if (applicationType == MainConstant.APPLICATION_TYPE_PPT) {
            appControl = new PGControl(this, (PGModel) obj, filePath);
        }
        // PDF
        else if (applicationType == MainConstant.APPLICATION_TYPE_PDF) {
            appControl = new PDFControl(this, (PDFLib) obj, filePath);
        }

        View view = appControl.getView();
        if (view != null) {
            Object bg = frame.getViewBackground();
            if (bg != null) {
                if (bg instanceof Integer) {
                    view.setBackgroundColor((Integer) bg);
                } else if (bg instanceof Drawable) {
                    view.setBackgroundDrawable((Drawable) bg);
                }
            }
        }

        final boolean hassPassword = applicationType == MainConstant.APPLICATION_TYPE_PDF && ((PDFLib) obj).hasPasswordSync();
        if (applicationType == MainConstant.APPLICATION_TYPE_PDF) {
            if (!hassPassword) {
                /*handler.post(new Runnable()
                {
                    
                    @ Override
                    public void run()
                    {
                        frame.openFileFinish();
                    }
                });*/
                frame.openFileFinish();
            }
        } else {
            frame.openFileFinish();
        }

        PictureKit.instance().setDrawPictrue(true);

        //use a handler as easiest method to post a Runnable Delayed.
        //we cannot check hardware-acceleration directly as it will return reasonable results after attached to Window.
        handler.post(new Runnable() {

            @Override
            public void run() {
                //now lets check for HardwareAcceleration since it is only avaliable since ICS.
                // 14 = ICS_VERSION_CODE
                if (android.os.Build.VERSION.SDK_INT >= 11) {
                    try {
                        View contentView = getView();
                        //use reflection to get that Method
                        Method isHardwareAccelerated = contentView.getClass().getMethod("isHardwareAccelerated", null);
                        Object o = isHardwareAccelerated.invoke(contentView, null);
                        if (null != o && o instanceof Boolean && (Boolean) o) {
                            //ok we're shure that HardwareAcceleration is on.
                            //Now Try to switch it off:
                            Method setLayerType = contentView.getClass().getMethod("setLayerType", int.class, android.graphics.Paint.class);
                            int LAYER_TYPE_SOFTWARE = contentView.getClass().getField("LAYER_TYPE_SOFTWARE").getInt(null);
                            setLayerType.invoke(contentView, LAYER_TYPE_SOFTWARE, null);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, false);
                // 初始化数
                actionEvent(EventConstant.SYS_INIT_ID, null);
                // 更新状态
                if (applicationType == MainConstant.APPLICATION_TYPE_PDF) {
                    if (!hassPassword) {
                        frame.updateToolsbarStatus();
                    }
                } else {
                    frame.updateToolsbarStatus();
                }
                //
                getView().postInvalidate();
            }
        });       
        /*// 初始化数
        actionEvent(EventConstant.SYS_INIT_ID, null);
        // 更新状态
        frame.updateToolsbarStatus();*/

    }

    /**
     *
     */
    public boolean openFile(String filePath) {
        this.filePath = filePath;
        String fileName = filePath.toLowerCase();
        // word
        if (fileName.endsWith(MainConstant.FILE_TYPE_DOC)
                || fileName.endsWith(MainConstant.FILE_TYPE_DOCX)
                || fileName.endsWith(MainConstant.FILE_TYPE_TXT)
                || fileName.endsWith(MainConstant.FILE_TYPE_DOT)
                || fileName.endsWith(MainConstant.FILE_TYPE_DOTX)
                || fileName.endsWith(MainConstant.FILE_TYPE_DOTM)) {
            applicationType = MainConstant.APPLICATION_TYPE_WP;
        }
        // excel
        else if (fileName.endsWith(MainConstant.FILE_TYPE_XLS)
                || fileName.endsWith(MainConstant.FILE_TYPE_XLSX)
                || fileName.endsWith(MainConstant.FILE_TYPE_XLT)
                || fileName.endsWith(MainConstant.FILE_TYPE_XLTX)
                || fileName.endsWith(MainConstant.FILE_TYPE_XLTM)
                || fileName.endsWith(MainConstant.FILE_TYPE_XLSM)) {
            applicationType = MainConstant.APPLICATION_TYPE_SS;
        }
        // PowerPoint
        else if (fileName.endsWith(MainConstant.FILE_TYPE_PPT)
                || fileName.endsWith(MainConstant.FILE_TYPE_PPTX)
                || fileName.endsWith(MainConstant.FILE_TYPE_POT)
                || fileName.endsWith(MainConstant.FILE_TYPE_PPTM)
                || fileName.endsWith(MainConstant.FILE_TYPE_POTX)
                || fileName.endsWith(MainConstant.FILE_TYPE_POTM)) {
            applicationType = MainConstant.APPLICATION_TYPE_PPT;
        }
        // PDF document
        else if (fileName.endsWith(MainConstant.FILE_TYPE_PDF)) {
            applicationType = MainConstant.APPLICATION_TYPE_PDF;
        } else {
            applicationType = MainConstant.APPLICATION_TYPE_WP;
        }

        boolean isSupport = FileKit.instance().isSupport(fileName);
        // txt or no support
        if (fileName.endsWith(MainConstant.FILE_TYPE_TXT)
                || !isSupport) {
            TXTKit.instance().readText(this, handler, filePath);
        } else {
            /*if (applicationType == MainConstant.APPLICATION_TYPE_PDF)
            {
                try                
                {
                    reader = new PDFReader(this, filePath);
                    createApplication(reader.getModel());
                }
                catch (Exception e)
                {
                    ErrorUtil.instance().writerLog(e, true);
                }
            }
            else*/
            {
                new FileReaderThread(this, handler, filePath, null).start();
            }
        }
        return true;
    }

    /**
     * 布局视图
     *
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void layoutView(int x, int y, int w, int h) {
        //appControl.layoutView(x, y, w, h);
    }

    /**
     * action派发
     *
     * @param actionID 动作ID
     * @param obj      动作ID的Value
     */
    public void actionEvent(int actionID, final Object obj) {
        if (actionID == EventConstant.SYS_READER_FINSH_ID) {
            if (reader != null) {
                if (appControl != null) {
                    appControl.actionEvent(actionID, obj);
                }

                reader.dispose();
                reader = null;
            }
        }
        if (frame == null || frame.doActionEvent(actionID, obj)) {
            return;
        }
        switch (actionID) {
            case MainConstant.HANDLER_MESSAGE_SUCCESS: {
                try {
                    Message msg = new Message();
                    msg.obj = obj;
                    reader.dispose();
                    msg.what = MainConstant.HANDLER_MESSAGE_SUCCESS;

                    handler.handleMessage(msg);
                } catch (Throwable e) {
                    sysKit.getErrorKit().writerLog(e);
                }
            }
            break;
            case EventConstant.SYS_SET_PROGRESS_BAR_ID:
                if (handler != null) {
                    handler.post(new Runnable() {
                        /**
                         *
                         */
                        public void run() {
                            if (!isDispose) {
//                                frame.getActivity().setProgressBarIndeterminateVisibility((Boolean)obj);
                                frame.showProgressBar((Boolean) obj);
                            }
                        }
                    });
                }
                break;
            case EventConstant.TEST_REPAINT_ID:
                getView().postInvalidate();
                break;

            case EventConstant.SYS_SHOW_TOOLTIP:
                if (obj != null && obj instanceof String) {
                    toast.setText((String) obj);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;

            case EventConstant.SYS_CLOSE_TOOLTIP:
                toast.cancel();
                break;

            case EventConstant.APP_CONTENT_SELECTED:  //selected interesting content
                appControl.actionEvent(actionID, obj);
                frame.updateToolsbarStatus();
                break;

            case EventConstant.APP_ABORTREADING:
                if (reader != null) {
                    reader.abortReader();
                }
                break;

            case EventConstant.SYS_START_BACK_READER_ID:
                if (handler != null) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (!isDispose) {
//                                frame.getActivity().setProgressBarIndeterminateVisibility(true);
                                frame.showProgressBar(true);
                            }
                        }
                    });
                }
                break;

            case EventConstant.SYS_READER_FINSH_ID:
                if (handler != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!isDispose) {
//                                frame.getActivity().setProgressBarIndeterminateVisibility(false);
                                frame.showProgressBar(false);
                            }
                        }
                    });
                }
                break;

            case EventConstant.TXT_DIALOG_FINISH_ID:
                TXTKit.instance().reopenFile(this, handler, filePath, (String) obj);
                break;

            case EventConstant.TXT_REOPNE_ID:
                String[] strings = (String[]) obj;
                if (strings.length == 2) {
                    this.filePath = strings[0];
                    applicationType = MainConstant.APPLICATION_TYPE_WP;
                    TXTKit.instance().reopenFile(this, handler, filePath, strings[1]);
                }
                break;

            default:
                if (appControl != null) {
                    appControl.actionEvent(actionID, obj);
                }
        }
    }

    /**
     *
     */
    public IFind getFind() {
        return appControl.getFind();
    }


    /**
     * 得到action的状态
     *
     * @return obj
     */
    public Object getActionValue(int actionID, Object obj) {
        switch (actionID) {
            case EventConstant.SYS_FILEPAHT_ID: // 文件路径
                return filePath;

            default:
                break;
        }
        if (appControl == null) {
            return null;
        }

        Object ret = null;
        if (actionID == EventConstant.APP_THUMBNAIL_ID
                || actionID == EventConstant.WP_PAGE_TO_IMAGE
                || actionID == EventConstant.APP_PAGEAREA_TO_IMAGE
                || actionID == EventConstant.PG_SLIDE_TO_IMAGE
                || actionID == EventConstant.PG_SLIDESHOW_SLIDESHOWTOIMAGE) {
            boolean b = PictureKit.instance().isDrawPictrue();
            boolean isThumbnail = frame.isThumbnail();
            PictureKit.instance().setDrawPictrue(true);
            if (actionID == EventConstant.APP_THUMBNAIL_ID) {
                frame.setThumbnail(true);
            }
            ret = appControl.getActionValue(actionID, obj);
            if (actionID == EventConstant.APP_THUMBNAIL_ID) {
                frame.setThumbnail(isThumbnail);
            }
            PictureKit.instance().setDrawPictrue(b);
        } else {
            ret = appControl.getActionValue(actionID, obj);
        }

        return ret;
    }


    /**
     * 获取应用组件
     */
    public View getView() {
        if (appControl == null) {
            return null;
        }
        return appControl.getView();
    }

    /**
     *
     */
    public Dialog getDialog(Activity activity, int id) {
        return null;
    }

    /**
     *
     */
    public boolean isAutoTest() {
        return isAutoTest;
    }

    /**
     *
     */
    public IMainFrame getMainFrame() {
        return frame;
    }

    /**
     *
     */
    public Activity getActivity() {
        return frame.getActivity();
    }

    /**
     *
     */
    public IOfficeToPicture getOfficeToPicture() {
        return officeToPicture;
    }

    /**
     *
     */
    public ICustomDialog getCustomDialog() {
        return customDialog;
    }

    /**
     * @return
     */
    public ISlideShow getSlideShow() {
        return slideShow;
    }

    /**
     *
     */
    public IReader getReader() {
        return reader;
    }


    /**
     *
     */
    public byte getApplicationType() {
        return applicationType;
    }


    /**
     * set OfficeToPicture instance
     */
    public void setOffictToPicture(IOfficeToPicture opt) {
        officeToPicture = opt;
    }

    /**
     *
     */
    public void setCustomDialog(ICustomDialog dlg) {
        this.customDialog = dlg;
    }

    /**
     * @param slideshow
     */
    public void setSlideShow(ISlideShow slideshow) {
        this.slideShow = slideshow;
    }

    /**
     *
     */
    public SysKit getSysKit() {
        return this.sysKit;
    }

    /**
     * current view index
     *
     * @return
     */
    public int getCurrentViewIndex() {
        return appControl.getCurrentViewIndex();
    }

    /**
     *
     */
    public void dispose() {
        isDispose = true;
        if (appControl != null) {
            appControl.dispose();
            appControl = null;
        }
        if (reader != null) {
            reader.dispose();
            reader = null;
        }
        if (officeToPicture != null) {
            officeToPicture.dispose();
            officeToPicture = null;
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        if (customDialog != null) {
            customDialog = null;
        }

        if (slideShow != null) {
            slideShow = null;
        }

        frame = null;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (uncaught != null) {
            uncaught.dispose();
            uncaught = null;
        }
        onKeyListener = null;
        toast = null;
        filePath = null;
        //disposeStatic();
        System.gc();
        if (sysKit != null) {
            sysKit.dispose();
        }
    }

    /**
     *
     */
    /*private void disposeStatic()
    {
        PictureManage.instance().dispose();
        HyperlinkManage.instance().dispose();
        ListManage.instance().dispose();
        //SysKit.instance().setControl(null);
        PGBulletText.instance().dispose();
        ViewFactory.dispose();
    }*/

    private boolean isDispose;
    //
    private boolean isCancel;
    // 自动化测试
    private boolean isAutoTest;
    //
    private byte applicationType = -1;
    // 文件路径
    private String filePath;
    //
    private IMainFrame frame;
    //
    private IOfficeToPicture officeToPicture;
    //
    private ICustomDialog customDialog;
    //
    private ISlideShow slideShow;
    //
    private IReader reader;
    //toast
    private Toast toast;
    // 
    private ProgressDialog progressDialog;
    //
    private DialogInterface.OnKeyListener onKeyListener;
    //
    private Handler handler;
    //
    private IControl appControl;
    //
    public SysKit sysKit;
    //
    private AUncaughtExceptionHandler uncaught;
}
