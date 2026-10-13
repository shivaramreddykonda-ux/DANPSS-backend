package com.danpss.controller;

import com.danpss.entity.JobPosting;
import com.danpss.repository.JobPostingRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobPostingController {
    private final JobPostingRepository repository;

    public JobPostingController(JobPostingRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<JobPosting> getAllJobs() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobPosting createJob(@Valid @RequestBody JobPosting job) {
        if (job.getTitle() == null || job.getTitle().isBlank()
                || job.getCompany() == null || job.getCompany().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Job title and company are required");
        }
        return repository.save(job);
    }
}
