package com.job.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.board.entity.Job;
import com.job.board.entity.JobTag;
import com.job.board.model.JobDocument;
import com.job.board.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticsearchIndexService {

    private final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private final RestClient restClient;
    private final JobRepository jobRepository;
    private final ObjectMapper objectMapper;

    public void createIndex() throws IOException {
        Request headRequest = new Request("HEAD", "/jobs");
        Response headResponse = restClient.performRequest(headRequest);
        int statusCode = headResponse.getStatusLine().getStatusCode();

        if (statusCode == 200) {
            auditLogger.info("AUDIT - Index 'jobs' already exists, skip creating.");
            System.out.println("Index jobs already exists, skip creating.");
        } else {
            String mappingJson = new String(
                    new ClassPathResource("elasticsearch/job-index.json").getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            Request request = new Request("PUT", "/jobs");
            request.setJsonEntity(mappingJson);
            restClient.performRequest(request);
            auditLogger.info("AUDIT - Index 'jobs' created successfully.");
        }
    }

    public void deleteIndex() throws IOException {
        Request request = new Request("HEAD", "/jobs");
        Response response = restClient.performRequest(request);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            Request deleteRequest = new Request("DELETE", "/jobs");
            restClient.performRequest(deleteRequest);
            auditLogger.info("AUDIT - Index 'jobs' deleted successfully.");
        } else {
            System.out.println("Index jobs not found, skip delete.");
            auditLogger.info("AUDIT - Index 'jobs' not found, skip deleting.");
        }
    }

    public int reindexAllJobs() throws IOException {
        deleteIndex();
        createIndex();
        List<Job> jobs = jobRepository.findAll();
        bulkIndexJobs(jobs);
        auditLogger.info("AUDIT - Bulk reindex completed for {} jobs.", jobs.size());
        return jobs.size();
    }

    private void bulkIndexJobs(List<Job> jobs) throws IOException {
        StringBuilder bulkRequestBody = new StringBuilder();

        for (Job job : jobs) {
            JobDocument doc = new JobDocument();
            doc.setId(job.getId());
            doc.setTitle(job.getTitle());
            doc.setDescription(job.getDescription());
            doc.setLocation(job.getLocation());
            doc.setStatus(job.getStatus().name());
            doc.setCompanyName(job.getCompany().getName());
            doc.setCategoryName(
                    job.getCategory() != null ? job.getCategory().getName() : null
            );
            List<String> tagNames = job.getTags().stream()
                    .map(JobTag::getName)
                    .toList();
            doc.setTags(tagNames);

            bulkRequestBody.append("{ \"index\" : { \"_index\" : \"jobs\", \"_id\" : \"")
                    .append(job.getId()).append("\" } }\n");
            bulkRequestBody.append(objectMapper.writeValueAsString(doc)).append("\n");
        }

        Request request = new Request("POST", "/_bulk");
        request.setJsonEntity(bulkRequestBody.toString());
        restClient.performRequest(request);
        auditLogger.info("AUDIT - Bulk indexing finished for {} jobs.", jobs.size());
    }
}
