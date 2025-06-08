package com.job.board.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompanyRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String website;
}
