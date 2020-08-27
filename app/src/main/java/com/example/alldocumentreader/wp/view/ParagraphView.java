/*
 * 文件名称:          ParagraphView.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:27:16
 */
package com.example.alldocumentreader.wp.view;

import android.graphics.Canvas;

import com.example.alldocumentreader.constant.wp.WPViewConstant;
import com.example.alldocumentreader.java.awt.Rectangle;
import com.example.alldocumentreader.objectpool.IMemObj;
import com.example.alldocumentreader.simpletext.model.IDocument;
import com.example.alldocumentreader.simpletext.model.IElement;
import com.example.alldocumentreader.simpletext.view.AbstractView;
import com.example.alldocumentreader.simpletext.view.IView;


/**
 * word 段落视图
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-20
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ParagraphView extends AbstractView implements IMemObj
{

    /**
     * 
     * @param elem
     */
    public ParagraphView(IElement elem)
    {
        this.elem = elem;
    }
    
    public String getText()
    {
        return elem.getText(null);
    }
    
    /**
     * 
     */
    public short getType()
    {
        return WPViewConstant.PARAGRAPH_VIEW;
    }
    
    /**
     * model到视图
     * @param offset 指定的offset
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack)
    {
        if (getChildView() == null)
        {
            buildLine();
        }
        IView view = getView(offset, WPViewConstant.LINE_VIEW, isBack);
        if (view != null)
        {
            view.modelToView(offset, rect, isBack);
        }
        rect.x += getX();
        rect.y += getY();
        return rect;        
    }    
    
    /**
     * @param x
     * @param y
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public long viewToModel(int x, int y, boolean isBack)
    {
        if (getChildView() == null)
        {
            buildLine();
        }
        x -= getX();
        y -= getY();
        //IView view = getView(x, y, WPViewConstant.LINE_VIEW, isBack);
        IView view = getChildView();
        if (view != null && y > view.getY())
        {
            while (view != null)
            {
                if (y >= view.getY() && y < view.getY() + view.getLayoutSpan(WPViewConstant.Y_AXIS))
                {
                    break;
                }
                view = view.getNextView();
            }
        }
        view = view == null ? getChildView() : view;
        if (view != null)
        {
            return view.viewToModel(x, y, isBack);
        }
        return -1;
    }
    
    /**
     * 
     * @param canvas
     * @param x
     * @param y
     * @param zoom
     */
    public void draw(Canvas canvas, int originX, int originY, float zoom)
    {
        if (getChildView() == null)
        {
            buildLine();
        }
        int dX = (int)(x * zoom) + originX;
        int dY = (int)(y * zoom) + originY;
        if (bnView != null)
        {
            bnView.draw(canvas, dX, dY, zoom);
        }
        super.draw(canvas, originX, originY, zoom);
    }
    
    /**
     * 
     */
    public void setBNView(BNView bnView)
    {
        this.bnView = bnView;
    }
    
    /**
     * 
     */
    public BNView getBNView()
    {
        return this.bnView;
    }

    
    /**
     * 
     */
    private void buildLine()
    {
        IDocument doc = getDocument();
        if (doc != null)
        {
            LayoutKit.instance().buildLine(doc, this);
        }
    }
    
    /**
     * 
     *
     */
    public void free()
    {
        /*IView temp = child;
        IView next;
        while (temp != null)
        {
            next = temp.getNextView();
            temp.free();
            temp = next;
        }
        child = null;
        if (bnView != null)
        {
            bnView.dispose();
            bnView = null;
        }*/
    }

    /**
     * 
     *
     */
    public IMemObj getCopy()
    {
        return null;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
        if (bnView != null)
        {
            bnView.dispose();
            bnView = null;
        }
    }
    
    /**
     * 
     */
    private BNView bnView;
}
