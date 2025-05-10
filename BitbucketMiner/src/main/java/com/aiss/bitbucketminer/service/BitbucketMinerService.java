package com.aiss.bitbucketminer.service;

import com.aiss.bitbucketminer.client.BitbucketClient;
import com.aiss.bitbucketminer.dto.BitbucketResponse;
import com.aiss.bitbucketminer.dto.CommitDto;
import com.aiss.bitbucketminer.dto.IssueDto;
import com.aiss.bitbucketminer.dto.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

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

    /** Sólo fetch de Bitbucket, sin enviar a GitMiner */
    public BitbucketResponse fetchOnly(String workspace,
                                       String repoSlug,
                                       Integer nCommits,
                                       Integer nIssues,
                                       Integer maxPages) {
        // 0) Datos básicos del proyecto
        ProjectDto project = client.getProject(workspace, repoSlug);

        // 1) Parámetros
        int commitsPerPage = (nCommits   != null ? nCommits   : defaultCommits);
        int issuesPerPage  = (nIssues    != null ? nIssues    : defaultIssues);
        int pages          = (maxPages   != null ? maxPages   : defaultMaxPages);

        log.info("FetchOnly: {}/{} commits={}, issues={}, pages={}",
                workspace, repoSlug, commitsPerPage, issuesPerPage, pages);

        // 2) Obtener DTOs
        List<CommitDto> dtoCommits = client
                .getCommits(workspace, repoSlug, commitsPerPage, pages)
                .stream().limit(commitsPerPage).collect(Collectors.toList());
        List<IssueDto> dtoIssues = client
                .getIssues(workspace, repoSlug, issuesPerPage, pages)
                .stream().limit(issuesPerPage).collect(Collectors.toList());

        // 3) Anidar dentro de project
        project.setCommits(dtoCommits);
        project.setIssues(dtoIssues);

        // 4) Devolver respuesta
        BitbucketResponse response = new BitbucketResponse();
        response.setProject(project);
        return response;
    }

    /** Fetch + POST a GitMiner */
    public BitbucketResponse fetchAndSend(String workspace,
                                          String repoSlug,
                                          Integer nCommits,
                                          Integer nIssues,
                                          Integer maxPages) {
        BitbucketResponse response = fetchOnly(workspace, repoSlug, nCommits, nIssues, maxPages);

        String url = gitminerBaseUrl + "/repos/{workspace}/{repoSlug}";
        log.info("Enviando a GitMiner: POST {}", url);
        restTemplate.postForLocation(
                url,
                response,
                workspace, repoSlug
        );

        return response;
    }
}
