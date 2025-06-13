package com.job.board.repository;

import com.job.board.entity.JobCategory;
import com.job.board.entity.JobTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobTagRepository extends JpaRepository<JobTag, Long> {
    @Query("SELECT c FROM JobTag c LEFT JOIN FETCH c.jobs")
    List<JobTag> findAllWithJobs();

    @Query("SELECT DISTINCT t.name FROM JobTag t JOIN t.jobs j WHERE j.status = 'OPEN'")
    List<String> findTagsWithJobs();
}
