// Copyright 2002, FreeHEP.

package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;

import java.io.IOException;

/**
 * EMF GradientRectangle
 * 
 * @author Mark Donszelmann
 * @version $Id: GradientRectangle.java 10140 2006-12-07 07:50:41Z duns $
 */
public class GradientRectangle extends Gradient
{

    private int upperLeft, lowerRight;

    public GradientRectangle(int upperLeft, int lowerRight)
    {
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
    }

    public GradientRectangle(EMFInputStream emf) throws IOException
    {
        upperLeft = emf.readULONG();
        lowerRight = emf.readULONG();
    }

    public String toString()
    {
        return "  GradientRectangle: " + upperLeft + ", " + lowerRight;
    }
}
