package com.aiss.bitbucketminer.controller;

import com.aiss.bitbucketminer.dto.BitbucketResponse;
import com.aiss.bitbucketminer.dto.ProjectDto;
import com.aiss.bitbucketminer.service.BitbucketMinerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

@Validated
@RestController
@RequestMapping("/bitbucket")
public class BitbucketMinerController {

    private static final Logger log = LoggerFactory.getLogger(BitbucketMinerController.class);
    private final BitbucketMinerService service;

    public BitbucketMinerController(BitbucketMinerService service) {
        this.service = service;
    }

    @GetMapping("/{workspace}/{repoSlug}")
    public ResponseEntity<ProjectDto> getProjectData(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(required = false) @Min(1) Integer nCommits,
            @RequestParam(required = false) @Min(1) Integer nIssues,
            @RequestParam(required = false) @Min(1) Integer maxPages
    ) {
        log.debug("GET /bitbucket/{}/{}", workspace, repoSlug);
        BitbucketResponse resp = service.fetchOnly(workspace, repoSlug, nCommits, nIssues, maxPages);
        return ResponseEntity.ok(resp.getProject());
    }

    @PostMapping("/{workspace}/{repoSlug}")
    public ResponseEntity<?> postProjectData(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(required = false) @Min(1) Integer nCommits,
            @RequestParam(required = false) @Min(1) Integer nIssues,
            @RequestParam(required = false) @Min(1) Integer maxPages
    ) {
        log.debug("POST /bitbucket/{}/{}", workspace, repoSlug);
        try {
            BitbucketResponse resp = service.fetchAndSend(workspace, repoSlug, nCommits, nIssues, maxPages);
            return ResponseEntity.ok(resp.getProject());
        } catch (ResourceAccessException ex) {
            log.error("No se pudo conectar con GitMiner: {}", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("GitMiner no disponible: " + ex.getMessage());
        }
    }
}

