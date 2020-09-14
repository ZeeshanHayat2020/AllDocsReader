// Copyright 2002, FreeHEP.

package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFConstants;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFRenderer;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * SetBkMode TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetBkMode.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetBkMode extends EMFTag implements EMFConstants
{

    private int mode;

    public SetBkMode()
    {
        super(18, 1);
    }

    public SetBkMode(int mode)
    {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetBkMode(emf.readDWORD());
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
        // The SetBkMode function affects the line styles for lines drawn using a
        // pen created by the CreatePen function. SetBkMode does not affect lines
        // drawn using a pen created by the ExtCreatePen function.
        renderer.setBkMode(mode);
    }
}
