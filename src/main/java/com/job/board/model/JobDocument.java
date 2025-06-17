package com.job.board.model;

import lombok.Data;

@Data
public class JobDocument {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String status;
    private String companyName;
    private String categoryName;
}

