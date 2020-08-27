package com.example.alldocumentreader.models;

public class ModelScannedDocs {
    private String scanDocUri;
    private String scanDocName;

    public ModelScannedDocs(String scanDocUri, String scanDocName) {
        this.scanDocUri = scanDocUri;
        this.scanDocName = scanDocName;
    }

    public String getScanDocUri() {
        return scanDocUri;
    }

    public String getScanDocName() {
        return scanDocName;
    }


}
