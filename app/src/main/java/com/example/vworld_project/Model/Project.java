package com.example.vworld_project.Model;

public class Project {

    private String projectid;
    private String onwerid;
    private String type;
    private String title;
    private String time;
    private String bids;
    private int bidno;
    private String cost;

    public Project(String projectid, String onwerid, String type, String title, String time, String bids, int bidno, String cost) {
        this.projectid = projectid;
        this.onwerid = onwerid;
        this.type = type;
        this.title = title;
        this.time = time;
        this.bids = bids;
        this.bidno = bidno;
        this.cost = cost;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getOnwerid() {
        return onwerid;
    }

    public void setOnwerid(String onwerid) {
        this.onwerid = onwerid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBids() {
        return bids;
    }

    public void setBids(String bids) {
        this.bids = bids;
    }

    public int getBidno() {
        return bidno;
    }

    public void setBidno(int bidno) {
        this.bidno = bidno;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
