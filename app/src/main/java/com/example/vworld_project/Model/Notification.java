package com.example.vworld_project.Model;

public class Notification {

    private String userID;
    private String title;
    private String text;
    private String projectID;
    private String ownerID;
    private String notifyType;
    private String bidId;

    public Notification(String userID, String title, String text, String projectID, String ownerID, String notifyType, String bidId) {
        this.userID = userID;
        this.title = title;
        this.text = text;
        this.projectID = projectID;
        this.ownerID = ownerID;
        this.notifyType = notifyType;
        this.bidId = bidId;
    }

    public Notification() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }
}
