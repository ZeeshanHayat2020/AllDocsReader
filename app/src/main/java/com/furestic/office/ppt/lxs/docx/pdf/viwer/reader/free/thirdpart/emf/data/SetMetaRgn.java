// Copyright 2002, FreeHEP.
package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFRenderer;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * SetMetaRgn TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetMetaRgn.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetMetaRgn extends EMFTag {

    public SetMetaRgn() {
        super(28, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return this;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // The SetMetaRgn function intersects the current clipping region
        // for the specified device context with the current metaregion and
        // saves the combined region as the new metaregion for the specified
        // device context. The clipping region is reset to a null region.

        // TODO: what ist the current metaregion?
    }
}
