package com.aiss.bitbucketminer.service;

import com.aiss.bitbucketminer.client.BitbucketClient;
import com.aiss.bitbucketminer.dto.BitbucketResponse;
import com.aiss.bitbucketminer.dto.CommitDto;
import com.aiss.bitbucketminer.dto.IssueDto;
import com.aiss.bitbucketminer.dto.ProjectDto;
import com.aiss.bitbucketminer.entity.CommentEntity;
import com.aiss.bitbucketminer.entity.CommitEntity;
import com.aiss.bitbucketminer.entity.IssueEntity;
import com.aiss.bitbucketminer.entity.UserEntity;
import com.aiss.bitbucketminer.repository.CommitRepository;
import com.aiss.bitbucketminer.repository.IssueRepository;
import com.aiss.bitbucketminer.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BitbucketMinerService {

    private static final Logger log = LoggerFactory.getLogger(BitbucketMinerService.class);

    private final BitbucketClient client;
    private final CommitRepository commitRepo;
    private final UserRepository userRepo;
    private final IssueRepository issueRepo;

    @Value("${bitbucket.default.nCommits}")
    private int defaultCommits;
    @Value("${bitbucket.default.nIssues}")
    private int defaultIssues;
    @Value("${bitbucket.default.maxPages}")
    private int defaultMaxPages;

    public BitbucketMinerService(BitbucketClient client,
                                 CommitRepository commitRepo,
                                 UserRepository userRepo,
                                 IssueRepository issueRepo) {
        this.client     = client;
        this.commitRepo = commitRepo;
        this.userRepo   = userRepo;
        this.issueRepo  = issueRepo;
    }

    @Transactional
    public BitbucketResponse fetch(String workspace,
                                   String repoSlug,
                                   Integer nCommits,
                                   Integer nIssues,
                                   Integer maxPages) {

        ProjectDto project = client.getProject(workspace, repoSlug);

        int commitsPerPage = nCommits   != null ? nCommits   : defaultCommits;
        int issuesPerPage  = nIssues    != null ? nIssues    : defaultIssues;
        int pages          = maxPages   != null ? maxPages   : defaultMaxPages;

        log.info("Fetch y persistencia: {}/{} commits={}, issues={}, pages={}",
                workspace, repoSlug, commitsPerPage, issuesPerPage, pages);

        // 1) Obtener DTOs
        List<CommitDto> dtoCommits = client
                .getCommits(workspace, repoSlug, commitsPerPage, pages)
                .stream().limit(commitsPerPage).collect(Collectors.toList());

        List<IssueDto> dtoIssues = client
                .getIssues(workspace, repoSlug, issuesPerPage, pages)
                .stream().limit(issuesPerPage).collect(Collectors.toList());

        // 2) Mapear y persistir Commits
        List<CommitEntity> entitiesCommits = dtoCommits.stream().map(c -> {
            CommitEntity e = new CommitEntity();
            e.setId(c.getId());
            e.setTitle(c.getTitle());
            e.setMessage(c.getMessage());
            e.setAuthorName(c.getAuthorName());
            e.setAuthorEmail(c.getAuthorEmail());
            e.setAuthoredDate(c.getAuthoredDate());
            e.setWebUrl(c.getWebUrl());
            return e;
        }).collect(Collectors.toList());
        commitRepo.saveAll(entitiesCommits);

        // 3) Mapear y persistir Issues (+ Users + Comments)
        for (IssueDto i : dtoIssues) {
            // User
            UserEntity u = null;
            if (i.getUser() != null) {
                u = new UserEntity();
                u.setId(i.getUser().getId());
                u.setUsername(i.getUser().getUsername());
                u.setName(i.getUser().getName());
                u.setAvatarUrl(i.getUser().getAvatarUrl());
                u.setWebUrl(i.getUser().getWebUrl());
                userRepo.save(u);
            }

            IssueEntity ie = new IssueEntity();
            ie.setId(i.getId());
            ie.setTitle(i.getTitle());
            ie.setDescription(i.getDescription());
            ie.setState(i.getState());
            ie.setCreatedAt(i.getCreatedAt());
            ie.setUpdatedAt(i.getUpdatedAt());
            ie.setClosedAt(i.getClosedAt());
            ie.setLabels(i.getLabels());
            ie.setVotes(i.getVotes());
            ie.setUser(u);
            // Comments
            List<CommentEntity> ces = i.getComments().stream().map(c -> {
                CommentEntity ce = new CommentEntity();
                ce.setId(c.getId());
                ce.setBody(c.getBody());
                ce.setCreatedAt(c.getCreatedAt());
                ce.setUpdatedAt(c.getUpdatedAt());
                return ce;
            }).collect(Collectors.toList());
            ie.setComments(ces);

            issueRepo.save(ie);
        }

        // 4) Devolver DTO con setters (no all-args)
        BitbucketResponse response = new BitbucketResponse();
        response.setProject(project);
        response.setCommits(dtoCommits);
        response.setIssues(dtoIssues);
        return response;
    }
}





