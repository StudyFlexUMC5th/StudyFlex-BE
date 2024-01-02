package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.response.BaseResponse;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.response.study.AuthorityType;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/studies")
public class StudyController {
    private final StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService) {

        this.studyService = studyService;
    }

    @GetMapping("/checkName")
    public BaseResponse<?> checkDuplicateStudyName(@RequestParam String study_name){
        studyService.checkDuplicateStudyName(study_name);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS,"사용가능한 스터디 이름입니다.");
    }

    @GetMapping("/{study_id}/checkAuthority")
    public BaseResponse<?> checkAuthority(@PathVariable Long study_id, @RequestParam Long member_id){
        AuthorityType authorityType = studyService.checkAuthority(study_id, member_id);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, authorityType);
    }

    @PostMapping("/{study_id}/participation")
    public BaseResponse<?> participation(@PathVariable Long study_id, @AuthenticationPrincipal Member member){
        studyService.participation(study_id, member);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "스터디 참여에 성공했습니다.");
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
