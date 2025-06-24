package com.job.board.repository;

import com.job.board.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByCompanyIdAndSeekerIdAndJobId(Long companyId, Long seekerId, Long jobId);

    List<Conversation> findBySeekerId(Long seekerId);
}

