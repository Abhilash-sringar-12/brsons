package com.example.brsons.pojo;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public class ImageUploadInfo {

    private String imageName;
    private String imageCategory;
    private String imageSubCategory;
    private String imageSubCategory1;
    private String imageURL;
    private Boolean imageLatest;
    private String imageKey;

    public ImageUploadInfo() {
    }

    public ImageUploadInfo(String tempImageName, String tempCategoryName, String tempSubCategoryName, String tempSubCategoryName1, Boolean imageLatest, String imageURL,
                           String ImageUploadId) {
        this.imageName = tempImageName;
        this.imageCategory = tempCategoryName;
        this.imageSubCategory = tempSubCategoryName;
        this.imageSubCategory1 = tempSubCategoryName1;
        this.imageLatest = imageLatest;
        this.imageURL = imageURL;
        this.imageKey = ImageUploadId;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
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

    public String getImageSubCategory1() {
        return imageSubCategory1;
    }

    public void setImageSubCategory1(String imageSubCategory1) {
        this.imageSubCategory1 = imageSubCategory1;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Boolean getImageLatest() {
        return imageLatest;
    }

    public void setImageLatest(Boolean imageLatest) {
        this.imageLatest = imageLatest;
    }

}
