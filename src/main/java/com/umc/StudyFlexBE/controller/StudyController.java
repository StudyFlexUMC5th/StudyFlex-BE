package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/studies")
public class StudyController {
    private final StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService) {

        this.studyService = studyService;
    }
    @GetMapping("/latest")
    public ResponseEntity<List<Study>> getLatestStudies() {
        List<Study> latestStudies = studyService.getLatestStudies();
        return ResponseEntity.ok(latestStudies);
    }

    @GetMapping("/open")
    public ResponseEntity<List<Study>> getOpenStudies() {
        List<Study> openStudies = studyService.getOpenStudies();
        return ResponseEntity.ok(openStudies);
    }

}
