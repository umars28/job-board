package com.job.board.repository;

import com.job.board.entity.Company;
import com.job.board.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

//@Repository tidak perlu karena sudah auto using kalau extends interfacenya
public interface JobRepository extends JpaRepository<Job, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Job j WHERE j.company = :company")
    void deleteByCompany(@Param("company") Company company);
}
