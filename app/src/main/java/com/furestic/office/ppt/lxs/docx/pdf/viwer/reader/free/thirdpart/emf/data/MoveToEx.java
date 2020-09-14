// Copyright 2002, FreeHEP.

package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import android.graphics.Point;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.java.awt.geom.GeneralPath;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFRenderer;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * MoveToEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: MoveToEx.java 10367 2007-01-22 19:26:48Z duns $
 */
public class MoveToEx extends EMFTag
{

    private Point point;

    public MoveToEx()
    {
        super(27, 1);
    }

    public MoveToEx(Point point)
    {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new MoveToEx(emf.readPOINTL());
    }

    public String toString()
    {
        return super.toString() + "\n  point: " + point;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // The MoveToEx function updates the current position to the
        // specified point
        // and optionally returns the previous position.
        GeneralPath currentFigure = new GeneralPath(renderer.getWindingRule());
        currentFigure.moveTo((float)point.x, (float)point.y);
        renderer.setFigure(currentFigure);
    }
}
