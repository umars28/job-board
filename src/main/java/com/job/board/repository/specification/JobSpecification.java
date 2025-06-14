package com.job.board.repository.specification;

import com.job.board.entity.Job;
import com.job.board.enums.JobStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class JobSpecification {
    public static Specification<Job> statusOpen() {
        return (root, query, cb) -> cb.equal(root.get("status"), JobStatus.OPEN);
    }

    public static Specification<Job> locationEqual(String location) {
        return (root, query, cb) -> cb.equal(root.get("location"), location);
    }

    public static Specification<Job> categoryEqual(String category) {
        return (root, query, cb) -> cb.equal(root.get("category").get("name"), category);
    }

    public static Specification<Job> tagsIn(List<String> tags) {
        return (root, query, cb) -> {
            Join<Object, Object> tagsJoin = root.join("tags", JoinType.LEFT);
            return tagsJoin.get("name").in(tags);
        };
    }

    public static Specification<Job> keywordLike(String keyword) {
        return (root, query, cb) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }
}
