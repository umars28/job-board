package com.job.board.repository;

import com.job.board.entity.Company;
import com.job.board.entity.JobApplication;
import com.job.board.entity.JobSeeker;
import com.job.board.enums.ApplicantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobId(Long jobId);

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

    List<JobApplication> findByJobIdAndApplicantStatus(Long jobId, ApplicantStatus applicantStatus);

    @Query("SELECT COUNT(a) FROM JobApplication a WHERE a.job.company = :company")
    int countByJobCompany(@Param("company") Company company);

    @Query("SELECT a.job.id FROM JobApplication a WHERE a.jobSeeker.id = :jobSeekerId")
    List<Long> findJobIdsByJobSeekerId(@Param("jobSeekerId") Long jobSeekerId);
}
