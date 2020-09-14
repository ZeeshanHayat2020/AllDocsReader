/*
 * 文件名称:          	SSEditor.java
 *  
 * 编译器:            android2.2
 * 时间:             	上午4:13:44
 */
package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.ss.control;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.common.shape.IShape;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.constant.MainConstant;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.java.awt.Rectangle;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.pg.animate.IAnimation;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.simpletext.control.IHighlight;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.simpletext.control.IWord;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.simpletext.model.IDocument;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.system.IControl;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        	Office engine V1.0
 * <p>
 * 作者:            	ljj8494
 * <p>
 * 日期:            	2013-3-22
 * <p>
 * 负责人:          	ljj8494
 * <p>
 * 负责小组:        	TMC
 * <p>
 * <p>
 */
public class SSEditor implements IWord
{

    public SSEditor(Spreadsheet ss)
    {
        this.ss = ss;
    }
    
    public IHighlight getHighlight()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IDocument getDocument()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getText(long start, long end)
    {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public long viewToModel(int x, int y, boolean isBack)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte getEditType()
    {
        return MainConstant.APPLICATION_TYPE_SS;
    }

    @Override
    public IAnimation getParagraphAnimation(int pargraphID)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IShape getTextBox()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IControl getControl()
    {
        // TODO Auto-generated method stub
        return ss.getControl();
    }

    @Override
    public void dispose()
    {
        ss = null;
    }
    
    private Spreadsheet ss;
}
