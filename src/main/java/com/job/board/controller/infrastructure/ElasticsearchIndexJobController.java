package com.job.board.controller.infrastructure;

import com.job.board.service.ElasticsearchIndexService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/elasticsearch/jobs/index")
@RequiredArgsConstructor
public class ElasticsearchIndexJobController {
    private final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private final ElasticsearchIndexService elasticsearchIndexService;

    @PostMapping("/create")
    public String createIndex() throws Exception {
        auditLogger.info("AUDIT - Request POST /elasticsearch/jobs/index/create");
        elasticsearchIndexService.createIndex();
        return "✅ Index created.";
    }

    @DeleteMapping("/delete")
    public String deleteIndex() throws Exception {
        auditLogger.info("AUDIT - Request DELETE /elasticsearch/jobs/index/delete");
        elasticsearchIndexService.deleteIndex();
        return "Index deleted";
    }

    @PostMapping("/reindex-all")
    public String reindexAll() throws Exception {
        auditLogger.info("AUDIT - Request POST /elasticsearch/jobs/index/reindex-all");
        int total = elasticsearchIndexService.reindexAllJobs();
        return "✅ Reindex selesai. Total jobs: " + total;
    }
}
