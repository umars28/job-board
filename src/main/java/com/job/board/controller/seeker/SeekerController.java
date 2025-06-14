package com.job.board.controller.seeker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/seeker")
@Controller("publicSeekerController")
public class SeekerController {
    @GetMapping("/profile")
    public String profile() {
        return "/public/seeker/profile";
    }

    @GetMapping("/job/applied")
    public String appliedJob() {
        return "/public/seeker/applied-job";
    }

    @GetMapping("/notification")
    public String notification() {
        return "/public/seeker/notification";
    }

    @GetMapping("/chat")
    public String chat() {
        return "/public/seeker/chat";
    }
}
