// Copyright 2002, FreeHEP.

package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import android.graphics.Point;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFRenderer;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * SetViewportOrgEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetViewportOrgEx.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetViewportOrgEx extends EMFTag
{

    private Point point;

    public SetViewportOrgEx()
    {
        super(12, 1);
    }

    public SetViewportOrgEx(Point point)
    {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetViewportOrgEx(emf.readPOINTL());
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
        // The SetViewportOrgEx function specifies which device point maps
        // to the viewport origin (0,0).

        // This function (along with SetViewportExtEx and SetWindowExtEx) helps
        // define the mapping from the logical coordinate space (also known as a
        // window) to the device coordinate space (the viewport). SetViewportOrgEx
        // specifies which device point maps to the logical point (0,0). It has the
        // effect of shifting the axes so that the logical point (0,0) no longer
        // refers to the upper-left corner.
        renderer.setViewportOrigin(point);
        //renderer.resetTransformation();
    }
}
