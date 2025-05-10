package com.aiss.bitbucketminer.client;

import com.aiss.bitbucketminer.dto.ProjectDto;
import com.aiss.bitbucketminer.dto.CommitDto;
import com.aiss.bitbucketminer.dto.IssueDto;
import com.aiss.bitbucketminer.dto.CommentDto;
import com.aiss.bitbucketminer.dto.UserDto;
import com.aiss.bitbucketminer.exception.BitbucketApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Component
public class BitbucketClient {
    private static final Logger log = LoggerFactory.getLogger(BitbucketClient.class);

    private final RestTemplate restTemplate;

    @Value("${bitbucket.api.base-url}")
    private String baseUrl;

    public BitbucketClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProjectDto getProject(String workspace, String repoSlug) {
        String url = baseUrl + "/repositories/{workspace}/{repoSlug}";
        Map<String,String> vars = Map.of("workspace", workspace, "repoSlug", repoSlug);
        try {
            Map<String,Object> raw = restTemplate.getForObject(url, Map.class, vars);
            if (raw == null) {
                throw new BitbucketApiException("Respuesta vacía al obtener proyecto", null);
            }
            ProjectDto dto = new ProjectDto();
            dto.setId((String) raw.get("uuid"));
            dto.setName((String) raw.get("name"));
            Map<String,Object> links = (Map<String,Object>) raw.get("links");
            if (links != null) {
                Map<String,Object> html = (Map<String,Object>) links.get("html");
                if (html != null) {
                    dto.setWebUrl((String) html.get("href"));
                }
            }
            return dto;
        } catch (RestClientException ex) {
            log.error("Error al obtener proyecto {}/{}", workspace, repoSlug, ex);
            throw new BitbucketApiException("No se pudo recuperar datos del proyecto", ex);
        }
    }

    public List<CommitDto> getCommits(String workspace,
                                      String repoSlug,
                                      int pageSize,
                                      int maxPages) {
        List<Map<String,Object>> raws = fetchRawPages(
                "/repositories/{workspace}/{repoSlug}/commits",
                Map.of("workspace", workspace, "repoSlug", repoSlug),
                pageSize, maxPages
        );

        List<CommitDto> commits = new ArrayList<>();
        for (Map<String,Object> c : raws) {
            CommitDto dto = new CommitDto();

            // Mapeo de campos
            dto.setId((String)c.get("hash"));
            String msg = (String)c.get("message");
            dto.setMessage(msg);

            Map<String,Object> summary = (Map<String,Object>)c.get("summary");
            String rawSummary = summary != null ? (String)summary.get("raw") : null;
            String title = rawSummary != null
                    ? rawSummary.split("\n",2)[0]
                    : msg != null
                    ? msg.split("\n",2)[0]
                    : null;
            dto.setTitle(title);

            dto.setAuthoredDate((String)c.get("date"));

            Map<String,Object> author = (Map<String,Object>)c.get("author");
            if (author != null) {
                String raw = (String)author.get("raw");
                if (raw != null && raw.contains("<")) {
                    String name  = raw.substring(0, raw.indexOf("<")).trim();
                    String email = raw.substring(raw.indexOf("<")+1, raw.indexOf(">"));
                    dto.setAuthorName(name);
                    dto.setAuthorEmail(email);
                } else {
                    dto.setAuthorName(raw);
                }
            }

            Map<String,Object> links = (Map<String,Object>)c.get("links");
            if (links != null) {
                Map<String,Object> html = (Map<String,Object>)links.get("html");
                if (html != null) {
                    dto.setWebUrl((String)html.get("href"));
                }
            }

            commits.add(dto);
        }
        return commits;
    }

