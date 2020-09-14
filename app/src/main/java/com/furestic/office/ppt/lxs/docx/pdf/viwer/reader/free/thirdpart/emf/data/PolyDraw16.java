// Copyright 2002, FreeHEP.

package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import android.graphics.Point;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.java.awt.Rectangle;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFConstants;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * PolyDraw16 TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: PolyDraw16.java 10367 2007-01-22 19:26:48Z duns $
 */
public class PolyDraw16 extends EMFTag implements EMFConstants
{

    private Rectangle bounds;

    private Point[] points;

    private byte[] types;

    public PolyDraw16()
    {
        super(92, 1);
    }

    public PolyDraw16(Rectangle bounds, Point[] points, byte[] types)
    {
        this();
        this.bounds = bounds;
        this.points = points;
        this.types = types;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        int n;
        return new PolyDraw16(emf.readRECTL(), emf.readPOINTS(n = emf.readDWORD()), emf.readBYTE(n));
    }

    public String toString()
    {
        return super.toString() + "\n  bounds: " + bounds + "\n  #points: " + points.length;
    }
}
