package com.job.board.service;

import com.job.board.entity.Company;
import com.job.board.entity.JobSeeker;
import com.job.board.entity.User;
import com.job.board.enums.Role;
import com.job.board.repository.CompanyRepository;
import com.job.board.repository.SeekerRepository;
import com.job.board.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    private final SeekerRepository seekerRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyRepository companyRepository, SeekerRepository seekerRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.seekerRepository = seekerRepository;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public void updateProfile(User formUser, String username) {
        User existingUser = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setFirstName(formUser.getFirstName());
        existingUser.setLastName(formUser.getLastName());
        existingUser.setEmail(formUser.getEmail());

        if (formUser.getPassword() != null && !formUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(formUser.getPassword()));
        }

        if (existingUser.getRole() == Role.COMPANY && existingUser.getCompany() != null) {
            Company existingCompany = existingUser.getCompany();
            Company formCompany = formUser.getCompany();

            if (formCompany != null) {
                existingCompany.setName(formUser.getFirstName()+ " " +formUser.getLastName());
                existingCompany.setAddress(formCompany.getAddress());
                existingCompany.setWebsite(formCompany.getWebsite());
                companyRepository.save(existingCompany);
            }
        } else if (existingUser.getRole() == Role.JOB_SEEKER && existingUser.getJobSeeker() != null) {
            JobSeeker existingJobSeeker = existingUser.getJobSeeker();
            JobSeeker formJobSeeker = formUser.getJobSeeker();

            if (formJobSeeker != null) {
                existingJobSeeker.setFullName(formUser.getFirstName()+ " " +formUser.getLastName());
                existingJobSeeker.setPhone(formJobSeeker.getPhone());
                existingJobSeeker.setResumeUrl(formJobSeeker.getResumeUrl());
                seekerRepository.save(existingJobSeeker);
            }
        }

        userRepository.save(existingUser);
    }
}
