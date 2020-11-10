package com.example.brsons.pojo;

public class SliderImageInfo {

    private String sliderTitle;
    private String sliderDesc;
    private String sliderImageURL;

    public SliderImageInfo(String sliderTitle, String sliderDesc, String sliderImageURL) {
        this.sliderTitle = sliderTitle;
        this.sliderDesc = sliderDesc;
        this.sliderImageURL = sliderImageURL;
    }

    public String getSliderTitle() {
        return sliderTitle;
    }

    public void setSliderTitle(String sliderTitle) {
        this.sliderTitle = sliderTitle;
    }

    public String getSliderDesc() {
        return sliderDesc;
    }

    public void setSliderDesc(String sliderDesc) {
        this.sliderDesc = sliderDesc;
    }

    public String getSliderImageURL() {
        return sliderImageURL;
    }

    public void setSliderImageURL(String sliderImageURL) {
        this.sliderImageURL = sliderImageURL;
    }

}
