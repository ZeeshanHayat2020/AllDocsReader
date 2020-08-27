// Copyright 2002, FreeHEP.

package com.example.alldocumentreader.thirdpart.emf.data;

import com.example.alldocumentreader.java.awt.geom.GeneralPath;
import com.example.alldocumentreader.thirdpart.emf.EMFConstants;
import com.example.alldocumentreader.thirdpart.emf.EMFInputStream;
import com.example.alldocumentreader.thirdpart.emf.EMFRenderer;
import com.example.alldocumentreader.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * SetPolyFillMode TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetPolyFillMode.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetPolyFillMode extends EMFTag implements EMFConstants
{

    private int mode;

    public SetPolyFillMode()
    {
        super(19, 1);
    }

    public SetPolyFillMode(int mode)
    {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetPolyFillMode(emf.readDWORD());
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
        renderer.setWindingRule(getWindingRule(mode));
    }

    /**
     * gets a winding rule for GeneralPath creation based on
     * EMF SetPolyFillMode.
     *
     * @param polyFillMode PolyFillMode to convert
     * @return winding rule
     */
    private int getWindingRule(int polyFillMode)
    {
        if (polyFillMode == EMFConstants.WINDING)
        {
            return GeneralPath.WIND_EVEN_ODD;
        }
        else if (polyFillMode == EMFConstants.ALTERNATE)
        {
            return GeneralPath.WIND_NON_ZERO;
        }
        else
        {
            return GeneralPath.WIND_EVEN_ODD;
        }
    }

}
