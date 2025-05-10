package com.aiss.bitbucketminer.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "comments")
public class CommentEntity implements Serializable {

    @Id
    private String id;

    @Lob
    @Column(name = "body", columnDefinition = "CLOB")
    private String body;

    private String createdAt;
    private String updatedAt;

    // Getters y setters

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getBody() {return body;}
    public void setBody(String body) {this.body = body;}

    public String getCreatedAt() {return createdAt;}
    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}

    public String getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(String updatedAt) {this.updatedAt = updatedAt;}
}


