// Copyright 2002, FreeHEP.

package com.example.alldocumentreader.thirdpart.emf.data;

import com.example.alldocumentreader.java.awt.Color;
import com.example.alldocumentreader.thirdpart.emf.EMFInputStream;
import com.example.alldocumentreader.thirdpart.emf.EMFRenderer;

import java.io.IOException;

/**
 * EMF LogPen
 * 
 * @author Mark Donszelmann
 * @version $Id: LogPen.java 10515 2007-02-06 18:42:34Z duns $
 */
public class LogPen extends AbstractPen
{

    private int penStyle;

    private int width;

    private Color color;

    public LogPen(int penStyle, int width, Color color)
    {
        this.penStyle = penStyle;
        this.width = width;
        this.color = color;
    }

    public LogPen(EMFInputStream emf) throws IOException
    {
        penStyle = emf.readDWORD();
        width = emf.readDWORD();
        /* int y = */emf.readDWORD();
        color = emf.readCOLORREF();
    }

    public String toString()
    {
        return "  LogPen\n" + "    penstyle: " + penStyle + "\n    width: " + width
            + "\n    color: " + color;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        renderer.setUseCreatePen(true);
        renderer.setPenPaint(color);
        renderer.setPenStroke(createStroke(renderer, penStyle, null, width));
    }
}
