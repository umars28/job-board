package com.job.board.controller;

import com.job.board.entity.Job;
import com.job.board.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);
    //(Yield Injection)
    //@Autowired
    private JobRepository jobRepository;

    //Constructor Injection
    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @GetMapping
    public String list(Model model) {
        List<Job> jobList = jobRepository.findAll();
        model.addAttribute("jobs", jobList);
        return "jobs/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("job", new Job());
        return "jobs/form";
    }

    // @ModelAttribute explicit, can remove cause automatic, Binding data dari form, query param, path param ke objek Java
    // @RequestBody for json
    // @PathVariable Mengambil data dari URL path variable (dinamis pada URL)
    // @RequestParams Mengambil parameter dari query string atau form data
    @PostMapping
    public String save(@ModelAttribute Job job) {
        jobRepository.save(job);
        return "redirect:/jobs";
    }

    // @ModelAttribute explicit, can remove cause automatic, Binding data dari form, query param, path param ke objek Java
    // @PathVariable Mengambil data dari URL path variable (dinamis pada URL)
    // @RequestParams Mengambil parameter dari query string atau form data
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Job job = jobRepository.findById(id).orElseThrow();
        model.addAttribute("job", job);
        return "jobs/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Job job) {
        jobRepository.save(job);
        return "redirect:/jobs";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        jobRepository.deleteById(id);
        return "redirect:/jobs";
    }
}
