package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.request.ComplaintRequestDto;
import com.umc.StudyFlexBE.dto.response.BaseResponse;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.response.ComplaintResponseDto;
import com.umc.StudyFlexBE.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.umc.StudyFlexBE.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/app/studies")
public class ComplaintController {
    private final ComplaintService complaintService;

    @Autowired
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping("/{studyId}/complaint")
    public ResponseEntity<BaseResponse<ComplaintResponseDto>> postComplaint(
            @PathVariable Long studyId,
            @RequestBody ComplaintRequestDto request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long memberId = userDetails.getMemberId();

            ComplaintResponseDto complaintResponse = complaintService.postComplaint(memberId, studyId, request);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, complaintResponse));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
        }
    }
}