package com.example.kuldeepgupta.popularmoviesapp.data.adapter.model;

/**
 * Created by kuldeep.gupta on 28-01-2016.
 */
public class TrailerModel {

    // can be a remote url or a drawable resource id
    private String imgUrl;
    private String displayText;

    public TrailerModel() {

    }

    public TrailerModel(String imgUrl, String displayText) {
        this.imgUrl = imgUrl;
        this.displayText = displayText;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }


}
