package com.job.board.service;

import com.job.board.entity.Conversation;
import com.job.board.entity.User;
import com.job.board.enums.Role;
import com.job.board.repository.ConversationRepository;
import com.job.board.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserService userService;
    private final SeekerService seekerService;

    public ChatService(ConversationRepository conversationRepository,
                       UserRepository userRepository,
                       JwtService jwtService, UserService userService, SeekerService seekerService) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userService = userService;
        this.seekerService = seekerService;
    }

    public List<Conversation> getSeekerConversation(Long seekerdId) {
        return conversationRepository.findBySeekerId(seekerdId);
    }

    public String openChat(Long companyId, Long jobId) {
        Long seekerId = seekerService.getCurrentJobSeekerId();

        Conversation conversation = conversationRepository
                .findByCompanyIdAndSeekerIdAndJobId(companyId, seekerId, jobId)
                .orElseThrow(() -> new IllegalStateException(
                        "Conversation not found. The company must start the chat first."
                ));

        String token = jwtService.generateToken(seekerId, "seeker", conversation.getId(), jobId);

        return String.format(
                "http://localhost:8082/chat?token=%s",
                token
        );
    }

    public String startChat(Long seekerId, Long jobId, Principal principal) {
        Long companyId = getCompanyIdFromPrincipal(principal);

        Conversation conversation = conversationRepository
                .findByCompanyIdAndSeekerIdAndJobId(companyId, seekerId, jobId)
                .orElseGet(() -> {
                    Conversation newConv = new Conversation();
                    newConv.setCompanyId(companyId);
                    newConv.setSeekerId(seekerId);
                    newConv.setJobId(jobId);
                    return conversationRepository.save(newConv);
                });

        String token = jwtService.generateToken(companyId, "company", conversation.getId(), jobId);

        return String.format(
                "http://localhost:8082/chat?token=%s",
                token
        );
    }

    private Long getCompanyIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getRole() != Role.COMPANY) {
            throw new AccessDeniedException("Only company can start chat");
        }

        if (user.getCompany() == null) {
            throw new IllegalStateException("Company not found for user");
        }

        return user.getCompany().getId();
    }
}
