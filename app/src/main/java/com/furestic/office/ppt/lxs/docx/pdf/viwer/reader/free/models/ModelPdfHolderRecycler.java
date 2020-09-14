package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.models;

public class ModelPdfHolderRecycler {
    private int imgId;
    private String pdfFileName;
    private String pdfFileUri;
    private String pdfFileLockStatus;



    public ModelPdfHolderRecycler(int imgId,  String pdfFileName, String pdfFileUri,String pdfFileLockStatus ) {
        this.imgId = imgId;
        this.pdfFileName = pdfFileName;
        this.pdfFileUri = pdfFileUri;
        this.pdfFileLockStatus=pdfFileLockStatus;
    }

    public int getImgId() {
        return imgId;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public String getPdfFileUri() {
        return pdfFileUri;
    }

    public String getPdfFileLockStatus() {
        return pdfFileLockStatus;
    }

    public void setPdfFileLockStatus(String pdfFileLockStatus) {
        this.pdfFileLockStatus = pdfFileLockStatus;
    }






}
