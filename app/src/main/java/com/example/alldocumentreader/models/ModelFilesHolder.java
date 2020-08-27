package com.example.alldocumentreader.models;

public class ModelFilesHolder {
    private int imageId;



    private String fileName;
    private String fileUri;

    public ModelFilesHolder(int imageId, String fileName, String fileUri) {
        this.imageId = imageId;
        this.fileName = fileName;
        this.fileUri = fileUri;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public int getImageId() {
        return imageId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUri() {
        return fileUri;
    }






}
