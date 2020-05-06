package com.example.vworld_project.Model;

public class Review {
    private String reviewAdderId;
    private float rateValue;
    private String text;

    public Review(String reviewAdderId, float rateValue, String text) {
        this.reviewAdderId = reviewAdderId;
        this.rateValue = rateValue;
        this.text = text;
    }

    public Review(){
    }

    public String getReviewAdderId() {
        return reviewAdderId;
    }

    public void setReviewAdderId(String reviewAdderId) {
        this.reviewAdderId = reviewAdderId;
    }

    public float getRateValue() {
        return rateValue;
    }

    public void setRateValue(float rateValue) {
        this.rateValue = rateValue;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
