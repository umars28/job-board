package com.job.board.service;

import com.job.board.entity.Company;
import com.job.board.entity.User;
import com.job.board.enums.Role;
import com.job.board.repository.CompanyRepository;
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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
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
        }

        userRepository.save(existingUser);
    }
}
