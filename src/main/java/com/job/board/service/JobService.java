package com.job.board.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.board.entity.Company;
import com.job.board.entity.Job;
import com.job.board.enums.JobStatus;
import com.job.board.model.JobDocument;
import com.job.board.repository.JobCategoryRepository;
import com.job.board.repository.JobRepository;
import com.job.board.util.AuthUtil;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.job.board.repository.specification.JobSpecification.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class JobService {
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");

    private final JobRepository jobRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final CompanyService companyService;
    private final AuthUtil authUtil;
    private final ElasticsearchDocIndexService elasticsearchDocIndexService;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public JobService(JobRepository jobRepository, JobCategoryRepository jobCategoryRepository, CompanyService companyService, AuthUtil authUtil, ElasticsearchDocIndexService elasticsearchDocIndexService, RestClient restClient, ObjectMapper objectMapper) {
        this.jobRepository = jobRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.companyService = companyService;
        this.authUtil = authUtil;
        this.elasticsearchDocIndexService = elasticsearchDocIndexService;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public List<Job> getJobsFiltered(String status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        boolean isCompany = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_COMPANY"));

        JobStatus jobStatus = null;
        if (status != null) {
            jobStatus = JobStatus.valueOf(status);
        }

        if (isCompany) {
            if (jobStatus != null) {
                return jobRepository.findByCompanyUsernameAndStatusWithDetails(username, jobStatus);
            }
            return jobRepository.findByCompanyUsernameWithDetails(username);
        } else {
            if (jobStatus != null) {
                return jobRepository.findByStatusWithDetails(jobStatus);
            }
            return jobRepository.findAllWithDetails();
        }
    }

    public Job jobDetails(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
    }

    @Transactional
    public void archiveJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
        authUtil.authorizeCompanyAccessToJob(job);
        JobStatus oldStatus = job.getStatus();
        job.setStatus(JobStatus.ARCHIVED);
        jobRepository.save(job);

        auditLogger.info(
                "AUDIT - Job id={} archived (old status={})",
                id, oldStatus
        );

        try {
            elasticsearchDocIndexService.deleteJobFromIndex(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete index job in Elasticsearch", e);
        }
    }

    @Transactional
    public void restoreJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
        authUtil.authorizeCompanyAccessToJob(job);
        JobStatus oldStatus = job.getStatus();
        job.setStatus(JobStatus.OPEN);
        jobRepository.save(job);

        auditLogger.info(
                "AUDIT - Job id={} restored (old status={})",
                id, oldStatus
        );

        try {
            elasticsearchDocIndexService.indexJob(job);
        } catch (Exception e) {
            throw new RuntimeException("Failed to restore index job in Elasticsearch", e);
        }
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
    }

    @Transactional
    public void saveJob(Job job) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Company company = companyService.findByUsername(username);
        job.setCompany(company);

        if (job.getCategory() != null && job.getCategory().getId() != null) {
            jobCategoryRepository.findById(job.getCategory().getId())
                    .ifPresent(job::setCategory);
        } else {
            job.setCategory(null);
        }

        jobRepository.save(job);

        auditLogger.info(
                "AUDIT - Saved new Job with id={} title='{}' by company='{}' (username={})",
                job.getId(),
                job.getTitle(),
                company.getName(),
                username
        );

        try {
            elasticsearchDocIndexService.indexJob(job);
        } catch (Exception e) {
            throw new RuntimeException("Failed to index job in Elasticsearch", e);
        }
    }

    @Transactional
    public void updateJob(Long id, Job updatedJob) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));

        String oldTitle = existingJob.getTitle();

        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setStatus(updatedJob.getStatus());
        existingJob.setPostedAt(updatedJob.getPostedAt());
        existingJob.setExpiredAt(updatedJob.getExpiredAt());

        if (updatedJob.getCategory() != null && updatedJob.getCategory().getId() != null) {
            jobCategoryRepository.findById(updatedJob.getCategory().getId())
                    .ifPresent(existingJob::setCategory);
        } else {
            existingJob.setCategory(null);
        }

        existingJob.setTags(updatedJob.getTags());

        jobRepository.save(existingJob);

        auditLogger.info(
                "AUDIT - Updated Job id={} from title='{}' to title='{}'",
                id,
                oldTitle,
                updatedJob.getTitle()
        );

        try {
            elasticsearchDocIndexService.indexJob(existingJob);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update index job in Elasticsearch", e);
        }
    }

    public long getTotalActiveJobPostings() {
        return jobRepository.countAllByStatus((JobStatus.OPEN));
    }

    public int countActiveJobsByCompany(Company company) {
        return jobRepository.countByCompanyAndStatus(company, JobStatus.OPEN);
    }

    public List<String> getDistinctLocations() {
        return jobRepository.findDistinctLocations();
    }

    public Page<Job> findOpenJobs(String location, String category, List<String> tags, String keyword, Pageable pageable) {
        Specification<Job> spec = (root, query, cb) -> cb.conjunction();

        spec = spec.and(statusOpen());

        if (location != null && !location.isBlank()) {
            spec = spec.and(locationEqual(location));
        }
        if (category != null && !category.isBlank()) {
            spec = spec.and(categoryEqual(category));
        }
        if (tags != null && !tags.isEmpty()) {
            spec = spec.and(tagsIn(tags));
        }
        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and(keywordLike(keyword));
        }

        return jobRepository.findAll(spec, pageable);
    }

    public Page<JobDocument> findOpenJobsWithElasticsearch(
            String location,
            String category,
            List<String> tags,
            String keyword,
            Pageable pageable
    ) throws IOException {

        Map<String, Object> boolQuery = new HashMap<>();
        List<Object> must = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            Map<String, Object> multiMatch = Map.of(
                    "multi_match", Map.of(
                            "query", keyword.toLowerCase(),
                            "fields", List.of(
                                    "title.ngram",
                                    "title.edge",
                                    "description.ngram",
                                    "description.edge"
                            )
                    )
            );
            must.add(multiMatch);
        }

        if (location != null && !location.isBlank()) {
            Map<String, Object> term = Map.of(
                    "term", Map.of("location", location)
            );
            must.add(term);
        }

        if (category != null && !category.isBlank()) {
            Map<String, Object> term = Map.of(
                    "term", Map.of("categoryName", category)
            );
            must.add(term);
        }

        if (tags != null && !tags.isEmpty()) {
            Map<String, Object> terms = Map.of(
                    "terms", Map.of("tags", tags)
            );
            must.add(terms);
        }

        boolQuery.put("must", must);
        Map<String, Object> query = Map.of("bool", boolQuery);

        Map<String, Object> fullQuery = new HashMap<>();
        fullQuery.put("query", query);
        fullQuery.put("from", pageable.getPageNumber() * pageable.getPageSize());
        fullQuery.put("size", pageable.getPageSize());

        // Convert ke JSON
        String jsonQuery = objectMapper.writeValueAsString(fullQuery);

        // Panggil Elasticsearch
        Request request = new Request("POST", "/jobs/_search");
        request.setJsonEntity(jsonQuery);

        Response response = restClient.performRequest(request);
        String json = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

        // Parse hasil
        List<JobDocument> jobs = new ArrayList<>();
        JsonNode hits = objectMapper.readTree(json).path("hits").path("hits");
        for (JsonNode hit : hits) {
            JobDocument job = objectMapper.treeToValue(hit.path("_source"), JobDocument.class);
            jobs.add(job);
        }

        long total = objectMapper.readTree(json).path("hits").path("total").path("value").asLong();

        return new PageImpl<>(jobs, pageable, total);
    }


}
