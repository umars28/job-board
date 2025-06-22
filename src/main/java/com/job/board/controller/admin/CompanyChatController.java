package com.job.board.controller.admin;

import com.job.board.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/company")
public class CompanyChatController {

    private final ChatService chatService;

    public CompanyChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/start-chat/{seekerId}")
    public String startChat(@PathVariable Long seekerId, Principal principal) {
        String redirectUrl = chatService.startChat(seekerId, principal);
        return "redirect:" + redirectUrl;
    }
}
