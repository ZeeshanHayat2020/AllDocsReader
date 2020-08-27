// Copyright 2002, FreeHEP.

package com.example.alldocumentreader.thirdpart.emf.data;

import com.example.alldocumentreader.thirdpart.emf.EMFInputStream;
import com.example.alldocumentreader.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * SetMapperFlags TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetMapperFlags.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetMapperFlags extends EMFTag
{

    private int flags;

    public SetMapperFlags()
    {
        super(16, 1);
    }

    public SetMapperFlags(int flags)
    {
        this();
        this.flags = flags;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetMapperFlags(emf.readDWORD());
    }


    public String toString()
    {
        return super.toString() + "\n  flags: " + flags;
    }
}
