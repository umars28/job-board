package com.job.board.service;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ElasticsearchIndexService {

    private final RestClient restClient;

    public void createIndex() throws IOException {
        String mappingJson = new String(
                new ClassPathResource("elasticsearch/job-index.json").getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        Request request = new Request("PUT", "/jobs");
        request.setJsonEntity(mappingJson);
        restClient.performRequest(request);
    }

    public void deleteIndex() throws IOException {
        Request request = new Request("DELETE", "/jobs");
        restClient.performRequest(request);
    }
}
