/*
 * 文件名称:          PDFToolsbar.java
 *  
 * 编译器:            android2.2
 * 时间:              下午6:04:29
 */
package com.example.alldocumentreader.officereader.beans;

import android.content.Context;
import android.view.View;

import com.example.alldocumentreader.constant.EventConstant;
import com.example.alldocumentreader.R;
import com.example.alldocumentreader.system.IControl;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-9-19
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PDFToolsbar extends AToolsbar
{

    /**
     * 
     * @param context
     * @param control
     */
    public PDFToolsbar(Context content, IControl control)
    {
        super(content, control);
        init();
    }
    
    /**
     * 
     */
    private void init()
    {        
        // 查找
        addButton(R.drawable.app_find, R.drawable.app_find_disable, 
            R.string.app_toolsbar_find, EventConstant.APP_FIND_ID, true);
        
        // 选择文本
        //addButton(R.drawable.wp_select_text, R.drawable.wp_select_text_disable, 
        //    R.string.wp_toolsbar_select_text, EventConstant.WP_SELECT_TEXT_ID, true);
        
        // 分享
        addButton(R.drawable.file_share, R.drawable.file_share_disable, 
            R.string.file_toolsbar_share, EventConstant.APP_SHARE_ID, true);
        
        // 联网搜索
        //addButton(R.drawable.app_internet_search, R.drawable.app_internet_search_disable, 
        //    R.string.app_toolsbar_internet_search, EventConstant.APP_INTERNET_SEARCH_ID, true);
        
        // 标星
        addCheckButton(R.drawable.file_star_check, R.drawable.file_star_uncheck,R.drawable.file_star_disable,
            R.string.file_toolsbar_mark_star, R.string.file_toolsbar_unmark_star,
            EventConstant.FILE_MARK_STAR_ID, true);
        
        // 标签
        addButton(R.drawable.app_drawing, R.drawable.app_drawing_disable, 
            R.string.app_toolsbar_draw, EventConstant.APP_DRAW_ID, true);
        
        // 朗读
        /*addButton(R.drawable.app_read, R.drawable.app_read_disable, 
            R.string.app_toolsbar_read, EventConstant.APP_READ_ID, true);*/
        
        // 签批
        /*addButton(R.drawable.app_approve, R.drawable.app_approve_disable, 
            R.string.app_toolsbar_approve, EventConstant.APP_APPROVE_ID, true);*/
        
        // 生成图片 
        //addButton(R.drawable.app_approve, R.drawable.app_approve_disable, 
        //    R.string.app_toolsbar_generated_picture, EventConstant.APP_GENERATED_PICTURE_ID, true);
        
        // 帮助
        //addButton(R.drawable.file_help, -1, R.string.sys_menu_help, EventConstant.SYS_HELP_ID, false);
        
    }
    
    /**
     * 
     */
    public void updateStatus()
    {
        // 查找
        //setEnabled(EventConstant.APP_FIND_ID, true);
        
        // 选择文本
        //setEnabled(EventConstant.WP_SELECT_TEXT_ID, true);
        
        // 非文本文件才需要视图切换
        /*if (!((String)control.getActionValue(EventConstant.SYS_FILEPAHT_ID)).endsWith(MainConstant.FILE_TYPE_TXT))
        {
           // 视图切换
            setEnabled(EventConstant.WP_SWITCH_VIEW, true);
        }*/
        
        // 分享
        //setEnabled(EventConstant.APP_SHARE_ID, true);
        
        
        // 标星
        //setEnabled(EventConstant.FILE_MARK_STAR_ID, true);
        
        // 朗读
        //setEnabled(EventConstant.APP_READ_ID, true);
        
        // 签批
        //setEnabled( EventConstant.APP_APPROVE_ID, true);
        
        // 帮助
        //setEnabled(EventConstant.SYS_HELP_ID, true);
    }
    
    /**
     * 单击事件
     */
    public void onClick(View v)
    {
        if (v instanceof AImageButton)
        {
            AImageButton b = (AImageButton)v;
            control.actionEvent(b.getActionID(), null);
        }
    }
    
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
    }

}
