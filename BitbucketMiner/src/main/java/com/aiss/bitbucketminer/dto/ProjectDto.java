package com.aiss.bitbucketminer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder
({
        "id",
        "name",
        "web_url",
        "commits",
        "issues"
})

public class ProjectDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("web_url")
    private String webUrl;
    @JsonProperty("commits")
    private List<CommitDto> commits;
    @JsonProperty("issues")
    private List<IssueDto> issues;



    // getters y setters
    @JsonProperty("id")
    public String getId() {return id;}
    @JsonProperty("id")
    public void setId(String id) {this.id = id;}

    @JsonProperty("name")
    public String getName() {return name;}
    @JsonProperty("name")
    public void setName(String name) {this.name = name;}

    @JsonProperty("web_url")
    public String getWebUrl() {return webUrl;}
    @JsonProperty("web_url")
    public void setWebUrl(String webUrl) {this.webUrl = webUrl;}

    @JsonProperty("commits")
    public List<CommitDto> getCommits() {return commits;}
    @JsonProperty("commits")
    public void setCommits(List<CommitDto> commits) {this.commits = commits;}

    @JsonProperty("issues")
    public List<IssueDto> getIssues() {return issues;}
    @JsonProperty("issues")
    public void setIssues(List<IssueDto> issues) {this.issues = issues;}

}

