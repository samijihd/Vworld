package com.example.vworld_project.Model;

public class Project {

    private String id;
    private String projectid;
    private String ownerid;
    private String type;
    private String title;
    private String description;
    private String time;
    private String bids;
    private String bidno;
    private String budget;
    private String skill;
    private String isAccepted;
    private String isVisible;
    private String finishedWork;
    private String completed;
    private String freelancer;
    private String rated;

    public Project(String id, String projectid, String ownerid, String type, String title, String description, String time, String bids, String bidno, String budget, String skill, String isAccepted, String isVisible, String finishedWork, String completed, String freelancer, String rated) {
        this.id = id;
        this.projectid = projectid;
        this.ownerid = ownerid;
        this.type = type;
        this.title = title;
        this.description = description;
        this.time = time;
        this.bids = bids;
        this.bidno = bidno;
        this.budget = budget;
        this.skill = skill;
        this.isAccepted = isAccepted;
        this.isVisible = isVisible;
        this.finishedWork = finishedWork;
        this.completed = completed;
        this.freelancer = freelancer;
        this.rated = rated;
    }

    public Project(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getBidno() {
        return bidno;
    }

    public void setBidno(String bidno) {
        this.bidno = bidno;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }

    public String getFinishedWork() {
        return finishedWork;
    }

    public void setFinishedWork(String finishedWork) {
        this.finishedWork = finishedWork;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(String freelancer) {
        this.freelancer = freelancer;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }
}
