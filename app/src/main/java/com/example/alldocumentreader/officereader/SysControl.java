/*
 * 文件名称:          SysControl.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:15:54
 */
package com.example.alldocumentreader.officereader;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alldocumentreader.constant.EventConstant;
import com.example.alldocumentreader.system.AbstractControl;
import com.example.alldocumentreader.system.SysKit;

/**
 * sys control
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
public class SysControl extends AbstractControl
{
    
    /**
     * 
     * @param activity
     */
    public SysControl(AppCompatActivity activity)
    {
       this.activity = activity; 
       toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
    }

    /**
     * 
     *
     */
    public void actionEvent(int actionID, Object obj)
    {
        switch (actionID)
        {
            case EventConstant.SYS_SHOW_TOOLTIP:
                if (obj != null && obj instanceof String)
                {
                    toast.setText((String)obj);
                    toast.show();
                }
                break;
            case EventConstant.SYS_CLOSE_TOOLTIP:
                toast.cancel();
                break;
                
            case EventConstant.SYS_SEARCH_ID:
                activity.onSearchRequested();
                break;

            default:
                break;
        }
    }

    /**
     * 
     *
     */
    public View getView()
    {
        return ((SysActivity)activity).getSysFrame();
    }
    /**
     * 
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
    //toast
    private Toast toast;
    //
    private AppCompatActivity activity;
    private SysKit sysKit;
}

