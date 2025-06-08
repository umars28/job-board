package com.job.board.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TagRequest {
    private Long id;

    @NotBlank(message = "Tag name harus diisi")
    private String name;
}
