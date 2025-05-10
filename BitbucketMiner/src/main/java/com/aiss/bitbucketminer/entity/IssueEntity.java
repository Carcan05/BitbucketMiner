package com.aiss.bitbucketminer.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "issues")
public class IssueEntity {
    @Id
    private String id;
    private String title;
    @Lob
    @Column(name = "description", columnDefinition = "CLOB")
    private String description;
    private String state;
    private String createdAt;
    private String updatedAt;
    private String closedAt;

    @ElementCollection
    @CollectionTable(name = "issue_labels", joinColumns = @JoinColumn(name = "issue_id"))
    @Column(name = "label")
    private List<String> labels;

    private Integer votes;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "issue_id")
    private List<CommentEntity> comments;

    // getters y setters
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public String getCreatedAt() {return createdAt;}
    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}

    public String getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(String updatedAt) {this.updatedAt = updatedAt;}

    public String getClosedAt() {return closedAt;}
    public void setClosedAt(String closedAt) {this.closedAt = closedAt;}

    public List<String> getLabels() {return labels;}
    public void setLabels(List<String> labels) {this.labels = labels;}

    public Integer getVotes() {return votes;}
    public void setVotes(Integer votes) {this.votes = votes;}

    public UserEntity getUser() {return user;}
    public void setUser(UserEntity user) {this.user = user;}

    public List<CommentEntity> getComments() {return comments;}
    public void setComments(List<CommentEntity> comments) {this.comments = comments;}

}
