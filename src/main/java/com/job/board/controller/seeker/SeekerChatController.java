package com.job.board.controller.seeker;

import com.job.board.entity.Job;
import com.job.board.service.ChatService;
import com.job.board.service.JobService;
import com.job.board.service.SeekerService;
import com.job.board.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/seeker")
@Controller
public class SeekerChatController {
    private final ChatService chatService;
    private final JobService jobService;
    private final AuthUtil authUtil;
    private final SeekerService seekerService;

    public SeekerChatController(ChatService chatService, JobService jobService, AuthUtil authUtil, SeekerService seekerService) {
        this.chatService = chatService;
        this.jobService = jobService;
        this.authUtil = authUtil;
        this.seekerService = seekerService;
    }

    @GetMapping("/open-chat/{companyId}/{jobId}")
    public String openChat(@PathVariable Long companyId, @PathVariable Long jobId) {
        Job job = jobService.getJobById(jobId);
        authUtil.authorizeJobBelongsToCompany(jobId, companyId, job);
        Long seekerId = seekerService.getCurrentJobSeekerId();
        authUtil.authorizeSeekerAppliedToJobForSeeker(seekerId, jobId);
        String redirectUrl = chatService.openChat(companyId, jobId);
        return "redirect:" + redirectUrl;
    }
}
