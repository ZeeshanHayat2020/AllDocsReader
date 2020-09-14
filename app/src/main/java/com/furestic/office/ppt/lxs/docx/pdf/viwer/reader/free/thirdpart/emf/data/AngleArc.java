// Copyright 2002, FreeHEP.

package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import android.graphics.Point;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * AngleArc TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: AngleArc.java 10367 2007-01-22 19:26:48Z duns $
 */
public class AngleArc extends EMFTag
{

    private Point center;

    private int radius;

    private float startAngle, sweepAngle;

    public AngleArc()
    {
        super(41, 1);
    }

    public AngleArc(Point center, int radius, float startAngle, float sweepAngle)
    {
        this();
        this.center = center;
        this.radius = radius;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new AngleArc(emf.readPOINTL(), emf.readDWORD(), emf.readFLOAT(), emf.readFLOAT());
    }

    public String toString()
    {
        return super.toString() + "\n  center: " + center + "\n  radius: " + radius
            + "\n  startAngle: " + startAngle + "\n  sweepAngle: " + sweepAngle;
    }
}
