package com.job.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.board.entity.Job;
import com.job.board.model.JobDocument;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ElasticsearchDocIndexService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ElasticsearchDocIndexService(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public void indexJob(Job job) throws IOException {
        JobDocument doc = new JobDocument();
        doc.setId(job.getId());
        doc.setTitle(job.getTitle());
        doc.setDescription(job.getDescription());
        doc.setLocation(job.getLocation());
        doc.setStatus(job.getStatus().name());
        doc.setCompanyName(job.getCompany().getName());
        doc.setCategoryName(job.getCategory() != null ? job.getCategory().getName() : null);

        String jobJson = objectMapper.writeValueAsString(doc);

        Request request = new Request("PUT", "/jobs/_doc/" + job.getId());
        request.setJsonEntity(jobJson);
        restClient.performRequest(request);
    }

    public void deleteJobFromIndex(Long id) throws IOException {
        Request request = new Request("DELETE", "/jobs/_doc/" + id);
        restClient.performRequest(request);
    }
}
