/*
 * 文件名称:          ICustomDialog.java
 *  
 * 编译器:            android2.2
 * 时间:              下午12:47:14
 */
package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.common;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-12-21
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface ICustomDialog
{
    //password dialog
    public static final byte DIALOGTYPE_PASSWORD = 0;
    //txt encode dialog
    public static final byte DIALOGTYPE_ENCODE = 1;
    //loading dialog
    public static final byte DIALOGTYPE_LOADING = 2;
    //error dialog
    public static final byte DIALOGTYPE_ERROR = 3;
    //
    public static final byte DIALOGTYPE_FIND = 4;
    
    
    /**
     * 
     * @param type dialog type
     */
    public void showDialog(byte type);
    
    /**
     * 
     * @param type
     */
    public void dismissDialog(byte type);
}
