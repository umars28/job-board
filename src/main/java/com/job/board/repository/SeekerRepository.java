package com.job.board.repository;

import com.job.board.entity.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeekerRepository extends JpaRepository<JobSeeker, Long> {
    Optional<JobSeeker> findByUserUsername(String username);
}
