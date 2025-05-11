package com.aiss.bitbucketminer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder
({
        "id",
        "title",
        "message",
        "author_name",
        "author_email",
        "authored_date",
        "web_url"
})

public class CommitDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("message")
    private String message;
    @JsonProperty("author_name")
    private String authorName;
    @JsonProperty("author_email")
    private String authorEmail;
    @JsonProperty("authored_date")
    private String authoredDate;
    @JsonProperty("web_url")
    private String webUrl;


    // getters y setters
    @JsonProperty("id")
    public String getId() {return id;}
    @JsonProperty("id")
    public void setId(String id) {this.id = id;}

    @JsonProperty("title")
    public String getTitle() {return title;}
    @JsonProperty("title")
    public void setTitle(String title) {this.title = title;}

    @JsonProperty("message")
    public String getMessage() {return message;}
    @JsonProperty("message")
    public void setMessage(String message) {this.message = message;}

    @JsonProperty("author_name")
    public String getAuthorName() {return authorName;}
    @JsonProperty("author_name")
    public void setAuthorName(String authorName) {this.authorName = authorName;}

    @JsonProperty("author_email")
    public String getAuthorEmail() {return authorEmail;}
    @JsonProperty("author_email")
    public void setAuthorEmail(String authorEmail) {this.authorEmail = authorEmail;}

    @JsonProperty("authored_date")
    public String getAuthoredDate() {return authoredDate;}
    @JsonProperty("authored_date")
    public void setAuthoredDate(String authoredDate) {this.authoredDate = authoredDate;}

    @JsonProperty("web_url")
    public String getWebUrl() {return webUrl;}
    @JsonProperty("web_url")
    public void setWebUrl(String webUrl) {this.webUrl = webUrl;}

}

