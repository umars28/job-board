package com.job.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashbordController {
    @GetMapping
    public String list(Model model) {
        return "admin/dashboard/dashboard";
    }
}
