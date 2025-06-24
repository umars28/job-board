package com.job.board.controller.admin;

import com.job.board.entity.Job;
import com.job.board.service.ChatService;
import com.job.board.service.JobService;
import com.job.board.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/company")
public class CompanyChatController {

    private final ChatService chatService;
    private final JobService jobService;
    private final AuthUtil authUtil;

    public CompanyChatController(ChatService chatService, JobService jobService, AuthUtil authUtil) {
        this.chatService = chatService;
        this.jobService = jobService;
        this.authUtil = authUtil;
    }

    @GetMapping("/start-chat/{seekerId}/{jobId}")
    public String startChat(
            @PathVariable Long seekerId,
            @PathVariable Long jobId,
            Principal principal
    ) {

        Job job = jobService.getJobById(jobId);
        authUtil.authorizeCompanyAccessToJob(job);
        authUtil.authorizeSeekerAppliedToJobForCompany(seekerId, jobId);
        String redirectUrl = chatService.startChat(seekerId, jobId, principal);
        return "redirect:" + redirectUrl;
    }
}
