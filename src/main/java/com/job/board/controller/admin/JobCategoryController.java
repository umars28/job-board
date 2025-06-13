package com.job.board.controller.admin;

import com.job.board.entity.JobCategory;
import com.job.board.model.CategoryRequest;
import com.job.board.service.JobCategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/jobs/category")
@Controller
public class JobCategoryController {
    private final JobCategoryService jobCategoryService;

    public JobCategoryController(JobCategoryService jobCategoryService) {
        this.jobCategoryService = jobCategoryService;
    }

    @GetMapping
    public String listCategory(Model model) {
        List<JobCategory> listJobCategory = jobCategoryService.getAllJobCategories();
        model.addAttribute("listJobCategory", listJobCategory);
        model.addAttribute("categoryRequest", new CategoryRequest());
        model.addAttribute("openCreateModal", false);
        model.addAttribute("openEditModal", false);
        return "/admin/category/index";
    }

    @PostMapping("/save")
    public String saveJobCategory(@Valid @ModelAttribute("categoryRequest") CategoryRequest categoryRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            List<JobCategory> listJobCategory = jobCategoryService.getAllJobCategories();
            model.addAttribute("listJobCategory", listJobCategory);
            model.addAttribute("openCreateModal", true);
            return "/admin/category/index";
        }

        try {
            jobCategoryService.saveJobCategory(categoryRequest);
            redirectAttributes.addFlashAttribute("message", "Kategori berhasil disimpan.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menyimpan kategori: " + e.getMessage());
        }
        return "redirect:/jobs/category";
    }

    @PostMapping("/update")
    public String updateJobCategory(@Valid @ModelAttribute("categoryRequest") CategoryRequest categoryRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listJobCategory", jobCategoryService.getAllJobCategories());
            model.addAttribute("openEditModal", true);
            model.addAttribute("editCategory", categoryRequest);
            return "/admin/category/index";
        }

        try {
            jobCategoryService.updateJobCategory(categoryRequest);
            redirectAttributes.addFlashAttribute("message", "Kategori berhasil diperbarui.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal memperbarui kategori: " + e.getMessage());
        }

        return "redirect:/jobs/category";
    }

    @GetMapping("/delete/{id}")
    public String deleteJobCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jobCategoryService.deleteJobCategoryById(id);
            redirectAttributes.addFlashAttribute("message", "Kategori berhasil dihapus.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus kategori: " + e.getMessage());
        }
        return "redirect:/jobs/category";
    }


}
