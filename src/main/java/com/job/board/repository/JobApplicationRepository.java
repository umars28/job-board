package com.job.board.repository;

import com.job.board.entity.Company;
import com.job.board.entity.JobApplication;
import com.job.board.entity.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    @Query("SELECT ja.jobSeeker.id AS seekerId, COUNT(ja.id) AS appCount " +
            "FROM JobApplication ja GROUP BY ja.jobSeeker.id")
    List<Object[]> findApplicationCountsGroupedBySeeker();

    @Modifying
    @Transactional
    void deleteByJobSeeker(JobSeeker jobSeeker);

    @Modifying
    @Transactional
    @Query("DELETE FROM JobApplication ja WHERE ja.job.company = :company")
    void deleteByCompany(@Param("company") Company company);
}
