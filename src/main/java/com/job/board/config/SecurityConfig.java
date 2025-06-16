package com.job.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/elasticsearch/**"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/login", "/css/**","/dev-seeder/**", "/job/list", "/job/detail/**", "/vendor/**", "/js/**", "/elasticsearch/**").permitAll()
                        .requestMatchers("/jobs/list", "/dashboard").hasAnyRole("ADMIN", "COMPANY")
                        .requestMatchers("/users/**", "/jobs/category/**", "/jobs/tag/**").hasRole("ADMIN")
                        .requestMatchers("/jobs/create", "/jobs/edit/**", "/jobs/restore/**", "/jobs/archive/**", "/notification/**").hasRole("COMPANY")
                        .requestMatchers("/seeker/**", "/job/apply/**").hasRole("JOB_SEEKER")
                        .anyRequest().authenticated()
                )
                .formLogin(FormLoginConfigurer::disable)
                .logout(LogoutConfigurer::disable)
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/login"))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}
