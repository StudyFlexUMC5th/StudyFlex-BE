package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.request.StudyNoticeReq;
import com.umc.StudyFlexBE.dto.request.StudyReq;
import com.umc.StudyFlexBE.dto.response.*;
import com.umc.StudyFlexBE.service.StudyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/studies")
public class StudyController {
    private final StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService) {

        this.studyService = studyService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_CERTIFIED')")
    public BaseResponse<?> createStudy(@ModelAttribute StudyReq study){
        try {
            StudyRes studyRes = studyService.createStudy(study, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/checkName")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_CERTIFIED')")
    public BaseResponse<?> checkDuplicateStudyName(@RequestParam String study_name){
        try {
            studyService.checkDuplicateStudyName(study_name);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "사용가능한 스터디 이름입니다.");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{study_id}/checkAuthority")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_CERTIFIED')")
    public BaseResponse<?> checkAuthority(@PathVariable Long study_id){
        try{
            StudyAuthorityType studyAuthorityType = studyService.checkAuthority(study_id, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyAuthorityType);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/{study_id}/participation")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_CERTIFIED')")
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

    // 아래 3개는 메인 페이지에 위치하기 때문에 회원가입 안 한 사람도 볼 수 있도록
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
        try {
            List<RankResponseDto> rankedStudies = studyService.getRankedStudies();
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, rankedStudies);
        } catch (BaseException e) {

            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/{study_id}/postNotice")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_CERTIFIED')")
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
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_CERTIFIED')")
    public BaseResponse<?> getStudyNotice(@PathVariable Long study_id, @PathVariable Long notice_id){
        try {
            StudyNoticeRes studyNotice = studyService.getStudyNotice(study_id, notice_id, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyNotice);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/{study_id}/notice/{notice_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_CERTIFIED')")
    public BaseResponse<?> deleteStudyNotice(@PathVariable Long study_id, @PathVariable Long notice_id){
        try {
            studyService.deleteStudyNotice(study_id, notice_id, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{study_id}/notice")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_CERTIFIED')")
    public BaseResponse<?> getStudyNotices(@PathVariable Long study_id){
        try {
            StudyNoticesInfoRes studyNotices = studyService.getStudyNotices(study_id, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyNotices);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{study_id}/completed")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_CERTIFIED')")
    public BaseResponse<?> checkCompletedStudyWeek(@PathVariable Long study_id, @RequestParam int week){
        try {
            ProgressRes progressReq = studyService.checkCompletedStudyWeek(study_id, week, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, progressReq);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{study_id}/progress")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_CERTIFIED')")
    public BaseResponse<?> getStudyProgressList(@PathVariable Long study_id){
        try {
            Map<String, Object> studyProgressList = studyService.getStudyProgressList(study_id, getEmail());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyProgressList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{study_id}/details")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_CERTIFIED')")
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
