// Copyright 2007, FreeHEP.
package com.example.alldocumentreader.thirdpart.emf.data;

import android.graphics.Point;

import com.example.alldocumentreader.java.awt.Rectangle;
import com.example.alldocumentreader.thirdpart.emf.EMFRenderer;

/**
 * Parent class for a group of PolyLines. Childs are
 * rendered as not closed polygons.
 *
 * @author Steffen Greiffenberg
 * @version $Id$? */
public abstract class AbstractPolyPolyline extends AbstractPolyPolygon {

    protected AbstractPolyPolyline(
        int id,
        int version,
        Rectangle bounds,
        int[] numberOfPoints,
        Point[][] points) {

        super(id, version, bounds, numberOfPoints, points);
    }

    /**
     * displays the tag using the renderer. The default behavior
     * is not to close the polygons and not to fill them.
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        render(renderer, false);
    }
}
