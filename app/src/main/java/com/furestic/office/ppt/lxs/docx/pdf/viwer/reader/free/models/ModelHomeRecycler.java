package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.models;

public class ModelHomeRecycler {
    private int BtnImgId;
    private String BtnText;

    public ModelHomeRecycler(int btnImgId, String btnText) {
        BtnImgId = btnImgId;
        BtnText = btnText;
    }

    public int getBtnImgId() {
        return BtnImgId;
    }

    public String getBtnText() {
        return BtnText;
    }
}
