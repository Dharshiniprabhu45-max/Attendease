package com.attendease.attendease.controller;

import com.attendease.attendease.entity.Subject;
import com.attendease.attendease.service.SubjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    // Get all subjects
    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    // Get subject by ID
    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Long id) {
        return subjectService.getSubjectById(id);
    }

    // Create a new subject
    @PostMapping
    public Subject createSubject(@RequestBody Subject subject) {
        return subjectService.createSubject(subject);
    }

    // Update subject details
    @PutMapping("/{id}")
    public Subject updateSubject(
            @PathVariable Long id,
            @RequestBody Subject subject) {
        return subjectService.updateSubject(id, subject);
    }

    // Delete a subject
    @DeleteMapping("/{id}")
    public String deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return "Subject deleted successfully";
    }
}