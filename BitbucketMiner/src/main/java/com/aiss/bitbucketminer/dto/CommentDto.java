package com.aiss.bitbucketminer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "id",
        "body",
        "author",
        "created_at",
        "updated_at"
})

public class CommentDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("body")
    private String body;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("author")
    private UserDto author;


    // getters y setters
    @JsonProperty("id")
    public String getId() {return id;}
    @JsonProperty("id")
    public void setId(String id) {this.id = id;}

    @JsonProperty("body")
    public String getBody() {return body;}
    @JsonProperty("body")
    public void setBody(String body) {this.body = body;}

    @JsonProperty("created_at")
    public String getCreatedAt() {return createdAt;}
    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}

    @JsonProperty("updated_at")
    public String getUpdatedAt() {return updatedAt;}
    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {this.updatedAt = updatedAt;}

    @JsonProperty("author")
    public UserDto getAuthor() {return author;}
    @JsonProperty("author")
    public void setAuthor(UserDto author) {this.author = author;}

}

