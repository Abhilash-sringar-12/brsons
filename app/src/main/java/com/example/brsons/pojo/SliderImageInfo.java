package com.example.brsons.pojo;

public class SliderImageInfo {

    private String sliderTitle1;
    private String sliderTitle2;
    private String sliderTitle3;
    private String sliderDesc1;
    private String sliderDesc2;
    private String sliderDesc3;
    private String sliderImageURL1;
    private String sliderImageURL2;
    private String sliderImageURL3;

    public SliderImageInfo() {
    }

    public SliderImageInfo(String TempSliderTitle1, String TempSliderTitle2, String TempSliderTitle3, String TempSliderDesc1,
                           String TempSliderDesc2, String TempSliderDesc3, String sliderImageURL1,String sliderImageURL2,String sliderImageURL3) {
        this.sliderTitle1 = TempSliderTitle1;
        this.sliderTitle2 = TempSliderTitle2;
        this.sliderTitle3 = TempSliderTitle3;
        this.sliderDesc1 = TempSliderDesc1;
        this.sliderDesc2 = TempSliderDesc2;
        this.sliderDesc3 = TempSliderDesc3;
        this.sliderImageURL1 = sliderImageURL1;
        this.sliderImageURL2 = sliderImageURL2;
        this.sliderImageURL3 = sliderImageURL3;
    }

    public String getSliderTitle1() {
        return sliderTitle1;
    }

    public void setSliderTitle1(String sliderTitle1) {
        this.sliderTitle1 = sliderTitle1;
    }

    public String getSliderTitle2() {
        return sliderTitle2;
    }

    public void setSliderTitle2(String sliderTitle2) {
        this.sliderTitle2 = sliderTitle2;
    }

    public String getSliderTitle3() {
        return sliderTitle3;
    }

    public void setSliderTitle3(String sliderTitle3) {
        this.sliderTitle3 = sliderTitle3;
    }

    public String getSliderDesc1() {
        return sliderDesc1;
    }

    public void setSliderDesc1(String sliderDesc1) {
        this.sliderDesc1 = sliderDesc1;
    }

    public String getSliderDesc2() {
        return sliderDesc2;
    }

    public void setSliderDesc2(String sliderDesc2) {
        this.sliderDesc2 = sliderDesc2;
    }

    public String getSliderDesc3() {
        return sliderDesc3;
    }

    public void setSliderDesc3(String sliderDesc3) {
        this.sliderDesc3 = sliderDesc3;
    }

    public String getSliderImageURL1() {
        return sliderImageURL1;
    }

    public void setSliderImageURL1(String sliderImageURL1) {
        this.sliderImageURL1 = sliderImageURL1;
    }

    public String getSliderImageURL2() {
        return sliderImageURL2;
    }

    public void setSliderImageURL2(String sliderImageURL2) {
        this.sliderImageURL2 = sliderImageURL2;
    }

    public String getSliderImageURL3() {
        return sliderImageURL3;
    }

    public void setSliderImageURL3(String sliderImageURL3) {
        this.sliderImageURL3 = sliderImageURL3;
    }
}
