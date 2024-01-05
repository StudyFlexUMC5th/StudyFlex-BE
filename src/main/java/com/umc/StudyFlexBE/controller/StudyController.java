package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.request.StudyNoticeReq;
import com.umc.StudyFlexBE.dto.request.StudyReq;
import com.umc.StudyFlexBE.dto.response.*;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.service.StudyService;
import jakarta.validation.Valid;
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

    @PostMapping
    public BaseResponse<?> createStudy(@RequestBody StudyReq study, @AuthenticationPrincipal Member member){
        studyService.createStudy(study, member);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @GetMapping("/checkName")
    public BaseResponse<?> checkDuplicateStudyName(@RequestParam String study_name){
        studyService.checkDuplicateStudyName(study_name);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS,"사용가능한 스터디 이름입니다.");
    }

    @GetMapping("/{study_id}/checkAuthority")
    public BaseResponse<?> checkAuthority(@PathVariable Long study_id, @AuthenticationPrincipal Member member){
        StudyAuthorityType studyAuthorityType = studyService.checkAuthority(study_id, member);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyAuthorityType);
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
    @GetMapping("/ranking")
    public ResponseEntity<List<Study>> getStudyRanking() {
        List<Study> rankedStudies = studyService.getRankedStudies();
        return ResponseEntity.ok(rankedStudies);
    }

    @PostMapping("/{study_id}/postNotice")
    public BaseResponse<?> postStudyNotice(
            @PathVariable Long study_id,
            @AuthenticationPrincipal Member member,
            @RequestBody @Valid StudyNoticeReq studyNoticeReq){

        studyService.postStudyNotice(study_id,member,studyNoticeReq);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "공지사항이 등록되었습니다.");
    }

    @GetMapping("/{study_id}/notice/{notice_id}")
    public BaseResponse<?> getStudyNotice(@PathVariable Long study_id, @PathVariable Long notice_id, @AuthenticationPrincipal Member member){

        StudyNoticeRes studyNotice = studyService.getStudyNotice(study_id, notice_id, member);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, studyNotice);
    }

    @DeleteMapping("/{study_id}/notice/{notice_id}")
    public BaseResponse<?> deleteStudyNotice(@PathVariable Long study_id, @PathVariable Long notice_id, @AuthenticationPrincipal Member member){

        studyService.deleteStudyNotice(study_id, notice_id, member);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @GetMapping("/app/studies/{study_id}/notice")
    public BaseResponse<?> getStudyNotices(@PathVariable Long study_id, @AuthenticationPrincipal Member member){
        StudyNoticesInfoRes studyNotices = studyService.getStudyNotices(study_id, member);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS,studyNotices);
    }

    @GetMapping("/app/studies/{study_id}/completed")
    public BaseResponse<?> checkCompletedStudyWeek(@PathVariable Long study_id, @RequestParam int week, @AuthenticationPrincipal Member member){
        studyService.checkCompletedStudyWeek(study_id,week,member);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS,"학습 완료 체크가 정상적으로 처리되었습니다.");
    }
}
