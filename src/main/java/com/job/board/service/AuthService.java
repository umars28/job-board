package com.job.board.service;

import com.job.board.entity.User;
import com.job.board.model.LoginRequest;
import com.job.board.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean login(LoginRequest loginRequest, HttpSession session) {
        Optional<User> userData = userRepository.findByUsername(loginRequest.getUsername());

        if(userData.isPresent()) {
            User user = userData.get();

            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String roleName = "ROLE_" +user.getRole().name();
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                null,
                                List.of(new SimpleGrantedAuthority(roleName))
                        );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
                return true;
            }
        }
        return false;
    }

    public boolean isJobSeeker(Authentication auth) {
        if (auth == null) {
            return false;
        }

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_JOB_SEEKER"));
    }


    public void logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        session.invalidate();
    }
}
