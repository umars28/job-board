package com.job.board.controller;

import com.job.board.entity.JobTag;
import com.job.board.model.TagRequest;
import com.job.board.service.JobTagService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/jobs/tag")
@Controller
public class JobTagController {

    private final JobTagService jobTagService;

    public JobTagController(JobTagService jobTagService) {
        this.jobTagService = jobTagService;
    }

    @GetMapping
    public String jobTags(Model model) {
        List<JobTag> jobTagList = jobTagService.getAllJobTags();
        model.addAttribute("jobTagList", jobTagList);
        model.addAttribute("tagRequest", new TagRequest());
        return "/admin/tag/index";
    }

    @PostMapping("/save")
    public String saveJobTag(@Valid @ModelAttribute("tagRequest") TagRequest tagRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            List<JobTag> jobTagList = jobTagService.getAllJobTags();
            model.addAttribute("jobTagList", jobTagList);
            return "/admin/tag/index";
        }
        try {
            jobTagService.saveJobTag(tagRequest);
            redirectAttributes.addFlashAttribute("message", "Tag berhasil disimpan.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menyimpan Tag: " + e.getMessage());
        }
        return "redirect:/jobs/tag";
    }

    @PostMapping("/update")
    public String updateJobTag(@Valid @ModelAttribute("tagRequest") TagRequest tagRequest, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            List<JobTag> jobTagList = jobTagService.getAllJobTags();
            model.addAttribute("jobTagList", jobTagList);
            return "redirect:" + request.getHeader("Referer");
        }

        try {
            jobTagService.updateJobTag(tagRequest);
            redirectAttributes.addFlashAttribute("message", "Tag berhasil diupdate.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal mengupdate Tag: " + e.getMessage());
        }
        return "redirect:/jobs/tag";

    }

    @GetMapping("/delete/{id}")
    public String deleteJobTag(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jobTagService.deleteJobTagById(id);
            redirectAttributes.addFlashAttribute("message", "Tag berhasil dihapus.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus tag: " + e.getMessage());
        }
        return "redirect:/jobs/tag";
    }
}
