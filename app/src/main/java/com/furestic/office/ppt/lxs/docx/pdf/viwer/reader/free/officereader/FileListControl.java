/*
 * 文件名称:          FileListControl.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:29:01
 */
package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.officereader;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.system.AbstractControl;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.system.SysKit;

/**
 * file list control
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
public class FileListControl extends AbstractControl
{

    /**
     * 
     * @param activity
     */
    public FileListControl(AppCompatActivity activity)
    {
        this.activity = activity;
    }

    /**
     * 
     *
     */
    public void actionEvent(int actionID, Object obj)
    {
        ((FileListActivity)activity).actionEvent(actionID, obj);
    } 
    
    /**
     * 
     */
    public Activity getActivity()
    {
        return activity;
    }
    
    /**
     * 
     */
    public SysKit getSysKit()
    {
        if(sysKit == null)
        {
            sysKit = new SysKit(this);
        }
        return this.sysKit;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        activity = null;
        sysKit = null;
    }
    //
    private AppCompatActivity activity;
    private SysKit sysKit;
}
