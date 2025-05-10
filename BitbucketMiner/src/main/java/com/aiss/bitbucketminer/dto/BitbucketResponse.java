package com.aiss.bitbucketminer.dto;

import java.util.List;

public class BitbucketResponse {
    private List<CommitDto> commits;
    private List<IssueDto> issues;


    // getters y setters
    public List<CommitDto> getCommits() {return commits;}
    public void setCommits(List<CommitDto> commits) {this.commits = commits;}

    public List<IssueDto> getIssues() {return issues;}
    public void setIssues(List<IssueDto> issues) {this.issues = issues;}

}

