package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.models;

public class ModelFilesHolder {
    private String fileName;
    private String fileUri;
    private boolean isSelected;

    public ModelFilesHolder(String fileName, String fileUri, boolean isSelected) {
        this.fileName = fileName;
        this.fileUri = fileUri;
        this.isSelected = isSelected;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUri() {
        return fileUri;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
