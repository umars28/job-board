package com.job.board.controller.seeker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/job")
@Controller("seekerJobController")
public class JobController {
    @GetMapping("/list")
    public String jobList() {
        return "/public/job/list";
    }

    @GetMapping("/detail")
    public String jobDetail() {
        return "/public/job/detail";
    }
}
