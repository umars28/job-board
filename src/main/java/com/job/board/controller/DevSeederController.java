package com.job.board.controller;

import com.job.board.entity.JobCategory;
import com.job.board.entity.User;
import com.job.board.enums.Role;
import com.job.board.repository.JobCategoryRepository;
import com.job.board.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dev-seeder")
public class DevSeederController {

    private final UserRepository userRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final PasswordEncoder passwordEncoder;

    public DevSeederController(UserRepository userRepository, JobCategoryRepository jobCategoryRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/seed-admin")
    @ResponseBody
    public String seedAdmin() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setFirstName("Umar");
            admin.setEmail("umarsabirin369@gmail.com");
            admin.setLastName("Sabirin");
            admin.setUsername("admin");
            admin.setRole(Role.ADMIN);
            admin.setPassword(passwordEncoder.encode("passwordsecret"));

            userRepository.save(admin);
            return "✅ Admin user seeded.";
        }

        return "ℹ️ Admin user already exists.";
    }

    @GetMapping("/seed-job-category")
    @ResponseBody
    public String seedJobCategory() {
        if (jobCategoryRepository.count() == 0) {
            jobCategoryRepository.save(new JobCategory("IT"));
            jobCategoryRepository.save(new JobCategory("Marketing"));
            jobCategoryRepository.save(new JobCategory("Finance"));
            jobCategoryRepository.save(new JobCategory("HR"));
            jobCategoryRepository.save(new JobCategory("Design"));

            jobCategoryRepository.save(new JobCategory("Data Science"));
            jobCategoryRepository.save(new JobCategory("Cybersecurity"));
            jobCategoryRepository.save(new JobCategory("Frontend Developer"));
            jobCategoryRepository.save(new JobCategory("Backend Developer"));
            jobCategoryRepository.save(new JobCategory("Fullstack Developer"));

            jobCategoryRepository.save(new JobCategory("Mobile Developer"));
            jobCategoryRepository.save(new JobCategory("DevOps Engineer"));
            jobCategoryRepository.save(new JobCategory("QA Engineer"));
            jobCategoryRepository.save(new JobCategory("UI/UX Designer"));
            jobCategoryRepository.save(new JobCategory("Product Manager"));

            jobCategoryRepository.save(new JobCategory("Project Manager"));
            jobCategoryRepository.save(new JobCategory("Business Analyst"));
            jobCategoryRepository.save(new JobCategory("SEO Specialist"));
            jobCategoryRepository.save(new JobCategory("Content Writer"));
            jobCategoryRepository.save(new JobCategory("Social Media Manager"));

            jobCategoryRepository.save(new JobCategory("Digital Marketing"));
            jobCategoryRepository.save(new JobCategory("Copywriter"));
            jobCategoryRepository.save(new JobCategory("Sales Executive"));
            jobCategoryRepository.save(new JobCategory("Customer Support"));
            jobCategoryRepository.save(new JobCategory("Technical Support"));

            jobCategoryRepository.save(new JobCategory("Accounting"));
            jobCategoryRepository.save(new JobCategory("Auditor"));
            jobCategoryRepository.save(new JobCategory("Banking"));
            jobCategoryRepository.save(new JobCategory("Investment Analyst"));
            jobCategoryRepository.save(new JobCategory("Tax Consultant"));

            jobCategoryRepository.save(new JobCategory("Legal Advisor"));
            jobCategoryRepository.save(new JobCategory("Paralegal"));
            jobCategoryRepository.save(new JobCategory("Logistics"));
            jobCategoryRepository.save(new JobCategory("Supply Chain Manager"));
            jobCategoryRepository.save(new JobCategory("Procurement"));

            jobCategoryRepository.save(new JobCategory("Manufacturing"));
            jobCategoryRepository.save(new JobCategory("Mechanical Engineer"));
            jobCategoryRepository.save(new JobCategory("Electrical Engineer"));
            jobCategoryRepository.save(new JobCategory("Civil Engineer"));
            jobCategoryRepository.save(new JobCategory("Chemical Engineer"));

            jobCategoryRepository.save(new JobCategory("Architecture"));
            jobCategoryRepository.save(new JobCategory("Construction"));
            jobCategoryRepository.save(new JobCategory("Real Estate Agent"));
            jobCategoryRepository.save(new JobCategory("Healthcare"));
            jobCategoryRepository.save(new JobCategory("Nurse"));

            jobCategoryRepository.save(new JobCategory("Doctor"));
            jobCategoryRepository.save(new JobCategory("Pharmacist"));
            jobCategoryRepository.save(new JobCategory("Lab Technician"));
            jobCategoryRepository.save(new JobCategory("Veterinarian"));
            jobCategoryRepository.save(new JobCategory("Psychologist"));

            jobCategoryRepository.save(new JobCategory("Teacher"));
            jobCategoryRepository.save(new JobCategory("Lecturer"));
            jobCategoryRepository.save(new JobCategory("Researcher"));
            jobCategoryRepository.save(new JobCategory("Translator"));
            jobCategoryRepository.save(new JobCategory("Librarian"));

            jobCategoryRepository.save(new JobCategory("Event Planner"));
            jobCategoryRepository.save(new JobCategory("Hospitality"));
            jobCategoryRepository.save(new JobCategory("Travel Consultant"));
            jobCategoryRepository.save(new JobCategory("Chef"));
            jobCategoryRepository.save(new JobCategory("Bartender"));

            jobCategoryRepository.save(new JobCategory("Fitness Trainer"));
            jobCategoryRepository.save(new JobCategory("Photographer"));
            jobCategoryRepository.save(new JobCategory("Video Editor"));
            jobCategoryRepository.save(new JobCategory("Animator"));
            jobCategoryRepository.save(new JobCategory("Game Developer"));
            return "✅ Job Category Seeded.";
        }
        return "ℹ️ Job Category Already Exists In The DB.";

    }
}
