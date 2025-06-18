package com.job.board.service;

import com.job.board.entity.JobCategory;
import com.job.board.entity.JobTag;
import com.job.board.model.CategoryRequest;
import com.job.board.model.TagRequest;
import com.job.board.repository.JobTagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobTagService {
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private final JobTagRepository jobTagRepository;

    public JobTagService(JobTagRepository jobTagRepository) {
        this.jobTagRepository = jobTagRepository;
    }

    public List<JobTag> getAllJobTags() {
        return jobTagRepository.findAllWithJobs();
    }

    public void saveJobTag(TagRequest tagRequest) {
        JobTag jobTag = new JobTag();
        jobTag.setName(tagRequest.getName());
        jobTagRepository.save(jobTag);
        auditLogger.info("AUDIT - Saved new JobTag with name='{}'", tagRequest.getName());
    }

    public void updateJobTag(TagRequest tagRequest) {
        JobTag tag = jobTagRepository.findById(tagRequest.getId())
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        String oldName = tag.getName();
        tag.setName(tagRequest.getName());
        jobTagRepository.save(tag);
        auditLogger.info(
                "AUDIT - Updated JobTag id={} from name='{}' to name='{}'",
                tagRequest.getId(),
                oldName,
                tagRequest.getName()
        );

    }

    public void deleteJobTagById(Long id) {
        jobTagRepository.deleteById(id);
        auditLogger.info("AUDIT - Deleted JobTag with id={}", id);
    }

    public List<String> getTagsWithJobs() {
        return jobTagRepository.findTagsWithJobs();
    }
}
