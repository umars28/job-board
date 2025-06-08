package com.job.board.service;

import com.job.board.entity.JobSeeker;
import com.job.board.entity.User;
import com.job.board.enums.Role;
import com.job.board.model.SeekerRequest;
import com.job.board.repository.JobApplicationRepository;
import com.job.board.repository.SeekerRepository;
import com.job.board.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeekerService {
    private final SeekerRepository seekerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public SeekerService(SeekerRepository seekerRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, JobApplicationRepository jobApplicationRepository) {
        this.seekerRepository = seekerRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public List<JobSeeker> getAllSeeker() {
        List<JobSeeker> seekers = seekerRepository.findAll();

        // Ambil map id -> count
        List<Object[]> counts = jobApplicationRepository.findApplicationCountsGroupedBySeeker();
        Map<Long, Long> countMap = new HashMap<>();
        for (Object[] row : counts) {
            Long seekerId = (Long) row[0];
            Long appCount = (Long) row[1];
            countMap.put(seekerId, appCount);
        }

        // Set field transient ke setiap seeker
        for (JobSeeker seeker : seekers) {
            seeker.setApplicationCount(countMap.getOrDefault(seeker.getId(), 0L));
        }

        return seekers;
    }

    public SeekerRequest getSeekerRequestById(Long seekerId) {
        JobSeeker seeker = seekerRepository.findById(seekerId)
                .orElseThrow(() -> new RuntimeException("Seeker not found"));

        User user = seeker.getUser();

        SeekerRequest request = new SeekerRequest();
        request.setUsername(user.getUsername());
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        request.setEmail(user.getEmail());
        request.setPhone(seeker.getPhone());
        request.setResumeUrl(seeker.getResumeUrl());

        return request;
    }

    @Transactional
    public void saveSeeker(SeekerRequest seekerRequest) {
        User user = new User();
        user.setUsername(seekerRequest.getUsername());
        user.setFirstName(seekerRequest.getFirstName());
        user.setLastName(seekerRequest.getLastName());
        user.setEmail(seekerRequest.getEmail());
        user.setPassword(passwordEncoder.encode("default"));
        user.setRole(Role.JOB_SEEKER);

        User savedUser = userRepository.save(user);

        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setUser(savedUser);
        jobSeeker.setFullName(seekerRequest.getFirstName() + " " + seekerRequest.getLastName());
        jobSeeker.setPhone(seekerRequest.getPhone());
        jobSeeker.setResumeUrl(seekerRequest.getResumeUrl());
        seekerRepository.save(jobSeeker);
    }

    @Transactional
    public void updateSeeker(Long seekerId, SeekerRequest request) {
        JobSeeker seeker = seekerRepository.findById(seekerId)
                .orElseThrow(() -> new RuntimeException("Seeker not found"));

        User user = seeker.getUser();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        seeker.setFullName(request.getFirstName() + " " + request.getLastName());
        seeker.setPhone(request.getPhone());
        seeker.setResumeUrl(request.getResumeUrl());

        userRepository.save(user);
        seekerRepository.save(seeker);
    }

    @Transactional
    public void deleteSeekerById(Long id) {
        JobSeeker seeker = seekerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobSeeker not found"));

        jobApplicationRepository.deleteByJobSeeker(seeker);
        User user = seeker.getUser();
        seekerRepository.delete(seeker);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
