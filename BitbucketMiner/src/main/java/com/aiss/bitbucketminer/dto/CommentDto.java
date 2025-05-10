package com.aiss.bitbucketminer.dto;

public class CommentDto {
    private String id;
    private String body;
    private String createdAt;
    private String updatedAt;


    // getters y setters
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getBody() {return body;}
    public void setBody(String body) {this.body = body;}

    public String getCreatedAt() {return createdAt;}
    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}

    public String getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(String updatedAt) {this.updatedAt = updatedAt;}

}

