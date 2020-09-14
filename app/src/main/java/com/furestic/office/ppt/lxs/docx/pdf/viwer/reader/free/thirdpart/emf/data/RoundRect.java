// Copyright 2002, FreeHEP.

package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.java.awt.Dimension;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.java.awt.Rectangle;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.java.awt.geom.RoundRectangle2D;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFRenderer;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * RoundRect TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: RoundRect.java 10377 2007-01-23 15:44:34Z duns $
 */
public class RoundRect extends EMFTag
{

    private Rectangle bounds;

    private Dimension corner;

    public RoundRect()
    {
        super(44, 1);
    }

    public RoundRect(Rectangle bounds, Dimension corner)
    {
        this();
        this.bounds = bounds;
        this.corner = corner;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new RoundRect(emf.readRECTL(), emf.readSIZEL());
    }

    public String toString()
    {
        return super.toString() + "\n  bounds: " + bounds + "\n  corner: " + corner;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        renderer.fillAndDrawOrAppend(new RoundRectangle2D.Double(bounds.x, bounds.x,
            bounds.getWidth(), bounds.getHeight(), corner.getWidth(), corner.getHeight()));
    }
}
