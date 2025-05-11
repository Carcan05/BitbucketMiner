package com.aiss.bitbucketminer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder
({
        "id",
        "username",
        "name",
        "avatarUrl",
        "webUrl"
})

public class UserDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("name")
    private String name;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("web_url")
    private String webUrl;


    // getters y setters
    @JsonProperty("id")
    public String getId() {return id;}
    @JsonProperty("id")
    public void setId(String id) {this.id = id;}

    @JsonProperty("username")
    public String getUsername() {return username;}
    @JsonProperty("username")
    public void setUsername(String username) {this.username = username;}

    @JsonProperty("name")
    public String getName() {return name;}
    @JsonProperty("name")
    public void setName(String name) {this.name = name;}

    @JsonProperty("avatar_url")
    public String getAvatarUrl() {return avatarUrl;}
    @JsonProperty("avatar_url")
    public void setAvatarUrl(String avatarUrl) {this.avatarUrl = avatarUrl;}

    @JsonProperty("web_url")
    public String getWebUrl() {return webUrl;}
    @JsonProperty("web_url")
    public void setWebUrl(String webUrl) {this.webUrl = webUrl;}

}