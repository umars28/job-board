package com.job.board.service;

import com.job.board.entity.Company;
import com.job.board.entity.User;
import com.job.board.enums.Role;
import com.job.board.model.CompanyRequest;
import com.job.board.repository.CompanyRepository;
import com.job.board.repository.JobApplicationRepository;
import com.job.board.repository.JobRepository;
import com.job.board.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;

    public CompanyService(CompanyRepository companyRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, JobApplicationRepository jobApplicationRepository, JobRepository jobRepository) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobRepository = jobRepository;
    }

    public List<Company> getAllCompanies() {
        auditLogger.info("AUDIT - Start retrieving companies");
        List<Company> companies =  companyRepository.findAllWithJobsAndUser();
        auditLogger.info("AUDIT - Retrieved companies count: {}", companies.size());
        return companies;
    }

    @Transactional
    public void saveCompany(CompanyRequest companyRequest) {
        User user = new User();
        user.setUsername(companyRequest.getUsername());
        user.setFirstName(companyRequest.getFirstName());
        user.setLastName(companyRequest.getLastName());
        user.setEmail(companyRequest.getEmail());
        user.setPassword(passwordEncoder.encode("default"));
        user.setRole(Role.COMPANY);

        User savedUser = userRepository.save(user);

        Company company = new Company();
        company.setUser(savedUser);
        company.setName(companyRequest.getFirstName() + " " + companyRequest.getLastName());
        company.setAddress(companyRequest.getAddress());
        company.setWebsite(companyRequest.getWebsite());
        Company savedCompany = companyRepository.save(company);

        auditLogger.info(
                "AUDIT - Created company id={} name={} with user id={} username={}",
                savedCompany.getId(),
                savedCompany.getName(),
                savedUser.getId(),
                savedUser.getUsername()
        );

    }

    public CompanyRequest getCompanyRequestById(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        User user = company.getUser();

        CompanyRequest request = new CompanyRequest();
        request.setUsername(user.getUsername());
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        request.setEmail(user.getEmail());
        request.setAddress(company.getAddress());
        request.setWebsite(company.getWebsite());

        auditLogger.info(
                "AUDIT - Fetched CompanyRequest for companyId={} (username={})",
                companyId,
                user.getUsername()
        );

        return request;
    }

    @Transactional
    public void updateCompany(Long companyId, CompanyRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        User user = company.getUser();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        company.setName(request.getFirstName() + " " + request.getLastName());
        company.setAddress(request.getAddress());
        company.setWebsite(request.getWebsite());

        userRepository.save(user);
        companyRepository.save(company);

        auditLogger.info(
                "AUDIT - Updated company id={} (new username={}, name={}, address={}, website={})",
                companyId,
                user.getUsername(),
                company.getName(),
                company.getAddress(),
                company.getWebsite()
        );
    }

    @Transactional
    public void deleteCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        jobApplicationRepository.deleteByCompany(company);
        jobRepository.deleteByCompany(company);
        User user = company.getUser();
        companyRepository.delete(company);
        if (user != null) {
            userRepository.delete(user);
        }

        auditLogger.info(
                "AUDIT - Deleted company id={} (username={}) and related jobs/applications",
                id,
                user != null ? user.getUsername() : "null"
        );
    }

    public long getTotalCompanies() {
        return companyRepository.count();
    }

    public Company findByUsername(String username) {
        return companyRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Company not found for user: " + username));
    }
}
