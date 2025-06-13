package com.job.board.controller.seeker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class HomepageController {
    @GetMapping
    public String homepage() {
        return "/public/homepage/home";
    }
}
