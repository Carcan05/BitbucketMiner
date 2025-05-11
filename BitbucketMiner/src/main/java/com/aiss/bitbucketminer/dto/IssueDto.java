package com.aiss.bitbucketminer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "state",
        "created_at",
        "updated_at",
        "closed_at",
        "labels",
        "author",
        "assignee",
        "upvotes",
        "downvotes",
        "web_url",
        "comments"
})

public class IssueDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("state")
    private String state;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("closed_at")
    private String closedAt;
    @JsonProperty("labels")
    private List<String> labels;
    @JsonProperty("upvotes")
    private int upvotes;
    @JsonProperty("downvotes")
    private int downvotes;
    @JsonProperty("web_url")
    private String webUrl;
    @JsonProperty("author")
    private UserDto author;
    @JsonProperty("assignee")
    private UserDto assignee;
    @JsonProperty("comments")
    private List<CommentDto> comments;

    // getters y setters
    @JsonProperty("id")
    public String getId() {return id;}
    @JsonProperty("id")
    public void setId(String id) {this.id = id;}

    @JsonProperty("title")
    public String getTitle() {return title;}
    @JsonProperty("title")
    public void setTitle(String title) {this.title = title;}

    @JsonProperty("description")
    public String getDescription() {return description;}
    @JsonProperty("description")
    public void setDescription(String description) {this.description = description;}

    @JsonProperty("state")
    public String getState() {return state;}
    @JsonProperty("state")
    public void setState(String state) {this.state = state;}

    @JsonProperty("created_at")
    public String getCreatedAt() {return createdAt;}
    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}

    @JsonProperty("updated_at")
    public String getUpdatedAt() {return updatedAt;}
    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {this.updatedAt = updatedAt;}

    @JsonProperty("closed_at")
    public String getClosedAt() {return closedAt;}
    @JsonProperty("closed_at")
    public void setClosedAt(String closedAt) {this.closedAt = closedAt;}

    @JsonProperty("labels")
    public List<String> getLabels() {return labels;}
    @JsonProperty("labels")
    public void setLabels(List<String> labels) {this.labels = labels;}

    @JsonProperty("upvotes")
    public int getUpvotes() {return upvotes;}
    @JsonProperty("upvotes")
    public void setUpvotes(int upvotes) {this.upvotes = upvotes;}

    @JsonProperty("downvotes")
    public int getDownvotes() {return downvotes;}
    @JsonProperty("downvotes")
    public void setDownvotes(int downvotes) {this.downvotes = downvotes;}

    @JsonProperty("web_url")
    public String getWebUrl() {return webUrl;}
    @JsonProperty("web_url")
    public void setWebUrl(String webUrl) {this.webUrl = webUrl;}

    @JsonProperty("author")
    public UserDto getAuthor() {return author;}
    @JsonProperty("author")
    public void setAuthor(UserDto author) {this.author = author;}

    @JsonProperty("assignee")
    public UserDto getAssignee() {return assignee;}
    @JsonProperty("assignee")
    public void setAssignee(UserDto assignee) {this.assignee = assignee;}

    @JsonProperty("comments")
    public List<CommentDto> getComments() {return comments;}
    @JsonProperty("comments")
    public void setComments(List<CommentDto> comments) {this.comments = comments;}
}


