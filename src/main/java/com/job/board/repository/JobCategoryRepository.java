package com.job.board.repository;

import com.job.board.entity.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    @Query("SELECT c FROM JobCategory c LEFT JOIN FETCH c.jobs")
    List<JobCategory> findAllWithJobs();

    @Query("SELECT DISTINCT c.name FROM JobCategory c JOIN c.jobs j")
    List<String> findCategoryNamesWithJobs();

}
