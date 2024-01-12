package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.request.StudyNoticeReq;
import com.umc.StudyFlexBE.dto.request.StudyReq;
import com.umc.StudyFlexBE.dto.response.*;
import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.service.StudyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('USER')")
@RequestMapping("/app/studies")
public class StudyController {
    private final StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService) {

        this.studyService = studyService;
    }

    @PostMapping
    public BaseResponse<?> createStudy(@ModelAttribute StudyReq study){
        Long id = studyService.createStudy(study, getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, id);
    }

    @GetMapping("/checkName")
    public BaseResponse<?> checkDuplicateStudyName(@RequestParam String study_name){
        studyService.checkDuplicateStudyName(study_name);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS,"사용가능한 스터디 이름입니다.");
    }

    @GetMapping("/{study_id}/checkAuthority")
    public BaseResponse<?> checkAuthority(@PathVariable Long study_id){
        StudyAuthorityType studyAuthorityType = studyService.checkAuthority(study_id, getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyAuthorityType);
    }

    @PostMapping("/{study_id}/participation")
    public BaseResponse<?> participation(@PathVariable Long study_id){
        studyService.participation(study_id, getEmail());
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


    @GetMapping("/ranking")
    public ResponseEntity<List<Study>> getStudyRanking() {
        List<Study> rankedStudies = studyService.getRankedStudies();
        return ResponseEntity.ok(rankedStudies);
    }

    @PostMapping("/{study_id}/postNotice")
    public BaseResponse<?> postStudyNotice(
            @PathVariable Long study_id,
            @RequestBody @Valid StudyNoticeReq studyNoticeReq){

        studyService.postStudyNotice(study_id,getEmail(),studyNoticeReq);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "공지사항이 등록되었습니다.");
    }

    @GetMapping("/{study_id}/notice/{notice_id}")
    public BaseResponse<?> getStudyNotice(@PathVariable Long study_id, @PathVariable Long notice_id){

        StudyNoticeRes studyNotice = studyService.getStudyNotice(study_id, notice_id, getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyNotice);
    }

    @DeleteMapping("/{study_id}/notice/{notice_id}")
    public BaseResponse<?> deleteStudyNotice(@PathVariable Long study_id, @PathVariable Long notice_id){

        studyService.deleteStudyNotice(study_id, notice_id, getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @GetMapping("/{study_id}/notice")
    public BaseResponse<?> getStudyNotices(@PathVariable Long study_id){
        StudyNoticesInfoRes studyNotices = studyService.getStudyNotices(study_id, getEmail());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS,studyNotices);
    }

    @GetMapping("/{study_id}/completed")
    public BaseResponse<?> checkCompletedStudyWeek(@PathVariable Long study_id, @RequestParam int week){
        ProgressRes progressReq = studyService.checkCompletedStudyWeek(study_id, week, getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, progressReq);
    }

    @GetMapping("/{study_id}/progress")
    public BaseResponse<?> getStudyProgressList(@PathVariable Long study_id){
        List<ProgressRes> studyProgressList = studyService.getStudyProgressList(study_id, getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS,studyProgressList);
    }

    @GetMapping("/{study_id}/details")
    public BaseResponse<?> getStudyDetail(@PathVariable Long study_id){
        StudyDetailRes studyDetail = studyService.getStudyDetail(study_id);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS,studyDetail);
    }

    private String getEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
