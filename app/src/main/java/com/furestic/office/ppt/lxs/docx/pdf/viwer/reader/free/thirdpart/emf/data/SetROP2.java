// Copyright 2002, FreeHEP.

package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFConstants;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFRenderer;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * SetROP2 TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetROP2.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetROP2 extends EMFTag implements EMFConstants
{

    private int mode;

    public SetROP2()
    {
        super(20, 1);
    }

    public SetROP2(int mode)
    {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetROP2(emf.readDWORD());
    }


    public String toString()
    {
        return super.toString() + "\n  mode: " + mode;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // The SetROP2 function sets the current foreground mix mode.
        // GDI uses the foreground mix mode to combine pens and interiors
        // of filled objects with the colors already on the screen. The
        // foreground mix mode defines how colors from the brush or pen
        // and the colors in the existing image are to be combined.
        renderer.setRop2(mode);
    }
}
