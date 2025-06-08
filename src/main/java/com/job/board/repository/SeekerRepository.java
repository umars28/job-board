package com.job.board.repository;

import com.job.board.entity.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeekerRepository extends JpaRepository<JobSeeker, Long> {
}
