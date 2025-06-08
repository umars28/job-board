package com.job.board.service;

import com.job.board.entity.JobCategory;
import com.job.board.entity.JobTag;
import com.job.board.model.CategoryRequest;
import com.job.board.model.TagRequest;
import com.job.board.repository.JobTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobTagService {
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
    }

    public void updateJobTag(TagRequest tagRequest) {
        JobTag tag = jobTagRepository.findById(tagRequest.getId())
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        tag.setName(tagRequest.getName());
        jobTagRepository.save(tag);
    }

    public void deleteJobTagById(Long id) {
        jobTagRepository.deleteById(id);
    }
}
