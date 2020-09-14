// Copyright 2002, FreeHEP.

package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFRenderer;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * RestoreDC TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: RestoreDC.java 10367 2007-01-22 19:26:48Z duns $
 */
public class RestoreDC extends EMFTag
{

    private int savedDC = -1;

    public RestoreDC()
    {
        super(34, 1);
    }

    public RestoreDC(int savedDC)
    {
        this();
        this.savedDC = savedDC;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new RestoreDC(emf.readDWORD());
    }


    public String toString()
    {
        return super.toString() + "\n  savedDC: " + savedDC;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        renderer.retoreDC();
    }
}
