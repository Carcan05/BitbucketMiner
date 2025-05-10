package com.aiss.bitbucketminer.client;

import com.aiss.bitbucketminer.dto.*;
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

    @SuppressWarnings("unchecked")
    public ProjectDto getProject(String workspace, String repoSlug) {
        String url = baseUrl + "/repositories/{workspace}/{repoSlug}";
        Map<String,String> vars = Map.of("workspace", workspace, "repoSlug", repoSlug);
        try {
            Map<String,Object> raw = restTemplate.getForObject(url, Map.class, vars);
            if (raw == null) throw new BitbucketApiException("Respuesta vacía", null);
            ProjectDto dto = new ProjectDto();
            dto.setId((String) raw.get("uuid"));
            dto.setName((String) raw.get("name"));
            Map<String,Object> links = (Map<String,Object>) raw.get("links");
            if (links != null) {
                Map<String,Object> html = (Map<String,Object>) links.get("html");
                dto.setWebUrl(html == null ? null : (String) html.get("href"));
            }
            return dto;
        } catch (RestClientException ex) {
            log.error("Error proyecto {}/{}", workspace, repoSlug, ex);
            throw new BitbucketApiException("No se pudo recuperar proyecto", ex);
        }
    }

    public List<CommitDto> getCommits(String workspace, String repoSlug, int pageSize, int maxPages) {
        var raws = fetchRawPages("/repositories/{workspace}/{repoSlug}/commits",
                Map.of("workspace", workspace, "repoSlug", repoSlug),
                pageSize, maxPages);
        List<CommitDto> commits = new ArrayList<>();
        for (var c : raws) {
            CommitDto dto = new CommitDto();
            dto.setId((String) c.get("hash"));
            String msg = (String) c.get("message");
            dto.setMessage(msg);
            Map<String,Object> summary = (Map<String,Object>) c.get("summary");
            String rawSum = summary != null ? (String) summary.get("raw") : null;
            dto.setTitle(rawSum != null ? rawSum.split("\n",2)[0] : (msg==null?null:msg.split("\n",2)[0]));
            dto.setAuthoredDate((String) c.get("date"));
            Map<String,Object> author = (Map<String,Object>) c.get("author");
            if (author != null) {
                String raw = (String) author.get("raw");
                if (raw != null && raw.contains("<")) {
                    dto.setAuthorName(raw.substring(0, raw.indexOf("<")).trim());
                    dto.setAuthorEmail(raw.substring(raw.indexOf("<")+1, raw.indexOf(">")));
                } else {
                    dto.setAuthorName(raw);
                }
            }
            Map<String,Object> links = (Map<String,Object>) c.get("links");
            if (links != null) {
                Map<String,Object> html = (Map<String,Object>) links.get("html");
                dto.setWebUrl(html == null ? null : (String) html.get("href"));
            }
            commits.add(dto);
        }
        return commits;
    }

    public List<IssueDto> getIssues(String workspace, String repoSlug, int pageSize, int maxPages) {
        var raws = fetchRawPages("/repositories/{workspace}/{repoSlug}/issues",
                Map.of("workspace", workspace, "repoSlug", repoSlug),
                pageSize, maxPages);
        List<IssueDto> issues = new ArrayList<>();
        for (var i : raws) {
            IssueDto dto = new IssueDto();
            dto.setId(String.valueOf(i.get("id")));
            dto.setTitle((String) i.get("title"));
            Map<String,Object> content = (Map<String,Object>) i.get("content");
            dto.setDescription(content == null ? null : (String) content.get("raw"));
            dto.setState((String) i.get("state"));
            dto.setCreatedAt((String) i.get("created_on"));
            dto.setUpdatedAt((String) i.get("updated_on"));
            dto.setClosedAt((String) i.get("closed_on"));
            dto.setLabels(i.get("labels") instanceof List<?> ?
                    (List<String>) i.get("labels") : List.of());
            dto.setUpvotes((Integer) i.getOrDefault("votes", 0));
            dto.setDownvotes(0);

            // author ← reporter
            Map<String,Object> rep = (Map<String,Object>) i.get("reporter");
            if (rep != null) {
                UserDto u = new UserDto();
                u.setId((String) rep.get("uuid"));
                u.setUsername((String) rep.get("nickname"));
                u.setName((String) rep.get("display_name"));
                Map<String,Object> rl = (Map<String,Object>) rep.get("links");
                if (rl != null) {
                    Map<String,Object> av = (Map<String,Object>) rl.get("avatar");
                    if (av != null) u.setAvatarUrl((String) av.get("href"));
                    Map<String,Object> html = (Map<String,Object>) rl.get("html");
                    if (html != null) u.setWebUrl((String) html.get("href"));
                }
                dto.setAuthor(u);
            }

            // assignee
            Map<String,Object> asg = (Map<String,Object>) i.get("assignee");
            if (asg != null) {
                UserDto a = new UserDto();
                a.setId((String) asg.get("uuid"));
                a.setUsername((String) asg.get("nickname"));
                a.setName((String) asg.get("display_name"));
                @SuppressWarnings("unchecked")
                Map<String,Object> alinks = (Map<String,Object>) asg.get("links");
                if (alinks != null) {
                    Map<String,Object> av = (Map<String,Object>) alinks.get("avatar");
                    if (av != null) a.setAvatarUrl((String) av.get("href"));
                    Map<String,Object> html = (Map<String,Object>) alinks.get("html");
                    if (html != null) a.setWebUrl((String) html.get("href"));
                }
                dto.setAssignee(a);
            }

            // webUrl
            Map<String,Object> ilinks = (Map<String,Object>) i.get("links");
            if (ilinks != null) {
                Map<String,Object> html = (Map<String,Object>) ilinks.get("html");
                dto.setWebUrl(html == null ? null : (String) html.get("href"));
            }

            // comments
            if (ilinks != null && ilinks.get("comments") instanceof Map) {
                String commentsUrl = (String) ((Map<?,?>) ilinks.get("comments")).get("href");
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
                all.addAll((List<Map<String,Object>>) body.get("values"));
                next = (String) body.get("next");
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
                for (var c : (List<Map<String,Object>>) body.get("values")) {
                    CommentDto cd = new CommentDto();
                    cd.setId(String.valueOf(c.get("id")));
                    cd.setBody((String) ((Map<?,?>) c.get("content")).get("raw"));
                    cd.setCreatedAt((String) c.get("created_on"));
                    cd.setUpdatedAt((String) c.get("updated_on"));
                    // author
                    Map<String,Object> user = (Map<String,Object>) c.get("user");
                    if (user != null) {
                        UserDto u = new UserDto();
                        u.setId((String) user.get("uuid"));
                        u.setUsername((String) user.get("nickname"));
                        u.setName((String) user.get("display_name"));
                        Map<String,Object> ulinks = (Map<String,Object>) user.get("links");
                        if (ulinks != null) {
                            Map<String,Object> av = (Map<String,Object>) ulinks.get("avatar");
                            if (av != null) u.setAvatarUrl((String) av.get("href"));
                            Map<String,Object> html = (Map<String,Object>) ulinks.get("html");
                            if (html != null) u.setWebUrl((String) html.get("href"));
                        }
                        cd.setAuthor(u);
                    }
                    comments.add(cd);
                }
                next = (String) body.get("next");
            } catch (RestClientException ex) {
                log.error("Error cargando comentarios {}", next, ex);
                throw new BitbucketApiException("Error al cargar comentarios: " + ex.getMessage(), ex);
            }
        }
        return comments;
    }
}
