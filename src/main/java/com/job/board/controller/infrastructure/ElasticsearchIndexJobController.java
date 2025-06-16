package com.job.board.controller.infrastructure;

import com.job.board.service.ElasticsearchIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/elasticsearch/jobs/index")
@RequiredArgsConstructor
public class ElasticsearchIndexJobController {
    private final ElasticsearchIndexService elasticsearchIndexService;

    @PostMapping("/create")
    public String createIndex() throws Exception {
        elasticsearchIndexService.createIndex();
        return "Index created";
    }

    @DeleteMapping("/delete")
    public String deleteIndex() throws Exception {
        elasticsearchIndexService.deleteIndex();
        return "Index deleted";
    }
}
