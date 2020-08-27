/*
 * 文件名称:          SettingControl.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:07:59
 */
package com.example.alldocumentreader.officereader;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alldocumentreader.constant.EventConstant;
import com.example.alldocumentreader.officereader.database.DBService;
import com.example.alldocumentreader.system.AbstractControl;
import com.example.alldocumentreader.system.SysKit;

/**
 * setting control
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
public class SettingControl extends AbstractControl
{
    /**
     * 
     * @param activity
     */
    public SettingControl(AppCompatActivity activity)
    {
       this.activity = activity; 
       dbService = new DBService(activity);
    }

    /**
     * 
     *
     */
    public void actionEvent(int actionID, Object obj)
    {
        switch(actionID)
        {
            case EventConstant.SYS_SET_MAX_RECENT_NUMBER:
                if (obj != null)
                {
                    dbService.changeRecentCount((Integer)obj);
                }
                break;
           default:
               break;
        }       
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
     * @return
     */
    public int getRecentMax()
    {   
        return dbService == null ? 0 : dbService.getRecentMax();
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
        if (dbService != null)
        {
            dbService.dispose();
            dbService = null;
        }
        sysKit = null;
    }
    //
    private AppCompatActivity activity;
    //
    public DBService dbService;
    private SysKit sysKit;

}
