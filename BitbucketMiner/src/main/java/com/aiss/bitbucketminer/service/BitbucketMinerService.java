package com.aiss.bitbucketminer.service;

import com.aiss.bitbucketminer.client.BitbucketClient;
import com.aiss.bitbucketminer.dto.BitbucketResponse;
import com.aiss.bitbucketminer.dto.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BitbucketMinerService {

    private static final Logger log = LoggerFactory.getLogger(BitbucketMinerService.class);

    private final BitbucketClient client;
    private final RestTemplate restTemplate;

    @Value("${bitbucket.default.nCommits}")
    private int defaultCommits;
    @Value("${bitbucket.default.nIssues}")
    private int defaultIssues;
    @Value("${bitbucket.default.maxPages}")
    private int defaultMaxPages;

    @Value("${gitminer.api.base-url}")
    private String gitminerBaseUrl;

    public BitbucketMinerService(BitbucketClient client,
                                 RestTemplate restTemplate) {
        this.client       = client;
        this.restTemplate = restTemplate;
    }

    public BitbucketResponse fetchOnly(String workspace,
                                       String repoSlug,
                                       Integer nCommits,
                                       Integer nIssues,
                                       Integer maxPages) {
        ProjectDto project = client.getProject(workspace, repoSlug);

        int commitsPerPage = (nCommits   != null ? nCommits   : defaultCommits);
        int issuesPerPage  = (nIssues    != null ? nIssues    : defaultIssues);
        int pages          = (maxPages   != null ? maxPages   : defaultMaxPages);

        log.info("FetchOnly: {}/{} commits={}, issues={}, pages={}",
                workspace, repoSlug, commitsPerPage, issuesPerPage, pages);

        project.setCommits(client.getCommits(workspace, repoSlug, commitsPerPage, pages));
        project.setIssues(client.getIssues(workspace, repoSlug, issuesPerPage, pages));

        BitbucketResponse response = new BitbucketResponse();
        response.setProject(project);
        return response;
    }

    public BitbucketResponse fetchAndSend(String workspace,
                                          String repoSlug,
                                          Integer nCommits,
                                          Integer nIssues,
                                          Integer maxPages) {
        BitbucketResponse response = fetchOnly(workspace, repoSlug, nCommits, nIssues, maxPages);

        String url = gitminerBaseUrl + "/projects";
        log.info("Enviando a GitMiner: POST {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProjectDto> body = new HttpEntity<>(response.getProject(), headers);

        restTemplate.postForLocation(url, body);

        return response;
    }
}
