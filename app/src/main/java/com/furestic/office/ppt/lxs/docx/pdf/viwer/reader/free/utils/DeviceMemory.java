package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.utils;

import android.os.Environment;
import android.os.StatFs;

public class DeviceMemory {

    public static float getInternalStorageSpace() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        float total = ((float) statFs.getBlockCount() * statFs.getBlockSize()) / 1073741824;
        return total;
    }

    public static float getInternalFreeSpace() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        float free = ((float) statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1073741824;
        return free;
    }

    public static float getInternalUsedSpace() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        float total = ((float) statFs.getBlockCount() * statFs.getBlockSize()) / 1073741824;
        float free = ((float) statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1073741824;
        float busy = total - free;
        return busy;
    }
}
