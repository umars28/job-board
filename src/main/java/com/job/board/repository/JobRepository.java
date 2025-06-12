package com.job.board.repository;

import com.job.board.entity.Company;
import com.job.board.entity.Job;
import com.job.board.entity.JobApplication;
import com.job.board.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Repository tidak perlu karena sudah auto using kalau extends interfacenya
public interface JobRepository extends JpaRepository<Job, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Job j WHERE j.company = :company")
    void deleteByCompany(@Param("company") Company company);

    @Query
    List<Job> findByStatus(JobStatus status);

    @Query("SELECT DISTINCT j FROM Job j " +
            "JOIN FETCH j.company " +
            "JOIN FETCH j.category " +
            "LEFT JOIN j.tags " +
            "WHERE j.status = :status")
    List<Job> findByStatusWithDetails(@Param("status") JobStatus status);

    @Query("SELECT DISTINCT j FROM Job j " +
            "JOIN FETCH j.company " +
            "JOIN FETCH j.category " +
            "LEFT JOIN j.tags")
    List<Job> findAllWithDetails();

    @Query("SELECT DISTINCT j FROM Job j " +
            "JOIN FETCH j.company c " +
            "JOIN FETCH j.category " +
            "LEFT JOIN j.tags " +
            "WHERE c.user.username = :username")
    List<Job> findByCompanyUsernameWithDetails(@Param("username") String username);

    @Query("SELECT DISTINCT j FROM Job j " +
            "JOIN FETCH j.company c " +
            "JOIN FETCH j.category " +
            "LEFT JOIN j.tags " +
            "WHERE c.user.username = :username AND j.status = :status")
    List<Job> findByCompanyUsernameAndStatusWithDetails(@Param("username") String username, @Param("status") JobStatus status);

    long countAllByStatus(JobStatus status);

    int countByCompanyAndStatus(Company company, JobStatus status);
}
