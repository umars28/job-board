package com.job.board.repository;

import com.job.board.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository tidak perlu karena sudah auto using kalau extends interfacenya
public interface JobRepository extends JpaRepository<Job, Long> {
}
