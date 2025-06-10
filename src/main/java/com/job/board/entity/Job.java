package com.job.board.entity;

import com.job.board.enums.JobStatus;
import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private LocalDateTime postedAt;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private LocalDateTime expiredAt;

    @ManyToOne
    private Company company;

    @ManyToOne
    private JobCategory category;

    @ManyToMany
    @JoinTable(
            name = "job_tags",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<JobTag> tags;

    @OneToMany(mappedBy = "job")
    private List<JobApplication> jobApplications;

}
