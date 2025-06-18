package com.job.board.service;

import com.job.board.entity.Job;
import com.job.board.entity.JobCategory;
import com.job.board.model.CategoryRequest;
import com.job.board.repository.JobCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobCategoryService {
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");

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
        JobCategory savedCategory = jobCategoryRepository.save(jobCategory);
        auditLogger.info(
                "AUDIT - Saved new job category with id={} and name='{}'",
                savedCategory.getId(),
                savedCategory.getName()
        );
    }

    public void deleteJobCategoryById(Long id) {
        JobCategory category = jobCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        String categoryName = category.getName();

        jobCategoryRepository.deleteById(id);

        auditLogger.info(
                "AUDIT - Deleted JobCategory id={} with name='{}'",
                id,
                categoryName
        );
    }

    public void updateJobCategory(CategoryRequest categoryRequest) {
        JobCategory category = jobCategoryRepository.findById(categoryRequest.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        String oldName = category.getName();
        category.setName(categoryRequest.getName());
        jobCategoryRepository.save(category);

        auditLogger.info(
                "AUDIT - Updated JobCategory id={} from '{}' to '{}'",
                category.getId(),
                oldName,
                category.getName()
        );
    }

    public List<String> getCategoriesWithJobs() {
        return jobCategoryRepository.findCategoryNamesWithJobs();
    }
}
