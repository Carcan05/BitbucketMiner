package com.aiss.bitbucketminer.dto;

import java.util.List;

public class ProjectDto {
    private String id;
    private String name;
    private String webUrl;
    private List<CommitDto> commits;
    private List<IssueDto> issues;



    // getters y setters
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getWebUrl() {return webUrl;}
    public void setWebUrl(String webUrl) {this.webUrl = webUrl;}

    public List<CommitDto> getCommits() {return commits;}
    public void setCommits(List<CommitDto> commits) {this.commits = commits;}

    public List<IssueDto> getIssues() {return issues;}
    public void setIssues(List<IssueDto> issues) {this.issues = issues;}

}

