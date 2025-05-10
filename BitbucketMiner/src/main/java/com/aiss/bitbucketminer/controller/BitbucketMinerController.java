package com.aiss.bitbucketminer.controller;

import com.aiss.bitbucketminer.dto.BitbucketResponse;
import com.aiss.bitbucketminer.service.BitbucketMinerService;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/bitbucket")
public class BitbucketMinerController {

    private static final Logger log = LoggerFactory.getLogger(BitbucketMinerController.class);
    private final BitbucketMinerService service;

    public BitbucketMinerController(BitbucketMinerService service) {
        this.service = service;
    }

    @RequestMapping(value = "/{workspace}/{repoSlug}", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<BitbucketResponse> mine(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(required = false) @Min(1) Integer nCommits,
            @RequestParam(required = false) @Min(1) Integer nIssues,
            @RequestParam(required = false) @Min(1) Integer maxPages
    ) {
        log.debug("Petición a /bitbucket/{}/{} → commits={}, issues={}, pages={}",
                workspace, repoSlug, nCommits, nIssues, maxPages);

        BitbucketResponse response = service.fetch(workspace, repoSlug, nCommits, nIssues, maxPages);
        return ResponseEntity.ok(response);
    }
}





