package com.example.alldocumentreader.models;

public class ModelFilesHolder {
    private String fileName;
    private String fileUri;

    public ModelFilesHolder(String fileName, String fileUri) {
        this.fileName = fileName;
        this.fileUri = fileUri;
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


}
