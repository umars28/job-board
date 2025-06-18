package com.job.board.controller.admin;

import com.job.board.entity.JobSeeker;
import com.job.board.model.SeekerRequest;
import com.job.board.service.SeekerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/users")
@Controller("adminSeekerController")
public class SeekerController {
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private final SeekerService seekerService;

    public SeekerController(SeekerService seekerService) {
        this.seekerService = seekerService;
    }

    @GetMapping("/seeker")
    public String jobSeekerList(Model model) {
        List<JobSeeker> seekerList = seekerService.getAllSeeker();
        model.addAttribute("seekers", seekerList);
        return "/admin/seeker/index";
    }

    @GetMapping("/seeker/create")
    public String createJobSeeker() {
        return "/admin/seeker/create";
    }

    @PostMapping("/seeker/save")
    public String saveJobSeeker(@ModelAttribute SeekerRequest seekerRequest, RedirectAttributes redirectAttributes) {
        auditLogger.info("AUDIT - Request POST /users/seeker/save with email={}", seekerRequest.getEmail());
        try {
            seekerService.saveSeeker(seekerRequest);
            redirectAttributes.addFlashAttribute("message", "Job seeker berhasil ditambahkan.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menambah job seeker: " + e.getMessage());
        }
        return "redirect:/users/seeker";
    }

    @GetMapping("/seeker/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        SeekerRequest seekerRequest = seekerService.getSeekerRequestById(id);
        model.addAttribute("seeker", seekerRequest);
        model.addAttribute("seekerId", id);
        return "admin/seeker/edit";
    }

    @PostMapping("/seeker/update/{id}")
    public String updateSeeker(@PathVariable Long id,
                               @ModelAttribute SeekerRequest seekerRequest, RedirectAttributes redirectAttributes) {
        auditLogger.info("AUDIT - Request POST /users/seeker/update/{} with email={}", id, seekerRequest.getEmail());
        try {
            seekerService.updateSeeker(id, seekerRequest);
            redirectAttributes.addFlashAttribute("message", "Job seeker berhasil diupdate.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal mengupdate job seeker: " + e.getMessage());
        }
        return "redirect:/users/seeker";
    }

    @GetMapping("/seeker/delete/{id}")
    public String deleteJobSeeker(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        auditLogger.info("AUDIT - Request GET /users/seeker/delete/{}", id);
        try {
            seekerService.deleteSeekerById(id);
            redirectAttributes.addFlashAttribute("message", "Seeker deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed delete seeker: " + e.getMessage());
        }
        return "redirect:/users/seeker";
    }

}
