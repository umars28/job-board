package com.job.board.service;

import com.job.board.entity.Job;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ElasticsearchDocIndexService {

    private final RestClient restClient;

    public ElasticsearchDocIndexService(RestClient restClient) {
        this.restClient = restClient;
    }

    public void indexJob(Job job) throws IOException {
        String jobJson = "{ \"id\": \"" + job.getId() + "\", \"title\": \"" + job.getTitle() + "\", \"description\": \"" + job.getDescription() + "\" }";

        Request request = new Request("PUT", "/jobs/_doc/" + job.getId());
        request.setJsonEntity(jobJson);
        restClient.performRequest(request);
    }

    public void deleteJobFromIndex(Long id) throws IOException {
        Request request = new Request("DELETE", "/jobs/_doc/" + id);
        restClient.performRequest(request);
    }
}
