package com.aiss.bitbucketminer.dto;

import java.util.List;

public class IssueDto {
    private String id;
    private String title;
    private String description;
    private String state;
    private String createdAt;
    private String updatedAt;
    private String closedAt;
    private List<String> labels;
    private Integer votes;
    private UserDto user;            // autor
    private List<CommentDto> comments;


    // getters y setters
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public String getCreatedAt() {return createdAt;}
    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}

    public String getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(String updatedAt) {this.updatedAt = updatedAt;}

    public String getClosedAt() {return closedAt;}
    public void setClosedAt(String closedAt) {this.closedAt = closedAt;}

    public List<String> getLabels() {return labels;}
    public void setLabels(List<String> labels) {this.labels = labels;}

    public Integer getVotes() {return votes;}
    public void setVotes(Integer votes) {this.votes = votes;}

    public UserDto getUser() {return user;}
    public void setUser(UserDto user) {this.user = user;}

    public List<CommentDto> getComments() {return comments;}
    public void setComments(List<CommentDto> comments) {this.comments = comments;}

}

