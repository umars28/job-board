package com.job.board.controller.seeker;

import com.job.board.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/seeker")
@Controller
public class SeekerChatController {
    private final ChatService chatService;

    public SeekerChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/open-chat/{companyId}")
    public String openChat(@PathVariable Long companyId) {
        String redirectUrl = chatService.openChat(companyId);
        return "redirect:" + redirectUrl;
    }
}
