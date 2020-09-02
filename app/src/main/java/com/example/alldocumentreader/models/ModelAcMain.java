package com.example.alldocumentreader.models;

public class ModelAcMain {

    private String itemName;
    private int itemImageId;

    public ModelAcMain(String itemName, int itemImageId) {
        this.itemName = itemName;
        this.itemImageId = itemImageId;
    }
    public String getItemName() {
        return itemName;
    }

    public int getItemImageId() {
        return itemImageId;
    }


}
