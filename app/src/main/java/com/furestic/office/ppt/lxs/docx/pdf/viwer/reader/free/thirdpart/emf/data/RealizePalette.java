// Copyright 2002, FreeHEP.
package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.data;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFInputStream;
import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * RealizePalette TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: RealizePalette.java 10140 2006-12-07 07:50:41Z duns $
 */
public class RealizePalette extends EMFTag {

    public RealizePalette() {
        super(52, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return this;
    }

}
