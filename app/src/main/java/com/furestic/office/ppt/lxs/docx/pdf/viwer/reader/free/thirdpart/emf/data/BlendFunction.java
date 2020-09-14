// Copyright 2002-2003, FreeHEP.

package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFConstants;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;

import java.io.IOException;

/**
 * EMF BitmapInfoHeader
 * 
 * @author Mark Donszelmann
 * @version $Id: BlendFunction.java 10363 2007-01-20 15:30:50Z duns $
 */
public class BlendFunction implements EMFConstants
{

    public static final int size = 4;

    private int blendOp;

    private int blendFlags;

    private int sourceConstantAlpha;

    private int alphaFormat;

    public BlendFunction(int blendOp, int blendFlags, int sourceConstantAlpha, int alphaFormat)
    {
        this.blendOp = blendOp;
        this.blendFlags = blendFlags;
        this.sourceConstantAlpha = sourceConstantAlpha;
        this.alphaFormat = alphaFormat;
    }

    public BlendFunction(EMFInputStream emf) throws IOException
    {
        blendOp = emf.readUnsignedByte();
        blendFlags = emf.readUnsignedByte();
        sourceConstantAlpha = emf.readUnsignedByte();
        alphaFormat = emf.readUnsignedByte();
    }

    public String toString()
    {
        return "BlendFunction";
    }

    public int getSourceConstantAlpha()
    {
        return sourceConstantAlpha;
    }

    public int getAlphaFormat()
    {
        return alphaFormat;
    }
}
