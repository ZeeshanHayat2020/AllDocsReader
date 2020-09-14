// Copyright 2002, FreeHEP.

package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import android.graphics.Point;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.java.awt.Color;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * SetPixelV TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetPixelV.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetPixelV extends EMFTag
{

    private Point point;

    private Color color;

    public SetPixelV()
    {
        super(15, 1);
    }

    public SetPixelV(Point point, Color color)
    {
        this();
        this.point = point;
        this.color = color;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetPixelV(emf.readPOINTL(), emf.readCOLORREF());
    }

    public String toString()
    {
        return super.toString() + "\n  point: " + point + "\n  color: " + color;
    }
}
