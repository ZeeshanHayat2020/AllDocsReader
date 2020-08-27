/*
 * 文件名称:          IWord.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:56:16
 */
package com.example.alldocumentreader.simpletext.control;

import com.example.alldocumentreader.common.shape.IShape;
import com.example.alldocumentreader.java.awt.Rectangle;
import com.example.alldocumentreader.pg.animate.IAnimation;
import com.example.alldocumentreader.simpletext.model.IDocument;
import com.example.alldocumentreader.system.IControl;

/**
 * IWord 接口
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-7-27
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IWord
{
    /**
     * @return Returns the highlight.
     */
    public IHighlight getHighlight();
    /**
     * 
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack);
    /**
     * 
     */
    public IDocument getDocument();
    /**
     * 
     */
    public String getText(long start, long end);
    
    /**
     * @param x 为100%的值
     * @param y 为100%的值
     */
    public long viewToModel(int x, int y, boolean isBack);
    /**
     * 
     */
    public byte getEditType();
    
    /**
     * 
     * @param para
     * @return
     */
    public IAnimation getParagraphAnimation(int pargraphID);
    
    /**
     * 
     */
    public IShape getTextBox();
    
    /**
     * 
     */
    public IControl getControl();
    
    /**
     * 
     */
    public void dispose();
}
