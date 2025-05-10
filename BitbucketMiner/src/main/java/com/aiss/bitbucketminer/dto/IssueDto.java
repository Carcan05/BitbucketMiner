package com.aiss.bitbucketminer.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "state",
        "createdAt",
        "updatedAt",
        "closedAt",
        "labels",
        "author",
        "assignee",
        "upvotes",
        "downvotes",
        "webUrl",
        "comments"
})

public class IssueDto {
    private String id;
    private String title;
    private String description;
    private String state;
    private String createdAt;
    private String updatedAt;
    private String closedAt;
    private List<String> labels;
    private int upvotes;
    private int downvotes;
    private String webUrl;
    private UserDto author;
    private UserDto assignee;
    private List<CommentDto> comments;

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

    public int getUpvotes() {return upvotes;}
    public void setUpvotes(int upvotes) {this.upvotes = upvotes;}

    public int getDownvotes() {return downvotes;}
    public void setDownvotes(int downvotes) {this.downvotes = downvotes;}

    public String getWebUrl() {return webUrl;}
    public void setWebUrl(String webUrl) {this.webUrl = webUrl;}

    public UserDto getAuthor() {return author;}
    public void setAuthor(UserDto author) {this.author = author;}

    public UserDto getAssignee() {return assignee;}
    public void setAssignee(UserDto assignee) {this.assignee = assignee;}

    public List<CommentDto> getComments() {return comments;}
    public void setComments(List<CommentDto> comments) {this.comments = comments;}
}


