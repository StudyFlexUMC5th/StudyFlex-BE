package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.request.StudyNoticeReq;
import com.umc.StudyFlexBE.dto.request.StudyReq;
import com.umc.StudyFlexBE.dto.response.*;
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
        try {
            StudyRes studyRes = studyService.createStudy(study, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/checkName")
    public BaseResponse<?> checkDuplicateStudyName(@RequestParam String study_name){
        try {
            studyService.checkDuplicateStudyName(study_name);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "사용가능한 스터디 이름입니다.");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{study_id}/checkAuthority")
    public BaseResponse<?> checkAuthority(@PathVariable Long study_id){
        try{
            StudyAuthorityType studyAuthorityType = studyService.checkAuthority(study_id, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyAuthorityType);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/{study_id}/participation")
    public BaseResponse<?> participation(@PathVariable Long study_id){
        try {
            StudyParticipationRes res = StudyParticipationRes.builder()
                    .success(studyService.participation(study_id, getEmail()))
                    .build();
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, res);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
    @GetMapping("/latest")
    public BaseResponse<?> getLatestStudies() {
        try {
            List<StudyMainPageResponseDto> latestStudies = studyService.getLatestStudies();
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, latestStudies);
        }catch (BaseException e){
            return  new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/open")
    public BaseResponse<?> getOpenStudies() {
        try {
            List<StudyMainPageResponseDto> openStudies = studyService.getOpenStudies();
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, openStudies);
        }catch (BaseException e){
            return  new BaseResponse<>(e.getStatus());
        }
    }
    @GetMapping("/ranking")
    public BaseResponse<?> getStudyRanking() {
        try{
            List<StudyMainPageResponseDto> rankedStudies = studyService.getRankedStudies();
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, rankedStudies);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/{study_id}/postNotice")
    public BaseResponse<?> postStudyNotice(
            @PathVariable Long study_id,
            @RequestBody @Valid StudyNoticeReq studyNoticeReq){
        try {
            studyService.postStudyNotice(study_id, getEmail(), studyNoticeReq);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "공지사항이 등록되었습니다.");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{study_id}/notice/{notice_id}")
    public BaseResponse<?> getStudyNotice(@PathVariable Long study_id, @PathVariable Long notice_id){
        try {
            StudyNoticeRes studyNotice = studyService.getStudyNotice(study_id, notice_id, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyNotice);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/{study_id}/notice/{notice_id}")
    public BaseResponse<?> deleteStudyNotice(@PathVariable Long study_id, @PathVariable Long notice_id){
        try {
            studyService.deleteStudyNotice(study_id, notice_id, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{study_id}/notice")
    public BaseResponse<?> getStudyNotices(@PathVariable Long study_id){
        try {
            StudyNoticesInfoRes studyNotices = studyService.getStudyNotices(study_id, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyNotices);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{study_id}/completed")
    public BaseResponse<?> checkCompletedStudyWeek(@PathVariable Long study_id, @RequestParam int week){
        try {
            ProgressRes progressReq = studyService.checkCompletedStudyWeek(study_id, week, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, progressReq);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{study_id}/progress")
    public BaseResponse<?> getStudyProgressList(@PathVariable Long study_id){
        try {
            List<ProgressRes> studyProgressList = studyService.getStudyProgressList(study_id, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyProgressList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{study_id}/details")
    public BaseResponse<?> getStudyDetail(@PathVariable Long study_id){
        try {
            StudyDetailRes studyDetail = studyService.getStudyDetail(study_id);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyDetail);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    private String getEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
