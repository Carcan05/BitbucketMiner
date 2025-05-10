package com.aiss.bitbucketminer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private String id;
    private String username;
    private String name;
    private String avatarUrl;
    private String webUrl;

    // getters y setters
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getAvatarUrl() {return avatarUrl;}
    public void setAvatarUrl(String avatarUrl) {this.avatarUrl = avatarUrl;}

    public String getWebUrl() {return webUrl;}
    public void setWebUrl(String webUrl) {this.webUrl = webUrl;}

}

