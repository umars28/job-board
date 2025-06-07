package com.job.board.controller;

import com.job.board.entity.User;
import com.job.board.enums.Role;
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
    private final PasswordEncoder passwordEncoder;

    public DevSeederController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
}
