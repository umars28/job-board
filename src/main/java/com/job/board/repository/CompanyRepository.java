package com.job.board.repository;

import com.job.board.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT c FROM Company c " +
            "LEFT JOIN FETCH c.jobs " +
            "LEFT JOIN FETCH c.user")
    List<Company> findAllWithJobsAndUser();
}
