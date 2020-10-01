package com.example.brsons.pojo;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public class ImageUploadInfo {

    private String imageName;
    private String imageCategory;
    private String imageSubCategory;
    private String imageURL;
    private String imageLatest;

    public ImageUploadInfo() {
    }

    public ImageUploadInfo(String tempImageName, String tempCategoryName, String tempSubCategoryName,String imageLatest, String imageURL) {
        this.imageName = tempImageName;
        this.imageCategory = tempCategoryName;
        this.imageSubCategory = tempSubCategoryName;
        this.imageLatest = imageLatest;
        this.imageURL = imageURL;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(String imageCategory) {
        this.imageCategory = imageCategory;
    }

    public String getImageSubCategory() {
        return imageSubCategory;
    }

    public void setImageSubCategory(String imageSubCategory) {
        this.imageSubCategory = imageSubCategory;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageLatest() {
        return imageLatest;
    }

    public void setImageLatest(String imageLatest) {
        this.imageLatest = imageLatest;
    }

}
