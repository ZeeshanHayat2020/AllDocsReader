package com.example.alldocumentreader.models;

public class ModelFilesListRecycler {


    private int imgId;
    private String fileName;
    private String fileUri;
    private String fileLockStatus;
    public ModelFilesListRecycler(int imgId, String fileName, String fileUri, String fileLockStatus) {
        this.imgId = imgId;
        this.fileName = fileName;
        this.fileUri = fileUri;
        this.fileLockStatus = fileLockStatus;
    }

    public void setFileLockStatus(String fileLockStatus) {
        this.fileLockStatus = fileLockStatus;
    }



    public int getImgId() {
        return imgId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUri() {
        return fileUri;
    }

    public String getFileLockStatus() {
        return fileLockStatus;
    }


}