    public List<IssueDto> getIssues(String workspace,
                                    String repoSlug,
                                    int pageSize,
                                    int maxPages) {
        List<Map<String,Object>> raws = fetchRawPages(
                "/repositories/{workspace}/{repoSlug}/issues",
                Map.of("workspace", workspace, "repoSlug", repoSlug),
                pageSize, maxPages
        );

        List<IssueDto> issues = new ArrayList<>();
        for (Map<String,Object> i : raws) {
            IssueDto dto = new IssueDto();

            dto.setId(String.valueOf(i.get("id")));
            dto.setTitle((String)i.get("title"));

            Map<String,Object> content = (Map<String,Object>)i.get("content");
            if (content != null) {
                dto.setDescription((String)content.get("raw"));
            }

            dto.setState((String)i.get("state"));
            dto.setCreatedAt((String)i.get("created_on"));
            dto.setUpdatedAt((String)i.get("updated_on"));
            dto.setClosedAt((String)i.get("closed_on"));

            Object lbls = i.get("labels");
            dto.setLabels(lbls instanceof List<?> ? (List<String>)lbls : Collections.emptyList());

            dto.setVotes((Integer)i.getOrDefault("votes", 0));

            Map<String,Object> rep = (Map<String,Object>)i.get("reporter");
            if (rep != null) {
                UserDto u = new UserDto();
                u.setId((String)rep.get("uuid"));
                u.setUsername((String)rep.get("nickname"));
                u.setName((String)rep.get("display_name"));

                Map<String,Object> repLinks = (Map<String,Object>)rep.get("links");
                if (repLinks != null) {
                    Map<String,Object> avatar = (Map<String,Object>)repLinks.get("avatar");
                    if (avatar != null) {
                        u.setAvatarUrl((String)avatar.get("href"));
                    }
                    Map<String,Object> html = (Map<String,Object>)repLinks.get("html");
                    if (html != null) {
                        u.setWebUrl((String)html.get("href"));
                    }
                }
                dto.setUser(u);
            }

            Map<String,Object> ilinks = (Map<String,Object>)i.get("links");
            String commentsUrl = null;
            if (ilinks != null) {
                Map<String,Object> cmt = (Map<String,Object>)ilinks.get("comments");
                if (cmt != null) commentsUrl = (String)cmt.get("href");
            }
            if (commentsUrl != null) {
                dto.setComments(fetchComments(commentsUrl));
            }

            issues.add(dto);
        }
        return issues;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String,Object>> fetchRawPages(String pathTemplate,
                                                   Map<String,String> vars,
                                                   int pageSize,
                                                   int maxPages) {
        List<Map<String,Object>> all = new ArrayList<>();
        String next = UriComponentsBuilder
                .fromHttpUrl(baseUrl + pathTemplate)
                .queryParam("pagelen", pageSize)
                .buildAndExpand(vars)
                .toUriString();

        int pages = 0;
        while (next != null && pages < maxPages) {
            try {
                log.debug("GET {}", next);
                ResponseEntity<Map> resp = restTemplate.getForEntity(next, Map.class);
                Map<String,Object> body = resp.getBody();
                if (body == null) break;

                all.addAll((List<Map<String,Object>>)body.get("values"));
                next = (String)body.get("next");
                pages++;
            } catch (RestClientException ex) {
                log.error("Error paginando {}", next, ex);
                throw new BitbucketApiException("Error al paginar: " + ex.getMessage(), ex);
            }
        }
        return all;
    }

    @SuppressWarnings("unchecked")
    private List<CommentDto> fetchComments(String url) {
        List<CommentDto> comments = new ArrayList<>();
        String next = url;

        while (next != null) {
            try {
                log.debug("GET comments {}", next);
                ResponseEntity<Map> resp = restTemplate.getForEntity(next, Map.class);
                Map<String,Object> body = resp.getBody();
                if (body == null) break;

                for (Map<String,Object> c : (List<Map<String,Object>>)body.get("values")) {
                    CommentDto cDto = new CommentDto();
                    cDto.setId(String.valueOf(c.get("id")));

                    Map<String,Object> content = (Map<String,Object>)c.get("content");
                    if (content != null) {
                        cDto.setBody((String)content.get("raw"));
                    }

                    cDto.setCreatedAt((String)c.get("created_on"));
                    cDto.setUpdatedAt((String)c.get("updated_on"));
                    comments.add(cDto);
                }

                next = (String)body.get("next");
            } catch (RestClientException ex) {
                log.error("Error cargando comentarios {}", next, ex);
                throw new BitbucketApiException("Error al cargar comentarios: " + ex.getMessage(), ex);
            }
        }
        return comments;
    }
}
