package com.job.board.controller.admin;

import com.job.board.entity.Company;
import com.job.board.model.CompanyRequest;
import com.job.board.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/users")
@Controller
public class CompanyController {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/company")
    public String companyList(Model model) {
        List<Company> companyList = companyService.getAllCompanies();
        model.addAttribute("company", companyList);
        return "/admin/company/index";
    }

    @GetMapping("/company/create")
    public String createCompany() {
        return "/admin/company/create";
    }

    @PostMapping("/company/save")
    public String saveCompany(@ModelAttribute CompanyRequest companyRequest, RedirectAttributes redirectAttributes) {
        try {
            auditLogger.info("AUDIT - Request POST /users/company/save received");
            companyService.saveCompany(companyRequest);
            redirectAttributes.addFlashAttribute("message", "Company berhasil ditambahkan.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menambah company: " + e.getMessage());
        }
        return "redirect:/users/company";
    }

    @GetMapping("/company/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CompanyRequest companyRequest = companyService.getCompanyRequestById(id);
        model.addAttribute("company", companyRequest);
        model.addAttribute("companyId", id);
        return "admin/company/edit";
    }

    @PostMapping("/company/update/{id}")
    public String updateSeeker(@PathVariable Long id,
                               @ModelAttribute CompanyRequest companyRequest, RedirectAttributes redirectAttributes) {
        try {
            auditLogger.info("AUDIT - Request POST /users/company/update for company id={}", id);
            companyService.updateCompany(id, companyRequest);
            redirectAttributes.addFlashAttribute("message", "Company berhasil diupdate.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal mengupdate company: " + e.getMessage());
        }
        return "redirect:/users/company";
    }

    @GetMapping("/company/delete/{id}")
    public String deleteJobSeeker(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            auditLogger.info("AUDIT - Request GET /users/company/delete for company id={}", id);
            companyService.deleteCompanyById(id);
            redirectAttributes.addFlashAttribute("message", "Company deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed delete company: " + e.getMessage());
        }
        return "redirect:/users/company";
    }
}
