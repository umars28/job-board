package com.job.board.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SeekerRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String resumeUrl;
}

