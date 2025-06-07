package com.job.board.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryRequest {
    private Long id;

    @NotBlank(message = "Category name harus diisi")
    private String name;
}
