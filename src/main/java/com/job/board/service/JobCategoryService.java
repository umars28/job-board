package com.job.board.service;

import com.job.board.entity.Job;
import com.job.board.entity.JobCategory;
import com.job.board.model.CategoryRequest;
import com.job.board.repository.JobCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobCategoryService {
    private final JobCategoryRepository jobCategoryRepository;

    public JobCategoryService(JobCategoryRepository jobCategoryRepository) {
        this.jobCategoryRepository = jobCategoryRepository;
    }

    public List<JobCategory> getAllJobCategories() {
        return jobCategoryRepository.findAllWithJobs();
    }

    public void saveJobCategory(CategoryRequest categoryRequest) {
        JobCategory jobCategory = new JobCategory();
        jobCategory.setName(categoryRequest.getName());
        jobCategoryRepository.save(jobCategory);
    }

    public void deleteJobCategoryById(Long id) {
        jobCategoryRepository.deleteById(id);
    }

    public void updateJobCategory(CategoryRequest categoryRequest) {
        JobCategory category = jobCategoryRepository.findById(categoryRequest.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(categoryRequest.getName());
        jobCategoryRepository.save(category);
    }
}
